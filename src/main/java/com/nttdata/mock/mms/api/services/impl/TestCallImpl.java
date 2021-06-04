package com.nttdata.mock.mms.api.services.impl;

import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.services.ItestCall;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;
import com.nttdata.mock.mms.api.utils.Loggable;

@Service(value = "testCallImpl")
public class TestCallImpl implements ItestCall {

	@Override
	@Loggable
	public ResponseBase testCall() throws MockMmmsException {
		ResponseBase result = new ResponseBase();

		// TODO: Test Response @DF
		result.setSuccess(Boolean.TRUE);
		result.setResultCode(200);
		result.setMessage("Successful Operation.");

		return result;
	}

}
