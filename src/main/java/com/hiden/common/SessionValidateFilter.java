package com.hiden.common;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by myron on 16-9-18.
 */
public class SessionValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
