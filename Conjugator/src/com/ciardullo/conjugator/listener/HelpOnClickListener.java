package com.ciardullo.conjugator.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.activity.ConjugatorActivity;
import com.ciardullo.conjugator.layout.ConjugatorEditText;

public class HelpOnClickListener implements OnClickListener, AppConstants {

	private int editTextId;

	public HelpOnClickListener(int n) {
		editTextId = n;
	}

	@Override
	public void onClick(View v) {
		ConjugatorActivity activity = (ConjugatorActivity)v.getContext();
		ConjugatorEditText e = (ConjugatorEditText)activity.findViewById(editTextId);
		e.unhide();
	}
}
