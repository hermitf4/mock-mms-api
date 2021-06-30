package com.nttdata.mock.mms.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nttdata.mock.mms.api.AbstractTest;

class AuthenticationManagementControllerTest extends AbstractTest{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagementControllerTest.class);
	
	@Test
	void getAuthenticationFedera() throws Exception {
		logger.info("getAuthenticationFedera() IN");
		
		String url = "/getAuthenticationFedera";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
				      .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		
		assertTrue(mvcResult.getResponse().getContentAsString().length() > 0);
		
		logger.info("Response {}", mvcResult.getResponse().getContentAsString());
		
		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(HttpStatus.OK.value(), status);
		
		logger.info("getAuthenticationFedera() OUT");
	}
	
}
