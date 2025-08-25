package dev.shann.mcproductservice.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoggerInterceptor.java
 * <p>
 * This class is an interceptor that logs the details of incoming requests and outgoing responses.
 * It implements the HandlerInterceptor interface from Spring MVC.
 */

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {


    // No instance variables needed for timing; use request attributes instead.


    /**
     * This method is called before the request is handled by the controller.
     *
     * @param request
     * @param response
     * @param handler
     * @return true to continue with the request, false to stop the request processing
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // Log request details here
        log.info("Request URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        var startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true; // Continue with the request processing
    }

    /**
     * This method is called after the handler is executed but before the view is rendered.
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler != null) {
            log.info("Handler: {}", handler.getClass().getName());
        }
        if (modelAndView != null) {
            log.info("Model Attributes: {}", modelAndView.getModel());
        }
        // Log response details here
        log.info("Response Status: {}", response.getStatus());
        var endTime = System.currentTimeMillis();
        request.setAttribute("endTime", endTime);
    }

    /**
     * This method is called after the request has been processed and the view has been rendered.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // This method is called after the request has been processed
        if (ex != null) {
            log.error("Exception occurred: {}", ex.getMessage());
        }
        log.info("Request Processing Time: {} ms", ((Long) request.getAttribute("endTime") - (Long) request.getAttribute("start_time")));
    }

}
