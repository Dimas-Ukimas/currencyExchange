package servlet.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.getAllExchangeRates();

            if (exchangeRates.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                        "message",
                        "There is no exchange rates in data base"
                ));

                return;
            }

            objectMapper.writeValue(resp.getWriter(), exchangeRates);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                    "message",
                    "Oops! Something went wrong in database. Please, try again later"
            ));
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ExchangeRateRequestDto dto = new ExchangeRateRequestDto();

        String baseCurrencyCode = req.getParameter("baseCurrencyCode").toUpperCase();
        String targetCurrencyCode = req.getParameter("targetCurrencyCode").toUpperCase();
        String rate = req.getParameter("rate");

        dto.setBaseCurrencyCode(baseCurrencyCode);
        dto.setTargetCurrencyCode(targetCurrencyCode);
        dto.setRate(new BigDecimal(rate));

        try {
            Optional<ExchangeRateResponseDto> savedExchangeRate = exchangeRateService.saveExchangeRate(dto);

            if (savedExchangeRate.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                        "message",
                        "One(or both) currencies does not exist in database"
                ));

                return;
            }

            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), savedExchangeRate);

        } catch (SQLException e) {
            int SQLITE_CONSTRAINT_VIOLATION_ERROR_CODE = 19;
            if (e.getErrorCode() == SQLITE_CONSTRAINT_VIOLATION_ERROR_CODE) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                        "message",
                        "This exchange rate is already exist in database"
                ));

                return;
            }

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                    "message",
                    "Oops! Something went wrong in database. Please, try again later"
            ));
        }
    }
}
