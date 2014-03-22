package com.salyangoz.adaptors;

import java.util.List;

import com.salyangoz.classes.smsContact;
import com.salyangoz.multitasksms.R;
import com.salyangoz.multitasksms.R.drawable;
import com.salyangoz.multitasksms.R.id;
import com.salyangoz.multitasksms.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdaptor extends ArrayAdapter<smsContact> {
	private LayoutInflater mInflater;

	public CustomListAdaptor(Context context, int resource,
			List<smsContact> objects) {
		super(context, resource, objects);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView;
		satirView = mInflater.inflate(R.layout.rowfordistinctsms, null);
		TextView txtContact = (TextView) satirView
				.findViewById(R.id.txtContact);
		TextView txtContent = (TextView) satirView
				.findViewById(R.id.txtContent);
		ImageView imageView = (ImageView) satirView.findViewById(R.id.imgRead);
		smsContact kisi = getItem(position);
		txtContact.setText(kisi.phoneNumber);
		txtContent.setText(kisi.body);

		if (kisi.read.trim().equals("0")) {
			imageView.setImageResource(R.drawable.reset);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		return satirView;
	}

}
