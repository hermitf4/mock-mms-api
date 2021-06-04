package com.nttdata.mock.mms.api.controllers;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.swagger.dto.ResponseBaseDTO;
import com.nttdata.mock.mms.api.utils.Constants;
import com.nttdata.mock.mms.api.utils.LogConstants;

public abstract class AbstractController {

	@Autowired
	protected ModelMapper mapper;

	@Autowired
	protected HttpServletResponse httpResponse;

	protected <T extends ResponseBaseDTO> T buildResponseSuccess(T response) {
		response.success(true);
		response.resultCode(Constants.SUCCESS_CODE);
		response.setTransactionId(httpResponse.getHeader(LogConstants.TRANSACTION_ID_KEY));

		return response;
	}

	protected <T extends ResponseBaseDTO> T buildResponseSuccess(Class<T> responseClass) {
		return buildResponseSuccess(getResponseBaseImplementation(responseClass));
	}

	protected <T extends ResponseBaseDTO> T buildResponseException(T response, Exception ex) {
		response.success(false).setTransactionId(httpResponse.getHeader(LogConstants.TRANSACTION_ID_KEY));

		if (ex instanceof MockMmmsException) {

			MockMmmsException sex = (MockMmmsException) ex;

			String message = StringUtils.defaultIfBlank(sex.getMessage(), sex.getLabel());

			response.message(message).resultCode(sex.getErrorCode());
		} else {
			response.message(Constants.DEFAULT_HTTP_500_MESSAGE).errors(Arrays.asList(ExceptionUtils.getStackTrace(ex)))
					.resultCode(500000);
		}

		return response;
	}

	protected <T extends ResponseBaseDTO> ResponseEntity<T> buildResponseEntitySuccess(T responseBaseFa) {
		return new ResponseEntity<>(responseBaseFa, HttpStatus.OK);
	}

	protected <T extends ResponseBaseDTO> ResponseEntity<T> buildResponseEntityException(Class<T> responseBaseFaClass,
			Exception ex) {
		T responseBase = this.getResponseBaseImplementation(responseBaseFaClass);

		buildResponseException(responseBase, ex);

		HttpStatus returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		if ((ex instanceof MockMmmsException)) {
			returnStatus = HttpStatus.valueOf(((MockMmmsException) ex).getHttpCode());
		}

		return new ResponseEntity<>(responseBase, returnStatus);
	}

	protected <T extends ResponseBaseDTO> ResponseEntity<T> buildResponseEntitySuccess(T responseBase,
			Map<String, String> headers) {

		HttpHeaders httpHeaders = new HttpHeaders();
		headers.entrySet().stream().forEach(e -> httpHeaders.add(e.getKey(), e.getValue()));

		return new ResponseEntity<>(responseBase, httpHeaders, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	private <T extends ResponseBaseDTO> T getResponseBaseImplementation(Class<T> responseBaseFaClass) {
		T responseBase;
		try {
			responseBase = responseBaseFaClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			responseBase = (T) new ResponseBaseDTO();
		}

		return responseBase;
	}
}
