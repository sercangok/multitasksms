package com.salyangoz.classes;

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
	public String thred_id;

	public smsContact(String phoneNumber, String id, String date, String body,
			String seen, String read, String thread_id) {
		super();
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.date = date;
		this.body = body;
		this.seen = seen;
		this.read = read;
		this.thred_id = thread_id;
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
