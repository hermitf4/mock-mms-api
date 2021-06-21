package com.nttdata.mock.mms.api.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.mock.mms.api.enums.MockAuthExceptionEnum;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.jwt.JwtTokenUtil;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;
import com.nttdata.mock.mms.api.utils.Constants;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("X-auth");
		String jwtToken = null;
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				String authType = getAuthType(jwtToken);
				if(authType.equals(Constants.AUTHFEDERA) && checkCookieFedera(request) != null) {
					validateToken(jwtToken, request);
				}else if (authType.equals(Constants.AUTHLDAP)){
					validateToken(jwtToken, request);
				}
			} catch (MockMmmsException e) {
				LOG.error("Invalid token!", e);
				ResponseBase errorResponse = new ResponseBase();
				errorResponse.setSuccess(false);
				errorResponse.setMessage(e.getMessage());
				errorResponse.setResultCode(401001);
	            response.setStatus(e.getHttpCode());
	            response.getWriter().write(convertObjectToJson(errorResponse));
			}
		} 
		
		chain.doFilter(request, response);
	}
	
	private Cookie checkCookieFedera(HttpServletRequest request) throws MockMmmsException{
		
		List<Cookie> cookies = Optional.ofNullable(request.getCookies()).map(Arrays::stream).orElseGet(Stream::empty).collect(Collectors.toList());
		
		return cookies.stream().filter(cookie -> cookie.getName().equals(Constants.AUTHFEDERA)).findAny().orElseThrow(MockAuthExceptionEnum.TOKEN_FEDERA_EXCEPTION);
		
	}

	private String getAuthType(String jwtToken) throws MockMmmsException {
		
		return jwtTokenUtil.validateToken(jwtToken).getClaim(Constants.AUTHTYPE).asString();
		
	}
	
	
	private void validateToken(String jwtToken, HttpServletRequest request) throws MockMmmsException {
		
		if (SecurityContextHolder.getContext().getAuthentication() == null && jwtTokenUtil.validateToken(jwtToken) != null) {	
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtToken, jwtToken);
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);	
			
		}
			
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
