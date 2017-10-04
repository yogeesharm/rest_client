package com.hcl.poc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hcl.poc.rest.api.Client;
import com.hcl.poc.rest.api.Response;

public class RestControlerTest {
	
	private static Logger logger = LogManager.getLogger(RestControlerTest.class);
	public static String URL = "http://localhost:8080/RestPoc/rest/users/";
	public static String ID = "1012";

	public static void testGetUsers() {
		Client client = new Client(URL);
		logger.info("Test for getting all the Users");
		Response response =	client.get();
		logger.info(response.getBody());
	}

	public static void testGetUser() {
		logger.info("Test for getting the User");
		Client client = new Client(URL + ID);
		Response response =	client.get();
		logger.info(response.getBody());
	}

	public static void testCreateUser() {
		logger.info("Test for creating new user");
		User user = new User();
		user.setFirstName("Yogeesha");
		user.setLastName("R M");
		user.setDateOfBirth(new Date());
		user.setSex("Male");
		user.setEmail("yogeesha@gmail.com");
		user.setAddress("Bengaluru");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
			String body = mapper.writeValueAsString(user);
			Client client = new Client(URL);
			Response response = client.post(body);
			logger.info(response.getBody());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	public static void testUpdateUser() {
		logger.info("Test for updating the user");
		Client client = new Client(URL + ID);
		try {
			Response response =	client.get();
			logger.info(response.getBody());
			ObjectMapper mapper = new ObjectMapper();
			if(response.getBody() != null && !response.getBody().isEmpty()){
				User user = mapper.readValue(response.getBody(), User.class);
				user.setAddress("New Address");
				
				ObjectMapper om = new ObjectMapper();
				mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				String body = om.writeValueAsString(user);
				Response response2 = client.put(body);
				logger.info(response2.getBody());
			} else {
				logger.info("User not exists");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void testDeleteUser() {
		logger.info("Test for deleting the user");
		Client client = new Client(URL + ID);
		Response response =	client.delete();
		logger.info(response.getBody());
	}
	
	public static void main(String[] args) {
		testGetUsers(); 
		testGetUser();
		testCreateUser();
		testUpdateUser();
		testDeleteUser();
	}
}
