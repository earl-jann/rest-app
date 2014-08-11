package com.cognizant.xbssdg.poc.rest.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cognizant.xbssdg.poc.rest.helper.CustomJsonDateDeserializer;
import com.cognizant.xbssdg.poc.rest.helper.CustomJsonDateSerializer;

@SuppressWarnings("restriction")
@XmlRootElement
@Entity
@Table(name = "users")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6087244364978353035L;
	@Column(name = "first_name")
	private String firstName;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "UserSeq")
	@SequenceGenerator(name="UserSeq", sequenceName = "USERS_SEQ")
	@Column(name = "user_id")
	private Long id;
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "username")
	private String username;
	
	@Column(name = "birthdate")	
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date birthdate;
	
	public User() {
		
	}

	public User(String username, String lastName, String firstName) {
		super();
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
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
