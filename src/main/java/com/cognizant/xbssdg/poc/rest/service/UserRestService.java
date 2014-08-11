package com.cognizant.xbssdg.poc.rest.service;

import java.io.IOException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.xbssdg.poc.rest.dao.UserDao;
import com.cognizant.xbssdg.poc.rest.entities.User;

/**
 * 
 * Service class that handles REST requests
 * 
 * @author etormes
 * 
 */
@Component
@Path("/users")
public class UserRestService {

	@Autowired
	private UserDao UserDao;

	/************************************ CREATE ************************************/

	/**
	 * Adds a new resource (User) from the given json format (at least username, lastname and
	 * firstname are required at the DB level)
	 * 
	 * @param User
	 * @return
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response createUser(User User) {
		UserDao.createUser(User);
		//@TODO : return with ID in Location header 
		return Response.status(201)
				.entity("A new User/resource has been created").build();
	}

	/**
	 * Adds a new resource (User) from "form" (at least username, lastname and
	 * firstname are required at the DB level)
	 * 
	 * @param username
	 * @param lastname
	 * @param firstname
	 * @return
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response createUserFromForm(@FormParam("username") String username,
			@FormParam("lastname") String lastname,
			@FormParam("firstname") String firstname) {
		User User = new User(username, lastname, firstname);
		UserDao.createUser(User);

		return Response.status(201)
				.entity("A new User/resource has been created").build();
	}

	/**
	 * A list of resources (here Users) provided in json format will be added to
	 * the database.
	 * 
	 * @param Users
	 * @return
	 */
	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public Response createUsers(List<User> Users) {
		for (User User : Users) {
			UserDao.createUser(User);
		}

		return Response.status(204).build();
	}

	/************************************ DELETE ************************************/
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response deleteUserById(@PathParam("id") Long id) {
		if (UserDao.deleteUserById(id) == 1) {
			return Response.status(204).build();
		} else {
			return Response
					.status(404)
					.entity("User with the id " + id
							+ " is not present in the database").build();
		}
	}

	@DELETE
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response deleteUsers() {
		UserDao.deleteUsers();
		return Response.status(200)
				.entity("All Users have been successfully removed").build();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response findById(@PathParam("id") Long id)
			throws JsonGenerationException, JsonMappingException, IOException {

		User UserById = UserDao.getUserById(id);

		if (UserById != null) {
			return Response.status(200).entity(UserById)
					.header("Access-Control-Allow-Headers", "X-extra-header")
					.allow("OPTIONS").build();
		} else {
			return Response.status(404)
					.entity("The User with the id " + id + " does not exist")
					.build();
		}
	}

	/************************************ READ ************************************/
	/**
	 * Returns all resources (Users) from the database
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<User> getUsers() throws JsonGenerationException,
			JsonMappingException, IOException {

		List<User> Users = UserDao.getUsers();

		return Users;
	}

	public void setUserDao(UserDao UserDao) {
		this.UserDao = UserDao;
	}

	/************************************ UPDATE ************************************/
	/**
	 * Updates the attributes of the User received via JSON for the given @param
	 * id
	 * 
	 * If the User does not exist yet in the database (verified by
	 * <strong>id</strong>) then the application will try to create a new User
	 * resource in the db
	 * 
	 * @param id
	 * @param User
	 * @return
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	@Transactional
	public Response updateUserById(User User) {
		String message;
		int status;
		if (User.getId() != null) {
			UserDao.updateUser(User);
			status = 200; // OK
			message = "User has been updated";
		} else if (UserCanBeCreated(User)) {
			UserDao.createUser(User);
			status = 201; // Created
			message = "The User you provided has been added to the database";
		} else {
			status = 406; // Not acceptable
			message = "The information you provided is not sufficient to perform either an UPDATE or "
					+ " an INSERTION of the new User resource <br/>"
					+ " If you want to UPDATE please make sure you provide an existent <strong>id</strong> <br/>"
					+ " If you want to insert a new User please provide at least a <strong>username</strong>, <strong>lastname</strong> and the <strong>firstname</strong> for the User resource";
		}

		return Response.status(status).entity(message).build();
	}

	private boolean UserCanBeCreated(User User) {
		return User.getUsername() != null && User.getFirstName() != null
				&& User.getLastName() != null;
	}

}
