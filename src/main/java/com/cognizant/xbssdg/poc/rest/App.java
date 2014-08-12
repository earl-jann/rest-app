package com.cognizant.xbssdg.poc.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.cognizant.xbssdg.poc.rest.exceptions.AppExceptionMapper;
import com.cognizant.xbssdg.poc.rest.exceptions.GenericExceptionMapper;
import com.cognizant.xbssdg.poc.rest.exceptions.NotFoundExceptionMapper;
import com.cognizant.xbssdg.poc.rest.resource.UserResource;
import com.cognizant.xbssdg.poc.rest.util.CORSResponseFilter;
import com.cognizant.xbssdg.poc.rest.util.LoggingResponseFilter;

/**
 * Hello world!
 * 
 */
public class App extends ResourceConfig {
	public App() {
        // register application resources
//		register(UserRestService.class);
		register(UserResource.class);
 
        // register filters
        register(RequestContextFilter.class);
        register(LoggingResponseFilter.class);
        register(CORSResponseFilter.class);
 
        // register exception mappers
        register(GenericExceptionMapper.class);
        register(AppExceptionMapper.class);
        register(NotFoundExceptionMapper.class);
 
        // register features
        register(JacksonFeature.class);
        register(MultiPartFeature.class);		
	}
}
