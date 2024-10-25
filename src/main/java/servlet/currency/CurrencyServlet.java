package servlet.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.CurrencyResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String currencyCode = req.getPathInfo().replace("/", "");

        try {
            Optional<CurrencyResponseDto> currency = currencyService.getCurrency(currencyCode);

            if (currency.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                        "message",
                        "Currency not found in database"
                ));

                return;
            }

            objectMapper.writeValue(resp.getWriter(), currency.get());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), Collections.singletonMap(
                    "message",
                    "Oops! Something went wrong in database. Please, try again later"
            ));
        }
    }
}
