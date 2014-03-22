package com.salyangoz.adaptors;

import java.util.List;

import com.salyangoz.classes.conversation;
import com.salyangoz.multitasksms.R;
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

public class ConversationListenerAdaptor extends ArrayAdapter<conversation> {

	private LayoutInflater mInflater;

	public ConversationListenerAdaptor(Context context, int resource,
			List<conversation> objects) {
		super(context, resource, objects);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView;
		satirView = mInflater.inflate(R.layout.conversationxml, null);
		TextView txtDevice, txtPerson;
		txtDevice = (TextView) satirView.findViewById(R.id.txtDevice);
		txtPerson = (TextView) satirView.findViewById(R.id.txtPerson);
		conversation conv = getItem(position);
		if (conv.device != null) {
			txtDevice.setText(conv.device.body);
		} else
			txtPerson.setText(conv.person.body);
		return satirView;
	}

}
