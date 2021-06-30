package com.nttdata.mock.mms.api.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.servlet.http.Cookie;

import com.nttdata.mock.mms.api.AbstractTest;
import com.nttdata.mock.mms.api.jwt.JwtTokenUtil;

class JwtTokenUtilTest extends AbstractTest{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtilTest.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Test
	void decodeJwtToken() throws Exception {
		logger.info("decodeJwtToken() IN");
		
		Cookie cookieFedera = new Cookie("AuthFEDERA", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1QkI2RjFCQ0QwMzYzQTJFMURFMkVGODMzNDI4NjgwNyIsIkNETUNPRElDRUZJU0NBTEUiOiJIU1NCTFI4MEwxN1ozMzZFIiwiZXhwIjoxNjIzODMwNTY1LCJpYXQiOjE2MjM3NDQxNjZ9.G-vo2zvwB7gCbxvtvw_ozVSldO7qaxLJJRkVAFZDy8q27ErKMSxYiOPK3WlAMkdZa7C1n9hLNlDimLo8tjmd5rVcmQQS6PqHBYz_X6tFpcC0k3DgTNvBpGHi6NPHLGo-t4zuromI_ZMqeeUg6w2Gu93mwgEj-RTDuFwQ_Bo-6tvVpkzVA6UMgdMck1ieOJBjHV5sCPUdJUYdop1PrRboCvZ3c2mjUA-1dVKfm-I0n0yLk2uaUMU5N-UzRV45otRYTSawr_J2FP-mN7MIavTnV9h9PTd5lqsahHKckmjQP7mpBuX7e_IF9Ial4RKiwfbH6gf6iLuGQ2Stp-E_P1yLfg");
		
		String tokenFEDERA = cookieFedera.getValue();
		
		assertNotNull(jwtTokenUtil.decodeJwtTokenFedera(tokenFEDERA).toString() !=  null);
		
		logger.info("decodeJwtToken() OUT");
	}
	
}
