package com.cognizant.xbssdg.poc.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.xbssdg.poc.rest.AppConstants;
import com.cognizant.xbssdg.poc.rest.dao.UserDao;
import com.cognizant.xbssdg.poc.rest.entities.User;
import com.cognizant.xbssdg.poc.rest.exceptions.AppException;
import com.cognizant.xbssdg.poc.rest.helper.NullAwareBeanUtilsBean;

public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	private UserDao userDao;
		
	/********************* Create related methods implementation ***********************/
	@Transactional
	public Long createUser(User user) throws AppException {
		
		validateInputForCreation(user);
		
		//verify existence of resource in the db (user must be unique)
		User userByUsername = userDao.getUserByUsername(user.getUsername());
		if(userByUsername != null){
			throw new AppException(Response.Status.CONFLICT.getStatusCode(), 409, "User with username already existing in the database with the id " + userByUsername.getId(),
					"Please verify that the first name, last name and username are properly generated", AppConstants.USER_URL);
		}
		
		return userDao.createUser(user);
	}

	private void validateInputForCreation(User user) throws AppException {
		if(user.getUsername() == null){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
					"Please verify that the username is properly generated/set", AppConstants.USER_URL);
		}
		if(user.getLastName() == null){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
					"Please verify that the last name is properly generated/set", AppConstants.USER_URL);
		}
		if(user.getFirstName() == null){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
					"Please verify that the first name is properly generated/set", AppConstants.USER_URL);
		}
		//etc...
	}
	
	@Transactional
	public void createUsers(List<User> users) throws AppException {
		for (User user : users) {
			createUser(user);
		}		
	}	
	
	public List<User> getUsers() throws AppException {
		return userDao.getUsers();
	}
	
	public User getUserById(Long id) throws AppException {		
		User userById = userDao.getUserById(id);
		if(userById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The user you requested with id " + id + " was not found in the database",
					"Verify the existence of the user with the id " + id + " in the database",
					AppConstants.USER_URL);			
		}
		
		return userById;
	}	
	
	/********************* UPDATE-related methods implementation ***********************/	
	@Transactional
	public void updateFullyUser(User user) throws AppException {
		//do a validation to verify FULL update with PUT
		if(isFullUpdate(user)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
					400, 
					"Please specify all properties for Full UPDATE",
					"required properties - id, username, first name, last name" ,
					AppConstants.USER_URL);			
		}
		
		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if(verifyUserExistenceById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.USER_URL);				
		}
				
		userDao.updateUser(user);
	}

	/**
	 * Verifies the "completeness" of user resource sent over the wire
	 * 
	 * @param user
	 * @return
	 */
	private boolean isFullUpdate(User user) {
		return user.getId() == null
				|| user.getUsername() == null
				|| user.getLastName() == null
				|| user.getFirstName() == null;
	}
	
	/********************* DELETE-related methods implementation ***********************/
	@Transactional
	public void deleteUserById(Long id) {
		userDao.deleteUserById(id);
	}
	
	@Transactional	
	public void deleteUsers() {
		userDao.deleteUsers();		
	}

	public User verifyUserExistenceById(Long id) {
		User userById = userDao.getUserById(id);
		if(userById == null){
			return null;
		} else {
			return userById;			
		}
	}

	@Transactional
	public void updatePartiallyUser(User user) throws AppException {
		//do a validation to verify existence of the resource		
		User verifyUserExistenceById = verifyUserExistenceById(user.getId());
		if(verifyUserExistenceById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + user.getId(),
					AppConstants.USER_URL);				
		}
		copyPartialProperties(verifyUserExistenceById, user);		
		userDao.updateUser(verifyUserExistenceById);
		
	}

	private void copyPartialProperties(User verifyUserExistenceById,
						User user) {
		
		BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyUserExistenceById, user);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User getUserByUsername(String username) throws AppException {
		User userByUsername = userDao.getUserByUsername(username);
		if(userByUsername == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The user you requested with username " + username + " was not found in the database",
					"Verify the existence of the user with the username " + username + " in the database",
					AppConstants.USER_URL);			
		}
		
		return userByUsername;
	}
}
