
package servlet.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<CurrencyResponseDto> currencies = currencyService.getAllCurrencies();

            objectMapper.writeValue(resp.getWriter(), currencies);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Oops! Something went wrong in database. Please, try again later.");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CurrencyRequestDto reqDto = new CurrencyRequestDto();

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        reqDto.setName(name);
        reqDto.setCode(code);
        reqDto.setSign(sign);

        try {
            Optional<CurrencyResponseDto> savedCurrency = currencyService.saveCurrency(reqDto);

            if (savedCurrency.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                objectMapper.writeValue(resp.getWriter(), "This currency is already exist");

                return;
            }

            objectMapper.writeValue(resp.getWriter(), savedCurrency.get());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Oops! Something went wrong in database. Please, try again later.");
        }

    }
}
