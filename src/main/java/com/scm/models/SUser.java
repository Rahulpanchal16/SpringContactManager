package com.scm.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class SUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@NotBlank(message = "Name field cannot be blank")
	@Size(min = 2, max = 30, message = "name must be between 2 and 30 characters long")
	@Column(name = "user_name")
	private String name;

	@NotBlank(message = "Email cannot be blank")
	@Column(unique = true, name = "user_email")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, message = "min 6 characters allowed")
	@Column(name = "user_password")
	private String password;

	@Column(name = "user_about", length = 5000)
	private String about;

	@Column(name = "user_isenabled")
	private boolean isEnabled;
	@Column(name = "user_role")
	private String role;

	@Column(name = "user_image_url")
	private String imgUrl;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "suser", orphanRemoval = true)
	private List<Contacts> contacts = new ArrayList<>();

	public SUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SUser(int id, String name, String email, String password, String about, boolean isEnabled, String role,
			String imgUrl, List<Contacts> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.about = about;
		this.isEnabled = isEnabled;
		this.role = role;
		this.imgUrl = imgUrl;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "SUser [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", about=" + about
				+ ", isEnabled=" + isEnabled + ", role=" + role + ", imgUrl=" + imgUrl + ", contacts=" + contacts + "]";
	}

}
