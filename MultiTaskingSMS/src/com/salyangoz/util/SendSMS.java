package com.salyangoz.util;

import android.content.Context;
import android.telephony.SmsManager;

public class SendSMS {

	public SendSMS() {

	}

	public static void SendSMS(Context context, String message,
			String _phoneNumber) {
		String mesaj = message;
		String phoneNumber = _phoneNumber;
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, mesaj, null, null);
		return;
	}
}
