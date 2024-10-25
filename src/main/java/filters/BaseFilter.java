package filters;


import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.PropertiesUtil;

import java.io.UnsupportedEncodingException;

public abstract class BaseFilter implements Filter {

    protected void setCorsHeaders(HttpServletResponse servletResponse) {

        servletResponse.setHeader("Access-Control-Allow-Origin", PropertiesUtil.get("api.url"));
        servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        servletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        servletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    }

    protected void setRequestResponseEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }
}
