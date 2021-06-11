package com.nttdata.mock.mms.api.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nttdata.mock.mms.api.AbstractTest;
import com.nttdata.mock.mms.api.controllers.AuthenticationManagementControllerTest;

public class JwtTokenUtilTest extends AbstractTest{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagementControllerTest.class);
	
	@Test
	public void decodeJwtToken() throws Exception {
		logger.info("decodeJwtToken() IN");
		
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1QkI2RjFCQ0QwMzYzQTJFMURFMkVGODMzNDI4NjgwNyIsIkNETUNPRElDRUZJU0NBTEUiOiJIU1NCTFI4MEwxN1ozMzZFIiwiZXhwIjoxNjIzNDE0NjEwLCJpYXQiOjE2MjMzMjgyMDN9.FQPDV_Sy2awf2rfmO-xY589YmWeoSbigtfMnWOBQxvQ";
	
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(token);
		
		logger.info(jwtTokenUtil.decodeJwtToken(jwtTokenUtil.getToken()).getClaim("CDMCODICEFISCALE").asString());
		
		assertTrue(jwtTokenUtil.decodeJwtToken(jwtTokenUtil.getToken()).toString().length() > 0);
		
		logger.info("decodeJwtToken() OUT");
	}
	
}
