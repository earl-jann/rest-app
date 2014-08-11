package com.cognizant.xbssdg.poc.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.cognizant.xbssdg.poc.rest.service.UserRestService;
import com.cognizant.xbssdg.poc.rest.util.CORSResponseFilter;
import com.cognizant.xbssdg.poc.rest.util.LoggingResponseFilter;

/**
 * Hello world!
 * 
 */
public class App extends ResourceConfig {
	public App() {
		register(RequestContextFilter.class);
		register(UserRestService.class);
		register(JacksonFeature.class);
		register(LoggingResponseFilter.class);
		register(CORSResponseFilter.class);
	}
}
