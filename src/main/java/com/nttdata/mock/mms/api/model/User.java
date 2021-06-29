package com.nttdata.mock.mms.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class User {
	
	private String firstName;
	private String lastName;
	private String email;
	
	@JsonIgnoreProperties
	private String password;
	@JsonIgnoreProperties
	private String codiceFiscale;
	
	
	public User() {
		super();
	}
	
	public User(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public User(String firstName, String lastName, String email, String password, String codiceFiscale) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.codiceFiscale = codiceFiscale;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
	
	
}
