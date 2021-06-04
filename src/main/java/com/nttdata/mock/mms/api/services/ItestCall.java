package com.nttdata.mock.mms.api.services;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;

public interface ItestCall {
	ResponseBase testCall() throws MockMmmsException;
}
