package com.salyangoz.classes;


public class conversation {
	public smsContact device;
	public smsContact person;

	public conversation() {
	}

	public conversation(smsContact device, smsContact person) {
		this.device = device;
		this.person = person;
	}
}
