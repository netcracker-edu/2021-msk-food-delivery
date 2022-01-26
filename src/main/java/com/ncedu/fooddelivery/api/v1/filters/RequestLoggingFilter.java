package com.ncedu.fooddelivery.api.v1.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    public RequestLoggingFilter() {
        super.setBeforeMessagePrefix("REQUEST DATA: ");
        super.setBeforeMessageSuffix("");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.debug(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        return;
    }
}
