package com.nttdata.mock.mms.api.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.model.User;
import com.nttdata.mock.mms.api.services.IUserManagement;

@Service(value = "UserManagement")
public class UserManagementImpl implements IUserManagement{

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		
		users.add(new User("Danilo","Fazio","danilo.fazio@nttdata.com"));
		users.add(new User("Raffaele","Pezzo","raffaele.pezzo@nttdata.com"));
		users.add(new User("Carmelo","Milordo","carmelo.milordo@nttdata.com"));
		users.add(new User("Antonio","Ascanio","antonio.ascanio@nttdata.com"));
		users.add(new User("Vincenzo","Iannini","vincenzo.iannini@nttdata.com"));
		users.add(new User("Nunzio","Mauro","nunzio.mauro@nttdata.com"));
		users.add(new User("Giovanni","Zarola","giovanni.zarola@nttdata.com"));
		users.add(new User("Guido","Scarlato","guido.scarlato@nttdata.com"));
		
		return users;
	}
	
	
	
}
