package eu.scscdev.dev.uniquepeople.assignment;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Log
public class TracingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Context path: " + request.getServletContext().getContextPath());
        chain.doFilter(request, response);
    }

}
