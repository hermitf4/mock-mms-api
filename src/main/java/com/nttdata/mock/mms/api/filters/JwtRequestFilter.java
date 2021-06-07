package com.nttdata.mock.mms.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nttdata.mock.mms.api.utils.JwtTokenUtil;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	
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
			
			if (jwtTokenUtil.validateToken()) {
				UsernamePasswordAuthenticationToken authReq
				 = new UsernamePasswordAuthenticationToken("test", "password");
				Authentication auth = authenticationManager.authenticate(authReq);
				SecurityContext sc = SecurityContextHolder.getContext();
				sc.setAuthentication(auth);
			}
		}
		
		chain.doFilter(request, response);
	}
}
