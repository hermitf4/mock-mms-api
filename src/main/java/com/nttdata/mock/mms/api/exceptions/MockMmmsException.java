package com.nttdata.mock.mms.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class MockMmmsException extends Exception {

	private static final long serialVersionUID = 1L;

	private final int httpCode;
	private final int errorCode;
	private final String label;
	private final String message;

	public MockMmmsException(int httpCode, int errorCode, String label) {
		super(label);
		this.message = label;
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.label = label;
	}

	public MockMmmsException concatMessage(String message) {
		return new MockMmmsException(httpCode, errorCode, label, String.join(" - ", this.message, message));
	}

}
