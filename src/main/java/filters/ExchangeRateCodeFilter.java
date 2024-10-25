package filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebFilter("/exchangeRate/*")
public class ExchangeRateCodeFilter extends BaseFilter {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        setCorsHeaders(response);
        setRequestResponseEncoding(request, response);

        boolean isCurrenciesCodesValid = request.getPathInfo().substring(1).matches("^[A-Za-z]{6}$");

        if (!isCurrenciesCodesValid) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), Collections.singletonMap(
                    "message",
                    "Currencies codes are invalid or absent"
            ));

            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
