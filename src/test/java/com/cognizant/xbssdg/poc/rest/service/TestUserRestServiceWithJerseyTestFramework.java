package com.cognizant.xbssdg.poc.rest.service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cognizant.xbssdg.poc.rest.App;

public class TestUserRestServiceWithJerseyTestFramework extends JerseyTest{
	
//	public static final String MOCK_SPRING_APPLICATIONCONTEXT = "classpath:spring/applicationContext.xml";	
//    
//	private UserRestService UserRestService;
//	
//	private UserDao UserDao;
//	
//	
//	@Override
//    protected Application configure() {
//        ResourceConfig resourceConfig = new MyDemoApplication();
//        enable(TestProperties.LOG_TRAFFIC);
//        enable(TestProperties.DUMP_ENTITY);
//        resourceConfig.property("contextConfigLocation", MOCK_SPRING_APPLICATIONCONTEXT); // Set which application context to use
//        return resourceConfig;
//    }
//	
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        UserRestService = (UserRestService) getSpringApplicationContext().getBean("UserRestService");
//        UserDao = (UserDao) getSpringApplicationContext().getBean("UserDao");
//        Assert.assertNotNull(UserRestService);
//    }
//
//    @After
//    public void after() throws Exception {
//        super.tearDown();
//    }
	
    @Override
    protected Application configure() {
        // Enable logging.
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        
        return new App()
        			.property("contextConfigLocation", "classpath:test-applicationContext.xml");
    }
 	

    @Ignore @Test
    public void testJerseyResource() {
        // Make a better test method than simply outputting the result.
        System.out.println(target("spring-hello").request().get(String.class));
    }
    
    @Ignore @Test
    public void testSmth(){
    	Response response = target("rest-app-0.0.1-SNAPSHOT/Users/2").request().get(Response.class);
    	Assert.assertEquals(200, response.getStatus());
    }
	
}
