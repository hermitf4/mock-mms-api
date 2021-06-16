package com.nttdata.mock.mms.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.jwt.JwtTokenUtil;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("X-auth");
		String jwtToken = null;
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				validateToken(jwtToken, chain, request, response);
			} catch (IllegalArgumentException | MockMmmsException e) {
				ResponseBase errorResponse = new ResponseBase();
				errorResponse.setSuccess(false);
				errorResponse.setMessage(e.getMessage());
	            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            response.getWriter().write(convertObjectToJson(errorResponse));
	            chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
		
	}
	
	private void validateToken(String jwtToken, FilterChain chain, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException, MockMmmsException {
		
			if (SecurityContextHolder.getContext().getAuthentication() == null && jwtTokenUtil.validateToken(jwtToken) != null) {			
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
				 = new UsernamePasswordAuthenticationToken(jwtToken, jwtToken);
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);	
		}
		chain.doFilter(request, response);
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
