package servlet.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.ExchangeRequestDto;
import dto.ExchangeResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final ExchangeService service = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ExchangeRequestDto dto = new ExchangeRequestDto();

        String baseCurrencyCode = req.getParameter("from").toUpperCase();
        String targetCurrencyCode = req.getParameter("to").toUpperCase();
        String amount = req.getParameter("amount");

        dto.setBaseCurrencyCode(baseCurrencyCode);
        dto.setTargetCurrencyCode(targetCurrencyCode);
        dto.setAmount(new BigDecimal(amount));

        try {
            Optional<ExchangeResponseDto> exchangeResult = service.doExchange(dto);

            if (exchangeResult.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                        "message",
                        "Exchange operation cannot be completed due to absence of needed exchange rate"
                ));

                return;
            }

            objectMapper.writeValue(resp.getWriter(), exchangeResult.get());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                    "message",
                    "Oops! Something went wrong in database. Please, try again later"
            ));
        }
    }
}
