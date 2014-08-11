package com.cognizant.xbssdg.poc.rest.service.integration;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import com.cognizant.xbssdg.poc.rest.entities.User;

public class RestDemoServiceIT {

	public static final String TEST_URL = "http://127.0.0.1:8080/rest-app-0.0.1-SNAPSHOT";

	@Test
	public void testGetUsers() throws JsonGenerationException,
			JsonMappingException, IOException {		
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client.target(TEST_URL + "/users/");

		Builder request = webTarget.request();
		request.header("Content-type", MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		List<User> users = response.readEntity(new GenericType<List<User>>() {
		});

		ObjectMapper mapper = new ObjectMapper();
		System.out.print(mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(users));

		Assert.assertTrue("At least one user is present", users.size() > 0);
	}

	@Test
	public void testGetUser() throws JsonGenerationException,
			JsonMappingException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client.target(TEST_URL + "/users/2");

		Builder request = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		User user = response.readEntity(User.class);

		ObjectMapper mapper = new ObjectMapper();
		System.out
				.print("Received user from database *************************** "
						+ mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(user));

	}

	@Test
	public void testCreateUser() throws JsonGenerationException,
			JsonMappingException, IOException {
		
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		User user = new User("bmuregasan","muregasan","bhaskar");
		
		Response result = client.target(TEST_URL).path("users")        
        .request(MediaType.TEXT_HTML).accept(MediaType.APPLICATION_JSON)
        .post(Entity.json(user), Response.class);
		
		Assert.assertTrue(result.getStatus() == 201);
//		
//		WebTarget webTarget2 = client.target(result.getHeaderString("Location"));
//        
//		Builder request2 = webTarget2.request(MediaType.APPLICATION_JSON);
//		
//		Response response = request2.delete();
//		
//		Assert.assertTrue(response.getStatus() == 200);
		
	}
}
