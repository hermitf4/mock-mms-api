package com.nttdata.mock.mms.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nttdata.mock.mms.api.utils.JwtTokenUtil;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("X-auth");
		
		if (requestTokenHeader != null) {
			JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(requestTokenHeader);
			
			try {
				validateToken(jwtTokenUtil, chain, request, response);
			} catch (IllegalArgumentException e) {
				LOG.error("Unable to get JWT Token", e);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
	
	
	private void validateToken(JwtTokenUtil jwtTokenUtil, FilterChain chain, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			
			if (jwtTokenUtil.validateToken("test")) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
				 = new UsernamePasswordAuthenticationToken("test", "password");
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		chain.doFilter(request, response);
	}
}
