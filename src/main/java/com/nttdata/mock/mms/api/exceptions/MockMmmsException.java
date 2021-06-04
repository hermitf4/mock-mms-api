package com.nttdata.mock.mms.api.exceptions;

public class MockMmmsException extends Exception {

	private static final long serialVersionUID = 1L;

	private final int httpCode;
	private final int errorCode;
	private final String label;
	private final String message;

	public MockMmmsException(int httpCode, int errorCode, String label, String message) {
		this.message = message != null ? message : super.getMessage();
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.label = label;

	}

	public MockMmmsException(int httpCode, int errorCode, String label) {
		super(label);
		this.message = label;
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.label = label;
	}

	public MockMmmsException(int httpCode, int errorCode, String label, String message, Throwable cause) {
		super(message, cause);
		this.message = message != null ? message : super.getMessage();
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.label = label;

	}

	public MockMmmsException label(String label) {
		return new MockMmmsException(httpCode, errorCode, label, message);
	}

	public MockMmmsException message(String message) {
		return new MockMmmsException(httpCode, errorCode, label, message);
	}

	public MockMmmsException concatMessage(String message) {
		return new MockMmmsException(httpCode, errorCode, label, String.join(" - ", this.message, message));
	}

	public int getHttpCode() {
		return httpCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getLabel() {
		return label;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + errorCode;
		result = prime * result + httpCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		// Return false if the other object has the wrong type, interface, or is null.
		if (!(obj instanceof MockMmmsException)) {
			return false;
		}
		MockMmmsException other = (MockMmmsException) obj;
		return errorCode == other.errorCode && httpCode == other.httpCode;
	}

}
