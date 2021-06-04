package com.nttdata.mock.mms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.mock.mms.api.services.ItestCall;
import com.nttdata.mock.mms.api.swagger.api.TestServiceApi;
import com.nttdata.mock.mms.api.swagger.dto.ResponseBaseDTO;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;

@RestController
public class TestServicesController extends AbstractController implements TestServiceApi {

	@Autowired
	ItestCall itestCall;

	@Override
	public ResponseEntity<ResponseBaseDTO> testCall() {
		ResponseEntity<ResponseBaseDTO> result = null;

		Class<ResponseBaseDTO> responseBaseClass = ResponseBaseDTO.class;

		ResponseBase model = null;

		try {
			model = itestCall.testCall();

			ResponseBaseDTO modelDTO = mapper.map(model, ResponseBaseDTO.class);

			result = buildResponseEntitySuccess(modelDTO);
		} catch (Exception ex) {
			result = buildResponseEntityException(responseBaseClass, ex);
		}
		
		return result;
	}
}