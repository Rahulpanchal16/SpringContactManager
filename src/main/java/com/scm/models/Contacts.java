package com.scm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Contacts")
public class Contacts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contact_id")
	private int conId;
	@Column(name = "contact_name")
	private String conName;
	@Column(name = "contact_nickname")
	private String conNickname;
	@Column(name = "contact_work")
	private String conWork;
	@Column(unique = true, name = "contact_email")
	private String conEmail;
	@Column(name = "contact_phone_no")
	private String conPhone;
	@Column(name = "contact_image_url")
	private String conImgUrl;
	@Column(name = "contact_description", length = 5000)
	private String conDescription;

	@ManyToOne
	@JsonIgnore
	private SUser suser;

	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contacts(int conId, String conName, String conNickname, String conWork, String conEmail, String conPhone,
			String conImgUrl, String conDescription, SUser suser) {
		super();
		this.conId = conId;
		this.conName = conName;
		this.conNickname = conNickname;
		this.conWork = conWork;
		this.conEmail = conEmail;
		this.conPhone = conPhone;
		this.conImgUrl = conImgUrl;
		this.conDescription = conDescription;
		this.suser = suser;
	}

	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public String getConNickname() {
		return conNickname;
	}

	public void setConNickname(String conNickname) {
		this.conNickname = conNickname;
	}

	public String getConWork() {
		return conWork;
	}

	public void setConWork(String conWork) {
		this.conWork = conWork;
	}

	public String getConEmail() {
		return conEmail;
	}

	public void setConEmail(String conEmail) {
		this.conEmail = conEmail;
	}

	public String getConPhone() {
		return conPhone;
	}

	public void setConPhone(String conPhone) {
		this.conPhone = conPhone;
	}

	public String getConImgUrl() {
		return conImgUrl;
	}

	public void setConImgUrl(String conImgUrl) {
		this.conImgUrl = conImgUrl;
	}

	public String getConDescription() {
		return conDescription;
	}

	public void setConDescription(String conDescription) {
		this.conDescription = conDescription;
	}

	public SUser getSuser() {
		return suser;
	}

	public void setSuser(SUser suser) {
		this.suser = suser;
	}

	@Override
	public String toString() {
		return "Contacts [conId=" + conId + ", conName=" + conName + ", conNickname=" + conNickname + ", conWork="
				+ conWork + ", conEmail=" + conEmail + ", conPhone=" + conPhone + ", conImgUrl=" + conImgUrl
				+ ", conDescription=" + conDescription + ", suser=" + suser + "]";
	}

}
