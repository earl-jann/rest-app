package com.cognizant.xbssdg.poc.rest.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.cognizant.xbssdg.poc.rest.dao.UserDao;
import com.cognizant.xbssdg.poc.rest.entities.User;

public class UserDaoJPA2Impl implements UserDao {

	@PersistenceContext(unitName="pocRestPersistence")
	private EntityManager entityManager;

	public List<User> getUsers() {
		
		String qlString = "SELECT p FROM User p";
		TypedQuery<User> query = entityManager.createQuery(qlString, User.class);		

		return query.getResultList();
	}

	public User getUserById(Long id) {
		
		try {
			String qlString = "SELECT p FROM User p WHERE p.id = ?1";
			TypedQuery<User> query = entityManager.createQuery(qlString, User.class);		
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Long deleteUserById(Long id) {
		
		User User = entityManager.find(User.class, id);
		entityManager.remove(User);
		
		return 1L;
	}

	public Long createUser(User User) {
		
		entityManager.persist(User);
		entityManager.flush();//force insert to receive the id of the User
		
		return User.getId();
	}

	public int updateUser(User User) {
		
		entityManager.merge(User);
		
		return 1; 
	}

	public void deleteUsers() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE Users");		
		query.executeUpdate();
	}

}

