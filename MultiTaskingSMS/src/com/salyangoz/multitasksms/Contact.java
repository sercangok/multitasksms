package com.salyangoz.multitasksms;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	private long contactID;
	private String name;
	private String phoneNumber;
	private int photoID;

	public Contact(long contactID, String name, String phoneNumber, int photoID) {
		super();
		this.contactID = contactID;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.photoID = photoID;
	}

	public long getContactID() {
		return contactID;
	}

	public void setContactID(long contactID) {
		this.contactID = contactID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

}
