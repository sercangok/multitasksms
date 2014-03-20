package com.salyangoz.multitasksms;

import java.util.ArrayList;

import android.content.Context;

public class contactSms {
	private String phoneNumber;
	public ArrayList<smsDate> sms;

	public contactSms(String _phoneNumber, String msgBody, boolean isSent,
			String date) {
		phoneNumber = _phoneNumber;
		sms = new ArrayList<smsDate>();
		smsDate item = new smsDate();
		item.date = date;
		item.sms = msgBody;
		item.isSent = isSent;
		sms.add(item);
	}

	public contactSms(String _phoneNumber, ArrayList<smsDate> sms) {
		this.sms = sms;
		phoneNumber = _phoneNumber;
	}
}
