package com.nttdata.mock.mms.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nttdata.mock.mms.api.model.User;

@Configuration
public class UsersConfig {
	

	@Bean 
	public Map<String, User> users(){
		
		Map<String, User> map = new HashMap<String, User>();
	    map.put("DANILO.FAZIO", new User("Danilo","Fazio", "danilo.fazio@nttdata.com", "password", "GTFRHR45RT6RDG56"));
	    map.put("CARMELO.MILORDO", new User("Carmelo","Milordo", "carmelo.milordo@nttdata.com", "password", "EG454GTG6HDG5G5H"));
	    map.put("VINCENZO.IANNINI", new User("Vincenzo","Iannini", "vincenzo.iannini@nttdata.com", "password", "NNNVCN96C19D005H"));
	    map.put("MARIO.ROMANELLI", new User("MARCO","ROMANELLI", "MARCO.ROMANELLI@LIBERO.IT", "password", "RMNMRC66H06A944T"));
	    
	    return map;
	    
	}
	
	public ImmutablePair<String, User> checkLogin(String username, String password) {

		User user = users().get(username);
		
		return (user != null && user.getPassword().equals(password)) ? new ImmutablePair<String, User>(username, user) : null;
		
	}
	
}
