package filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/currencies")
public class CurrencyFieldFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if ("POST".equals(request.getMethod())) {

            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String sign = request.getParameter("sign");

            boolean isNameValid = name != null && name.matches("^[A-Za-z]{1,10}\\s?[A-Za-z]{0,10}\\s?[A-Za-z]{0,10}$");
            boolean isCodeValid = code != null && code.matches("^[A-Za-z]{3}");
            boolean isSignValid = sign != null && sign.matches("^(?!\\s*$).+");

            if (!isNameValid || !isCodeValid || !isSignValid) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), "One or more fields are absent or invalid.");

                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
