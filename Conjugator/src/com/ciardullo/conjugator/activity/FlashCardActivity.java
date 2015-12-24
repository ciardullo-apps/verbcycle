package com.ciardullo.conjugator.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.R;
import com.ciardullo.conjugator.dao.ConjugationDAO;
import com.ciardullo.conjugator.dao.LoadLookupTableDAO;
import com.ciardullo.conjugator.dao.LoadTensesDAO;
import com.ciardullo.conjugator.dao.LoadVerbsDAO;
import com.ciardullo.conjugator.dao.UtilityDAO;

/**
 * Activity to show verb flash cards
 */
public class FlashCardActivity extends ListActivity implements AppConstants {

	// Keys to required data from the intent
	public static final String VERB_ID = "VERB_ID";
	public static final String TENSE_ID = "TENSE_ID";
	private int verbId;
	private int tenseId;

	private static enum LookupType {
		VERB, TENSE
	};

	public static int TENSE_COUNT = 0;

	// Reference the ArrayAdapter in order to implement swipe gesture
	ArrayAdapter<String> arrayAdapter = null;

	// Needed for gestures
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flashcd);

		// Set the number of tenses
		TENSE_COUNT = UtilityDAO.getTenseCount(this);
		
		// Get verb id and tense id from intent
		verbId = getIntent().getIntExtra(VERB_ID, 1);
		tenseId = getIntent().getIntExtra(TENSE_ID, 1);

		arrayAdapter = new ArrayAdapter<String>(this, R.layout.flashlist,
				R.id.list_content, populateConjugationList());
		setListAdapter(arrayAdapter);

		// Get the verb name
		TextView verbName = (TextView) findViewById(R.id.verbName);
		verbName.setText(getLookupName(LookupType.VERB, verbId));
		TextView tenseName = (TextView) findViewById(R.id.tenseName);
		tenseName.setText(getLookupName(LookupType.TENSE, tenseId));

		// Gesture detection
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		// Do this for each view
		View v = (View) findViewById(R.id.flashLayout);
		v.setOnTouchListener(gestureListener);
//		ListView lv = (ListView)findViewById(android.R.id.list);
//		lv.setOnTouchListener(gestureListener);
	}

	/**
	 * Populates the layout with data from the db
	 * 
	 * @return
	 */
	private List<String> populateConjugationList() {
		String[] pronouns = new String[] { getString(R.string.io),
				getString(R.string.tu), getString(R.string.lei),
				getString(R.string.noi), getString(R.string.voi),
				getString(R.string.loro) };
		List<String> values = new ArrayList<String>();

		// Bind list view to Cursor
		ConjugationDAO dao = null;
		Cursor cursor = null;

		try {
			dao = new ConjugationDAO(this);
			dao.open();
			dao.setVerbId(verbId);
			dao.setTenseId(tenseId);
			cursor = dao.getAllRows();
			cursor.moveToFirst();
			do {
				// values.add(pronouns[n] + " " + cursor.getString(0));
				values.add(cursor.getString(0));
			} while (cursor.moveToNext());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cursor != null)
					cursor.close();
			} catch (Exception e) {
			}
			try {
				if (dao != null)
					dao.close();
			} catch (Exception e) {
			}
		}

		return values;
	}

	/**
	 * Gets the verb name from the database for a given _id
	 * 
	 * @param verbId
	 * @return
	 */
	private String getLookupName(LookupType aType, int theId) {
		LoadLookupTableDAO dao = null;
		Cursor cursor = null;
		String s = null;
		try {
			if (aType == LookupType.VERB) {
				dao = new LoadVerbsDAO(this);
				dao.setSelection("v." + WHERE_BY_ID);
			} else if (aType == LookupType.TENSE) {
				dao = new LoadTensesDAO(this);
				dao.setSelection(WHERE_BY_ID_PARAM);
			}
			dao.setSelectionArgs(new String[] { String.valueOf(theId) });
			cursor = dao.getAllRows();
			cursor.moveToFirst();
			s = cursor.getString(0);
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception e) {
				}
			}
			if (dao != null) {
				try {
					dao.close();
				} catch (Exception e) {
				}
			}
		}

		return s;
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
//			See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
//			final ViewConfiguration vc = ViewConfiguration.get(FlashCardActivity.this);
//			final int SWIPE_MIN_DISTANCE = vc.getScaledPagingTouchSlop();
//			final int SWIPE_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();
//			final int SWIPE_MAX_OFF_PATH = vc.getScaledTouchSlop();

			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Left swipe, increment tense id
					nextFlashCard(null);
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Right swipe, decrement tense id
					prevFlashCard(null);
				}
			} catch (Exception e) {}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		View v = (View) findViewById(R.id.flashLayout);
		v.setOnTouchListener(null);
		
		gestureListener = null;
		gestureDetector = null;
		arrayAdapter = null;
		setListAdapter(null);
		super.onDestroy();
	}

	/**
	 * Shows the next flash card. Used by onFling and flashcd.xml
	 */
	public void nextFlashCard(View v) {
		if(tenseId < TENSE_COUNT) {
			tenseId++;
			arrayAdapter.clear();
			// addAll() yields NoSuchMethodError on Gingerbread
//			arrayAdapter.addAll(populateConjugationList());
			for(String s : populateConjugationList()) {
				arrayAdapter.add(s);
			}
			TextView tenseName = (TextView) findViewById(R.id.tenseName);
			tenseName.setText(getLookupName(LookupType.TENSE, tenseId));
		}
	}
	
	/**
	 * Shows the prev flash card. Used by onFling and flashcd.xml
	 */
	public void prevFlashCard(View v) {
		if(tenseId > 1) {
			tenseId--;
			arrayAdapter.clear();
			// addAll() yields NoSuchMethodError on Gingerbread
//			arrayAdapter.addAll(populateConjugationList());
			for(String s : populateConjugationList()) {
				arrayAdapter.add(s);
			}
			TextView tenseName = (TextView) findViewById(R.id.tenseName);
			tenseName.setText(getLookupName(LookupType.TENSE, tenseId));
		}
	}
}
