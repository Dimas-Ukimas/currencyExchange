package servlet.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String baseCurrencyCode = req.getPathInfo().substring(1, 4).toUpperCase();
        String targetCurrencyCode = req.getPathInfo().substring(4, 7).toUpperCase();

        try {
            Optional<ExchangeRateResponseDto> exchangeRate = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);

            if (exchangeRate.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(),
                        "Currency exchange not found.");

                return;
            }

            objectMapper.writeValue(resp.getWriter(), exchangeRate.get());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Oops! Something went wrong in database. Please, try again later.");
        }
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ExchangeRateRequestDto dto = new ExchangeRateRequestDto();

        String baseCurrencyCode = req.getPathInfo().substring(1, 4).toUpperCase();
        String targetCurrencyCode = req.getPathInfo().substring(4, 7).toUpperCase();
        String[] params = req.getReader().lines().collect(Collectors.joining()).split("[=&]");
        String rate = null;

        dto.setBaseCurrencyCode(baseCurrencyCode);
        dto.setTargetCurrencyCode(targetCurrencyCode);

        for (int i = 0; i < params.length; i++) {
            if ("rate".equalsIgnoreCase(params[i]) & i + 1 < params.length) {
                rate = params[i + 1];
            }
        }

        if (rate == null || !rate.matches("^\\d+(\\.\\d+)?([eE][-+]?\\d+)?$")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), "Rate field is empty or invalid");

            return;
        }

        dto.setRate(new BigDecimal(rate));

        try {
            Optional<ExchangeRateResponseDto> exchangeRate = exchangeRateService.updateExchangeRate(dto);

            if (exchangeRate.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), "Such exchange rate does not exist in database.");

                return;
            }

            objectMapper.writeValue(resp.getWriter(), exchangeRate.get());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Oops! Something went wrong in database. Please, try again later.");
        }
    }
}
