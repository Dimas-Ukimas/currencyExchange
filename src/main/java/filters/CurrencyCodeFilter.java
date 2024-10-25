package filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@WebFilter("/currency/*")
public class CurrencyCodeFilter extends BaseFilter {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        setCorsHeaders(response);
        setRequestResponseEncoding(request, response);

        boolean isCodeValid = request.getPathInfo().substring(1).matches("^[A-Za-z]{3}$");

        if (!isCodeValid) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), Collections.singletonMap(
                    "message",
                    "Invalid currency code. Please, use only 3 latin letters"
            ));

            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
