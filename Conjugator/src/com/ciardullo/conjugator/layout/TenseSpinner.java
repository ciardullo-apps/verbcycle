package com.ciardullo.conjugator.layout;

import android.content.Context;
import android.util.AttributeSet;

import com.ciardullo.conjugator.dao.LoadLookupTableDAO;
import com.ciardullo.conjugator.dao.LoadTensesDAO;
import com.ciardullo.conjugator.listener.ConjugationOnItemSelectedListener;

public class TenseSpinner extends ConjugatorSpinner {
	
	public TenseSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		spinnerType = SpinnerType.TENSE;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(SpinnerType.TENSE));
	}

	public TenseSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		spinnerType = SpinnerType.TENSE;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(SpinnerType.TENSE));
	}

	public TenseSpinner(Context context) {
		super(context);
		spinnerType = SpinnerType.TENSE;
		setOnItemSelectedListener(new ConjugationOnItemSelectedListener(SpinnerType.TENSE));
	}

	@Override
	protected LoadLookupTableDAO getLookupDAO(Context context) {
		// Return an initialized LoadLookupTableDAO
		LoadLookupTableDAO dao = new LoadTensesDAO(context);
		return dao;
	}
}
