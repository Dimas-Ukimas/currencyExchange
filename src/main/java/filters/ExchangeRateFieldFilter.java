package filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebFilter("/exchangeRates")
public class ExchangeRateFieldFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if ("POST".equalsIgnoreCase(request.getMethod())) {

            String baseCurrencyCode = request.getParameter("baseCurrencyCode");
            String targetCurrencyCode = request.getParameter("targetCurrencyCode");
            String rate = request.getParameter("rate");

            boolean isBaseCurrencyCodeValid = baseCurrencyCode != null && baseCurrencyCode.matches("^[A-Za-z]{3}$");
            boolean isTargetCurrencyCodeValid = targetCurrencyCode != null && targetCurrencyCode.matches("^[A-Za-z]{3}$") && !targetCurrencyCode.equals(baseCurrencyCode);
            boolean isRateValid = rate != null && rate.matches("^\\d+(\\.\\d+)?([eE][-+]?\\d+)?$") && new BigDecimal(rate).compareTo(BigDecimal.ZERO) != 0;

            if (!isBaseCurrencyCodeValid || !isTargetCurrencyCodeValid || !isRateValid) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), "One or more fields are absent or invalid");

                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
