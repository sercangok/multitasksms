package com.salyangoz.multitasksms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class smsContact {
	public String phoneNumber;
	public String id;
	public String date;
	public String body;
	public String seen;
	public String read;

	public smsContact(String phoneNumber, String id, String date, String body,
			String seen, String read) {
		super();
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.date = date;
		this.body = body;
		this.seen = seen;
		this.read = read;
	}

	public smsContact() {
		super();
	}

	public Date getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			Log.v("MultiTaskSMS", e.getMessage());
			return null;
		}
	}

}
