package com.nttdata.mock.mms.api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nttdata.mock.mms.api.model.User;

@Configuration
public class UsersConfig {
	

	@Bean 
	public Map<String, User> users(){
		
		Map<String, User> map = new HashMap<String, User>();
	    map.put("GTFRHR45RT6RDG56", new User("Danilo","Fazio", "danilo.fazio@nttdata.com", "password"));
	    map.put("EG454GTG6HDG5G5H", new User("Carmelo","Milordo", "carmelo.milordo@nttdata.com", "password"));
	    map.put("NNNVCN96C19D005H", new User("Vincenzo","Iannini", "vincenzo.iannini@nttdata.com", "password"));
	    map.put("RMNMRC66H06A944T", new User("MARCO","ROMANELLI", "MARCO.ROMANELLI@LIBERO.IT", "password"));
	    
	    return map;
	    
	}
	
	public User checkLogin(String username, String password) {
		
		User user = users().get(username);
		
		return (user != null && user.getPassword().equals(password)) ? user : null;
		
	}
	
}
