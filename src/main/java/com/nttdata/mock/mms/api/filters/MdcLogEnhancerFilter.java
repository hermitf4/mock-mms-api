package com.nttdata.mock.mms.api.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.nttdata.mock.mms.api.utils.CustomResponseWrapper;
import com.nttdata.mock.mms.api.utils.LogConstants;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLogEnhancerFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do nothing.
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MDC.put(LogConstants.UUID_KEY, UUID.randomUUID().toString());

		HttpServletResponse customResponse = (HttpServletResponse) response;

		CustomResponseWrapper customWrapper = new CustomResponseWrapper(customResponse);
		customWrapper.addHeader(LogConstants.TRANSACTION_ID_KEY, MDC.get(LogConstants.UUID_KEY));

		chain.doFilter(request, customWrapper);

		MDC.remove(LogConstants.UUID_KEY);
	}

	@Override
	public void destroy() {
		// Do nothing.
	}
}
