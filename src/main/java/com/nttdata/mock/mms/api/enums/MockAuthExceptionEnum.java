package com.nttdata.mock.mms.api.enums;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;

public enum MockAuthExceptionEnum implements Supplier<MockMmmsException>{
	
	//---------------HTTP 401---------------------/
	
	TOKEN_FEDERA_EXCEPTIOM(new MockMmmsException(HttpStatus.UNAUTHORIZED.value(), 401001, "Federa Token not found"));
	
	private final MockMmmsException exception;

	private MockAuthExceptionEnum(MockMmmsException exception) {
        this.exception = exception;
    }

	@Override
	public MockMmmsException get() {
		return exception;
	}
}
