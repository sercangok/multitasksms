package com.salyangoz.classes;

public class Contact {

	private long contactID;
	private String name;
	private String phoneNumber;

	public Contact(long contactID, String name, String phoneNumber) {
		super();
		this.contactID = contactID;
		this.name = name;
		this.phoneNumber = phoneNumber;
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
}
