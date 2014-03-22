package com.salyangoz.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.salyangoz.adaptors.ConversationListenerAdaptor;
import com.salyangoz.adaptors.CustomListAdaptor;
import com.salyangoz.classes.Contact;
import com.salyangoz.classes.ContactManager;
import com.salyangoz.classes.contactSms;
import com.salyangoz.classes.conversation;
import com.salyangoz.classes.smsContact;
import com.salyangoz.classes.smsDate;
import com.salyangoz.multitasksms.R;
import com.salyangoz.multitasksms.R.anim;
import com.salyangoz.multitasksms.R.drawable;
import com.salyangoz.multitasksms.R.id;
import com.salyangoz.multitasksms.R.layout;
import com.salyangoz.util.SendSMS;

import android.R.string;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MultiTaskSMSService extends Service implements OnClickListener,
		OnItemClickListener {
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	private WindowManager.LayoutParams paramsSMSList;
	private ImageView chatHead;
	private EditText txtPhoneNumber, txtContent, txtContactsName;
	public ListView lst, lstContacts;
	public static List<smsContact> distinctContents;
	public static List<conversation> conversations;
	public static ArrayList<String> phoneNumbers = new ArrayList<String>();
	public static ArrayList<contactSms> allSms = new ArrayList<contactSms>();
	public static ArrayList<smsDate> smsDates = new ArrayList<smsDate>();
	private CustomListAdaptor veriAdaptoru;
	private ConversationListenerAdaptor conversationAdaptoru;
	private ProgressBar prgLoading;
	private Boolean isOpen = false;
	private Animation animRight;
	private View smslist, contactList;
	ArrayAdapter<String> contactsAdaptor;
	Button btnNewMessage;
	Uri uri = Uri.parse("content://sms/");

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT);
			params.gravity = Gravity.TOP | Gravity.RIGHT;
			params.x = 0;
			params.y = 100;
			chatHead = new ImageView(this);
			chatHead.setImageResource(R.drawable.ic_launcher);

			paramsSMSList = new WindowManager.LayoutParams(wm
					.getDefaultDisplay().getWidth() / 2, wm.getDefaultDisplay()
					.getHeight() / 2,
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT);
			paramsSMSList.gravity = Gravity.TOP | Gravity.RIGHT;
			paramsSMSList.x = 0;
			paramsSMSList.y = 180;

			chatHead.setOnClickListener(this);
			wm.addView(chatHead, params);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG)
					.show();
		}
		animRight = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_right_animation);
		smslist = LayoutInflater.from(this).inflate(R.layout.smslist, null);
		btnNewMessage = (Button) smslist.findViewById(R.id.btnNewSMS);
		contactList = LayoutInflater.from(this)
				.inflate(R.layout.contacts, null);
		txtContactsName = (EditText) contactList.findViewById(R.id.txtContact);
		lstContacts = (ListView) contactList.findViewById(R.id.lstContacts);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onClick(View v) {
		if (isOpen) {
			smslist.startAnimation(animRight);
			wm.removeView(smslist);
			isOpen = false;
			return;

		} else {
			wm.addView(smslist, paramsSMSList);
			isOpen = true;
		}

		prgLoading = (ProgressBar) smslist.findViewById(R.id.prgLoading);
		btnNewMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				paramsSMSList.flags = WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;
				final View newsms = LayoutInflater
						.from(getApplicationContext()).inflate(R.layout.newsms,
								null);
				txtPhoneNumber = (EditText) newsms
						.findViewById(R.id.edtTelephoneNumber);
				txtContent = (EditText) newsms.findViewById(R.id.edtContent);
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				txtPhoneNumber.requestFocus();
				txtPhoneNumber.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN
								&& keyCode == KeyEvent.KEYCODE_ENTER) {
							txtPhoneNumber.clearFocus();
							txtContent.requestFocus();
							return true;
						}
						return false;
					}
				});
				txtContent.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						paramsSMSList.flags = WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;
						wm.updateViewLayout(newsms, paramsSMSList);
						txtPhoneNumber.clearFocus();
						txtContent.requestFocus();

					}
				});
				txtPhoneNumber.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						paramsSMSList.flags = WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;
						wm.updateViewLayout(newsms, paramsSMSList);
						txtContent.clearFocus();
						txtPhoneNumber.requestFocus();
					}
				});
				wm.addView(newsms, paramsSMSList);
				Button btn = (Button) newsms.findViewById(R.id.btnSend);
				Button btnContacts = (Button) newsms
						.findViewById(R.id.btnContacts);
				btnContacts.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						wm.addView(contactList, paramsSMSList);
						txtPhoneNumber.clearFocus();
						txtContent.clearFocus();
						AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
							@Override
							protected void onPostExecute(Void result) {
								lstContacts.setAdapter(contactsAdaptor);
								super.onPostExecute(result);
							}

							@Override
							protected void onPreExecute() {
								super.onPreExecute();
							}

							@Override
							protected Void doInBackground(Void... params) {
								contactsAdaptor = new ArrayAdapter<String>(
										getApplicationContext(),
										android.R.layout.simple_list_item_1,
										getContacts());
								return null;
							}
						};
						asyncTask.execute();
					}

					private List<String> getContacts() {
						List<String> allConntacList = new ArrayList<String>();
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null, null, null, null);
						while (phones.moveToNext()) {
							String name = phones.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
							String phoneNumber = phones.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							allConntacList.add(name);
						}
						phones.close();
						return allConntacList;
					}
				});
				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							SendSMS.SendSMS(getApplicationContext(), txtContent
									.getText().toString(), txtPhoneNumber
									.getText().toString());
						} catch (Exception e) {
						}
						paramsSMSList.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
						wm.removeView(newsms);
						txtContent.clearFocus();
						txtPhoneNumber.clearFocus();
					}
				});
			}
		});
		lst = (ListView) smslist.findViewById(R.id.listView1);
		lst.setOnItemClickListener(this);
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPostExecute(Void result) {
				lst.setAdapter(veriAdaptoru);
				prgLoading.setVisibility(View.INVISIBLE);
				super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {
				prgLoading.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				veriAdaptoru = new CustomListAdaptor(getApplicationContext(),
						R.layout.rowfordistinctsms, getDistinctNumbers());
				return null;
			}
		};
		asyncTask.execute();
	}

	private List<conversation> getCategoryMessage(String threadID) {
		Cursor c = getContentResolver().query(uri, null, "thread_id=?",
				new String[] { threadID }, "date ASC");
		conversations = new ArrayList<conversation>();
		if (c.moveToFirst()) {
			String id;
			String date;
			String phoneNumber;
			String body;
			String seen;
			int idColumn = c.getColumnIndex("_id");
			int dateColumn = c.getColumnIndex("date");
			int numberColumn = c.getColumnIndex("address");
			int bodyColumn = c.getColumnIndex("body");
			int seenColumn = c.getColumnIndex("seen");
			do {
				id = c.getString(idColumn);
				date = c.getString(dateColumn);
				body = c.getString(bodyColumn);
				phoneNumber = c.getString(numberColumn);
				seen = c.getString(seenColumn);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"HH:mm:ss dd-MM-yyyy");
				smsContact person = null, device = null;
				if (seen.equals("1")) {
					person = new smsContact(phoneNumber, id, date, body, seen,
							null, null);
				} else {
					device = new smsContact(phoneNumber, id, date, body, seen,
							null, null);
				}
				conversation con = new conversation(device, person);
				conversations.add(con);
				// String FormattedDate = sdf.format(date).toString();
			} while (c.moveToNext());
		}
		return conversations;
	}

	private List<smsContact> getDistinctNumbers() {
		ContactManager cm = new ContactManager(this);
		Contact contact;
		Cursor c = getContentResolver().query(
				uri,
				new String[] { "DISTINCT thread_id", "body", "date", "_id",
						"address", "seen", "read", "thread_id" },
				"address IS NOT NULL) GROUP BY (thread_id", null, "date DESC");
		distinctContents = new ArrayList<smsContact>();
		if (c.moveToFirst()) {
			String id;
			String date;
			String phoneNumber;
			String body;
			String seen;
			String read;
			String thread_id;
			int idColumn = c.getColumnIndex("_id");
			int dateColumn = c.getColumnIndex("date");
			int numberColumn = c.getColumnIndex("address");
			int bodyColumn = c.getColumnIndex("body");
			int seenColumn = c.getColumnIndex("seen");
			int readColumn = c.getColumnIndex("read");
			int threadColumn = c.getColumnIndex("thread_id");
			do {
				id = c.getString(idColumn);
				date = c.getString(dateColumn);
				body = c.getString(bodyColumn);
				phoneNumber = c.getString(numberColumn);
				seen = c.getString(seenColumn);
				read = c.getString(readColumn);
				thread_id = c.getString(threadColumn);
				smsContact s = new smsContact(phoneNumber, id, date, body,
						seen, read, thread_id);
				contact = cm.getContactInfo(phoneNumber);
				if (contact.getName() == null)
					s.phoneNumber = phoneNumber;
				else
					s.phoneNumber = contact.getName();
				if (s.seen.endsWith("1"))
					distinctContents.add(s);
			} while (c.moveToNext());
		}
		return distinctContents;
	}

	private void getMessagesSent(String number) {
		Uri uri = Uri.parse("content://sms/sent");
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		if (c.moveToFirst()) {
			String id;
			String date;
			String phoneNumber;
			String body;

			int idColumn = c.getColumnIndex("_id");
			int dateColumn = c.getColumnIndex("date");
			int numberColumn = c.getColumnIndex("address");
			int bodyColumn = c.getColumnIndex("body");
			do {
				id = c.getString(idColumn);
				date = c.getString(dateColumn);
				body = c.getString(bodyColumn);
				phoneNumber = c.getString(numberColumn);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MM-yyyy HH:mm:ss");
				String FormattedDate = sdf.format(date).toString();
				if (phoneNumber == number) {
					smsDate sms = new smsDate();
					sms.date = date;
					sms.sms = body;
					sms.isSent = true;
					smsDates.add(sms);
				}
			} while (c.moveToNext());
		}
	}

	private void getMessagesInbox(Cursor c) {
		if (c.moveToFirst()) {
			String id;
			String date;
			String phoneNumber;
			String body;

			int idColumn = c.getColumnIndex("_id");
			int dateColumn = c.getColumnIndex("date");
			int numberColumn = c.getColumnIndex("address");
			int bodyColumn = c.getColumnIndex("body");
			for (String number : phoneNumbers) {
				do {
					id = c.getString(idColumn);
					date = c.getString(dateColumn);
					body = c.getString(bodyColumn);
					phoneNumber = c.getString(numberColumn);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"HH:mm:ss dd-MM-yyyy");
					String FormattedDate = sdf.format(date).toString();
					if (phoneNumber == number) {
						smsDate sms = new smsDate();
						sms.date = date;
						sms.sms = body;
						sms.isSent = false;
						smsDates.add(sms);
					}
				} while (c.moveToNext());
				getMessagesSent(phoneNumber);
				contactSms con = new contactSms(phoneNumber, smsDates);
				smsDates.removeAll(smsDates);
				c.moveToFirst();
			}
		}
	}

	@Override
	public void onDestroy() {
		wm.removeView(chatHead);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		try {
			final smsContact clickedObject = (smsContact) lst.getAdapter()
					.getItem(position);
			AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected void onPostExecute(Void result) {
					lst.setAdapter(conversationAdaptoru);
					prgLoading.setVisibility(View.INVISIBLE);
					super.onPostExecute(result);
				}

				@Override
				protected void onPreExecute() {
					prgLoading.setVisibility(View.VISIBLE);
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(Void... params) {
					conversationAdaptoru = new ConversationListenerAdaptor(
							getApplicationContext(), R.layout.conversationxml,
							getCategoryMessage(clickedObject.thred_id));
					return null;
				}
			};
			asyncTask.execute();

		} catch (Exception e) {
		}
	}
}
