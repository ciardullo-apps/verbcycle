package com.ciardullo.conjugator.listener;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ciardullo.conjugator.AppConstants.SpinnerType;
import com.ciardullo.conjugator.R;
import com.ciardullo.conjugator.activity.ConjugatorActivity;
import com.ciardullo.conjugator.dao.ConjugationDAO;
import com.ciardullo.conjugator.layout.ConjugatorEditText;
import com.ciardullo.conjugator.layout.ConjugatorSpinner;
import com.ciardullo.conjugator.layout.TenseSpinner;
import com.ciardullo.conjugator.layout.VerbSpinner;

/**
 * Used by the Verb and Tense Spinners to update the list of conjugations
 */
public class ConjugationOnItemSelectedListener 
		implements OnItemSelectedListener {

	public static int[] editTextIds = new int[] {
		R.id.editIo,
		R.id.editTu,
		R.id.editLei,
		R.id.editNoi,
		R.id.editVoi,
		R.id.editLoro
	};

	public static int[] passFailImgIds = new int[] {
		R.id.passFailIo,
		R.id.passFailTu,
		R.id.passFailLei,
		R.id.passFailNoi,
		R.id.passFailVoi,
		R.id.passFailLoro
	};

	public ConjugationOnItemSelectedListener(SpinnerType spinnerType) {
		super();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// Get the selected position and which drop down was clicked
		SQLiteCursor cur = (SQLiteCursor)parent.getItemAtPosition(pos);

		// Get the context (aka, the Activity)
		ConjugatorActivity activity = (ConjugatorActivity)parent.getContext();
		int verbId = 0;
		int tenseId = 0;

		// Determine which Spinner changed
		ConjugatorSpinner spinner = (ConjugatorSpinner)parent;
		if(spinner.isVerbSpinner()) {
			verbId = cur.getInt(1);
			
			// Get the already-selected tenseID
			TenseSpinner v = (TenseSpinner)activity.findViewById(R.id.listTense);
			SQLiteCursor o = (SQLiteCursor)v.getSelectedItem();
			if(o != null)
				tenseId = o.getInt(1);
		} else if(spinner.isTenseSpinner()) {
			tenseId = cur.getInt(1);

			// Get the already-selected verbID
			VerbSpinner v = (VerbSpinner)activity.findViewById(R.id.listVerb);
			SQLiteCursor o = (SQLiteCursor)v.getSelectedItem();
			if(o != null)
				verbId = o.getInt(1);
		}
		
		if(verbId <= 0)
			verbId = 1;
		if(tenseId <=0)
			tenseId = 1;

		// Retrieve conjugations from the DB
		ConjugationDAO dao = new ConjugationDAO(parent.getContext());
		try {
			dao.open();
			dao.setVerbId(verbId);
			dao.setTenseId(tenseId);
			Cursor cursor = dao.getAllRows();
			
			// Populate the EditText widgets
			cursor.moveToFirst();
			int n = 0;
			do {
				ConjugatorEditText et = (ConjugatorEditText)activity.findViewById(editTextIds[n]);
				et.setConjugated(cursor.getString(0));
				et.setEditTextId(editTextIds[n]);
				et.setPassFailImgId(passFailImgIds[n]);
				et.reset();
//				et.unhide();
				// Always set the focus to io when a Spinner changes
				if(n == 0)
					et.requestFocus();
				n++;
			} while(cursor.moveToNext() && n < 6);
		} catch(Exception e) {
//			Log.e(ConjugationOnItemSelectedListener.class.getName(), e.getMessage());
		} finally {
			try {
				if(dao != null)
					dao.close();
			} catch(Exception e) {}
		}

	}

	public void onNothingSelected(AdapterView parent) {
		// Do nothing.
	}
}