package com.cognizant.xbssdg.poc.rest.resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.xbssdg.poc.rest.exceptions.AppException;
import com.cognizant.xbssdg.poc.rest.service.UserManagementService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
@Path("/users")
public class UserResource {
//	@etormes: i dunno but for some weird reason this doesn't get injected so I put it into the method params...
//	@Context
//	private UriInfo uriInfo;
//
//	public UriInfo getUriInfo() {
//		return uriInfo;
//	}
//
//	public void setUriInfo(UriInfo uriInfo) {
//		this.uriInfo = uriInfo;
//	}

	@Autowired
	private UserManagementService userManagementService;

	/*
	 * *********************************** CREATE ***********************************
	 */

	/**
	 * Adds a new resource (user) from the given json format (at least username, 
	 * last name and first name are required)
	 * 
	 * @param user
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createUser(User user, @Context UriInfo uriInfo) throws AppException {
		Long createUserId = userManagementService.createUser(convertToEntity(user));
		return Response.status(Response.Status.CREATED)
				// 201
				.entity("A new user has been created")
				.header("Location",
						uriInfo.getAbsolutePath().toString() + "/"
								+ String.valueOf(createUserId)).build();
	}

	/**
	 * Adds a new user (resource) from "form" (at least username, 
	 * last name and first name elements are required at the DB level)
	 * 
	 * @param username
	 * @param lastname
	 * @param firstname
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response createUserFromApplicationFormURLencoded(
			@FormParam("username") String username,
			@FormParam("lastname") String lastName,
			@FormParam("firstname") String firstName,
			@Context UriInfo uriInfo) throws AppException {

		User user = new User(username, lastName, firstName);
		Long createUserid = userManagementService.createUser(convertToEntity(user));

		return Response
				.status(Response.Status.CREATED)
				// 201
				.entity("A new user/resource has been created at " + uriInfo.getPath()
						+ createUserid)
						.header("Location",
								uriInfo.getAbsolutePath().toString() + "/"
										+ String.valueOf(createUserid)).build();
	}

	/**
	 * A list of resources (here users) provided in json format will be added
	 * to the database.
	 * 
	 * @param users
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createUsers(List<User> users) throws AppException {
		List<com.cognizant.xbssdg.poc.rest.entities.User> userEntities = new ArrayList<com.cognizant.xbssdg.poc.rest.entities.User>();
		for (User user : users) {
			userEntities.add(convertToEntity(user));
		}
		userManagementService.createUsers(userEntities);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of users was successfully created").build();
	}

	/*
	 * *********************************** READ ***********************************
	 */
	/**
	 * Returns all resources (users) from the database
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws AppException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })	
	public List<User> getUsers()
			throws IOException,	AppException {
		List<com.cognizant.xbssdg.poc.rest.entities.User> users= userManagementService.getUsers();
		
		List<User> usersWs = new ArrayList<User>();
		
		for (com.cognizant.xbssdg.poc.rest.entities.User user : users) {
			usersWs.add(convertToWs(user));
		}
		return usersWs;
	}
	
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getUserById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
			throws IOException,	AppException {
		com.cognizant.xbssdg.poc.rest.entities.User userEntityById = userManagementService.getUserById(id);
		
		User userById = convertToWs(userEntityById);
		return Response.status(200).entity(new GenericEntity<User>(userById) {}, detailed ? new Annotation[]{UserDetailedView.Factory.get()} : new Annotation[0])
				.header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}
	
	@GET
	@Path("login/{username}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getUserByUsername(@PathParam("username") String username, @QueryParam("detailed") boolean detailed)
			throws IOException,	AppException {
		com.cognizant.xbssdg.poc.rest.entities.User userEntityByUsername = userManagementService.getUserByUsername(username);
		
		User userByUsername = convertToWs(userEntityByUsername);
		return Response.status(200).entity(new GenericEntity<User>(userByUsername) {}, detailed ? new Annotation[]{UserDetailedView.Factory.get()} : new Annotation[0])
				.header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}


	/*
	 * *********************************** UPDATE ***********************************
	 */

	/**
	 * The method offers both Creation and Update resource functionality. If
	 * there is no resource yet at the specified location, then a user
	 * creation is executed and if there is then the resource will be full
	 * updated.
	 * 
	 * @param id
	 * @param user
	 * @return
	 * @throws AppException
	 */
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putUserById(@PathParam("id") Long id, User user, @Context UriInfo uriInfo)
			throws AppException {

		com.cognizant.xbssdg.poc.rest.entities.User userById = userManagementService.verifyUserExistenceById(id);

		if (userById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createUserId = userManagementService.createUser(convertToEntity(user));
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new user has been created AT THE LOCATION you specified")
					.header("Location",
							uriInfo.getAbsolutePath().toString() + "/"
									+ String.valueOf(createUserId)).build();
		} else {
			// resource is existent and a full update should occur
			userManagementService.updateFullyUser(convertToEntity(user));
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The user you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							uriInfo.getAbsolutePath().toString() + "/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdateUser(@PathParam("id") Long id,
			User user) throws AppException {
		user.setId(id);
		userManagementService.updatePartiallyUser(convertToEntity(user));
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The user you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteUserById(@PathParam("id") Long id) {
		userManagementService.deleteUserById(id);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("User successfully removed from database").build();
	}

	@DELETE
	@Produces({ MediaType.TEXT_HTML })
	public Response deleteUsers() {
		userManagementService.deleteUsers();
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("All users have been successfully removed").build();
	}

	public void setuserManagementService(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}
	
	private com.cognizant.xbssdg.poc.rest.entities.User convertToEntity(User user) {
		com.cognizant.xbssdg.poc.rest.entities.User userEntity = new com.cognizant.xbssdg.poc.rest.entities.User();
		try {
			BeanUtils.copyProperties(userEntity, user);
			return userEntity;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private User convertToWs(com.cognizant.xbssdg.poc.rest.entities.User user) {
		User userWs = new User();
		try {
			BeanUtils.copyProperties(userWs, user);
			return userWs;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
