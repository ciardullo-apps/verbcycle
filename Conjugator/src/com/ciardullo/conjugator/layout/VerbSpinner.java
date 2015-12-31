package com.ciardullo.conjugator.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.ciardullo.conjugator.dao.LoadLookupTableDAO;
import com.ciardullo.conjugator.dao.LoadVerbsDAO;
import com.ciardullo.conjugator.listener.ConjugationOnItemSelectedListener;

public class VerbSpinner extends ConjugatorSpinner {

	public VerbSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		spinnerType = SpinnerType.VERB;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(
				SpinnerType.VERB));
	}

	public VerbSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		spinnerType = SpinnerType.VERB;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(
				SpinnerType.VERB));
	}

	public VerbSpinner(Context context) {
		super(context);
		spinnerType = SpinnerType.VERB;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(
				SpinnerType.VERB));
	}

	@Override
	protected LoadLookupTableDAO getLookupDAO(Context context) {
		// Return an initialized LoadLookupTableDAO
		LoadLookupTableDAO dao = new LoadVerbsDAO(context);

		// Get verb filter preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String filterVerbsBy = prefs.getString(PREF_FILTER_BY,
				PREF_FILTER_BY_NO_FILTER);
		if (PREF_FILTER_BY_NO_FILTER.equals(filterVerbsBy)) {
			dao.setSelection(null);
			dao.setSelectionArgs(null);
		} else if (PREF_FILTER_BY_FORM_ARE.equals(filterVerbsBy)
				|| PREF_FILTER_BY_FORM_ERE.equals(filterVerbsBy)
				|| PREF_FILTER_BY_FORM_IRE.equals(filterVerbsBy)
				|| PREF_FILTER_BY_FORM_OTHER.equals(filterVerbsBy)) {
			dao.setSelection(PREF_FILTER_BY_FORM);
			dao.setSelectionArgs(new String[] { filterVerbsBy });
		}

		return dao;
	}
}
