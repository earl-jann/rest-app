package com.cognizant.xbssdg.poc.rest.service;
import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class UserRestServiceTestWithRestAssured {

	@Test
	public void testUserFetchSuccessful(){
		expect().
			body("id", equalTo("2")).
			body("username", equalTo("earl_tormes")).
		when().
			get("/rest-app-0.0.1-SNAPSHOT/users/1");
	}
}
