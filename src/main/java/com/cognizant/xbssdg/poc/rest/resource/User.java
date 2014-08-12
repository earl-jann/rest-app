package com.cognizant.xbssdg.poc.rest.resource;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;

import com.cognizant.xbssdg.poc.rest.helper.DateISO8601Adapter;

@SuppressWarnings("restriction")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6087244364978353035L;
	@XmlElement(name = "firstName")	
	private String firstName;	
	@XmlElement(name = "id")	
	private Long id;
	@XmlElement(name = "lastName")	
	private String lastName;
	@XmlElement(name = "username")	
	private String username;
	@XmlElement(name = "birthdate")
	@XmlJavaTypeAdapter(DateISO8601Adapter.class)
	private Date birthdate;

	public User() {

	}

	public User(String username, String lastName, String firstName) {
		super();
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public User(User user) {
		try {
			BeanUtils.copyProperties(this, user);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
