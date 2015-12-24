package com.ciardullo.conjugator.layout;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.AttributeSet;
import android.widget.Spinner;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.dao.LoadLookupTableDAO;

public abstract class ConjugatorSpinner extends Spinner 
		implements AppConstants {
	protected LoadLookupTableDAO lookupDAO;
	protected SpinnerType spinnerType;
	protected String filter;
	protected abstract LoadLookupTableDAO getLookupDAO(Context context);

	public ConjugatorSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ConjugatorSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ConjugatorSpinner(Context context) {
		super(context);
	}

	/**
	 * Populates this Spinner using its DAO
	 * @param lookupDAO
	 * @param spinner
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public Cursor loadLookups(Context context) throws SQLException, IOException {
		lookupDAO = getLookupDAO(context);
		lookupDAO.open();
		Cursor cursor = lookupDAO.getAllRows();

		return cursor;
	}
	
	/**
	 * Poor design because SimpleCursorAdapter requires database info in the view
	 * TODO Convert to use android.app.LoaderManager in API 11
	 * @return
	 */
	public String[] getAllColumns() {
		return lookupDAO.getAllColumns();
	}
	
	public void closeLookupDAO() {
		try {
			lookupDAO.close();
		} catch (Exception e) {}
	}
	
	public boolean isVerbSpinner() {
		return spinnerType == SpinnerType.VERB;
	}
	
	public boolean isTenseSpinner() {
		return spinnerType == SpinnerType.TENSE;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}