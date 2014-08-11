package com.cognizant.xbssdg.poc.rest.dao;

import java.util.List;

import com.cognizant.xbssdg.poc.rest.entities.User;

/**
 * 
 * @author etormes
 * 
 */
public interface UserDao {

	public List<User> getUsers();
	/**
	 * Returns a User given its id
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(Long id);

	public Long deleteUserById(Long id);

	public Long createUser(User User);

	public int updateUser(User User);

	/** removes all Users */
	public void deleteUsers();
}
