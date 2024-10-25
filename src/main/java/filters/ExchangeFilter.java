package filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

@WebFilter("/exchange")
public class ExchangeFilter extends BaseFilter{

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        setCorsHeaders(response);
        setRequestResponseEncoding(request, response);

        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amount = request.getParameter("amount");

        boolean isBaseCurrencyCodeValid = baseCurrencyCode != null && baseCurrencyCode.matches("^[A-Za-z]{3}$");
        boolean isTargetCurrencyCodeValid = targetCurrencyCode != null && baseCurrencyCode.matches("^[A-Za-z]{3}$") && !targetCurrencyCode.equals(baseCurrencyCode);
        boolean isAmountValid = amount != null && amount.matches("^\\d+(\\.\\d+)?([eE][-+]?\\d+)?$") && new BigDecimal(amount).compareTo(BigDecimal.ZERO) != 0;

        if (!isBaseCurrencyCodeValid || !isTargetCurrencyCodeValid || !isAmountValid) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), Collections.singletonMap(
                    "message",
                    "One or more parameters are absent or invalid"
            ));

            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
