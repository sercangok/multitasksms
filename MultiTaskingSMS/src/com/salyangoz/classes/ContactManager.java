package com.salyangoz.classes;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class ContactManager {
	private Context context;

	public ContactManager(Context context) {
		this.context = context;
	}

	public Contact getContactInfo(String phoneNumber) {
		String name = null;
		int photoID = 0, idx;
		long contactID = 0;
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));

		Cursor contactCursor = context.getContentResolver().query(lookupUri,
				null, null, null, null);

		if (contactCursor.moveToFirst()) {
			idx = contactCursor.getColumnIndex(ContactsContract.Contacts._ID);
			contactID = contactCursor.getLong(idx);
			idx = contactCursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			name = contactCursor.getString(idx);
			idx = contactCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);

			photoID = contactCursor.getInt(idx);
			// profilePhoto = getPhoto(context.getContentResolver(), contactID);
		}
		Contact contact = new Contact(contactID, name, phoneNumber);
		return contact;
	}

	public static Bitmap getPhoto(ContentResolver contentResolver,
			Long contactId) {
		Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		InputStream photoDataStream = Contacts.openContactPhotoInputStream(
				contentResolver, contactPhotoUri); // <--
		Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
		return photo;
	}

}
