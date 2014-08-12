package com.cognizant.xbssdg.poc.rest.service;

import java.util.List;

import com.cognizant.xbssdg.poc.rest.entities.User;
import com.cognizant.xbssdg.poc.rest.exceptions.AppException;

public interface UserManagementService {

	/*
	 * ******************** Create related methods **********************
	 */
	public Long createUser(User user) throws AppException;

	public void createUsers(List<User> users) throws AppException;

	/*
	 * ******************* Read related methods ********************
	 */
	public List<User> getUsers() throws AppException;
	/**
	 * Returns a user given its id
	 * 
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public User getUserById(Long id) throws AppException;
	public User getUserByUsername(String username) throws AppException;

	/*
	 * ******************** Update related methods **********************
	 */
	public void updateFullyUser(User user) throws AppException;

	public void updatePartiallyUser(User user) throws AppException;

	/*
	 * ******************** Delete related methods **********************
	 */
	public void deleteUserById(Long id);

	/** removes all users */
	public void deleteUsers();

	/*
	 * ******************** Helper methods **********************
	 */
	public User verifyUserExistenceById(Long id);
}