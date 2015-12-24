package com.ciardullo.conjugator.activity;

import java.util.Deque;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.R;
import com.ciardullo.conjugator.dao.BillingDAO;
import com.ciardullo.conjugator.layout.ConjugatorSpinner;
import com.ciardullo.conjugator.layout.TenseSpinner;
import com.ciardullo.conjugator.layout.VerbSpinner;
import com.ciardullo.conjugator.listener.ConjugationOnItemSelectedListener;
import com.ciardullo.conjugator.listener.HelpOnClickListener;
import com.ciardullo.util.LayoutUtil;

public class ConjugatorActivity extends Activity implements AppConstants {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Populate the Spinners
		VerbSpinner listVerb = (VerbSpinner) findViewById(R.id.listVerb);
		populateSpinner(listVerb);
		listVerb.setOnItemSelectedListener(
				new ConjugationOnItemSelectedListener(SpinnerType.VERB));

		TenseSpinner listTense = (TenseSpinner) findViewById(R.id.listTense);
		populateSpinner(listTense);
		listTense.setOnItemSelectedListener(
				new ConjugationOnItemSelectedListener(SpinnerType.TENSE));

		// Set listeners to show the answer
		ImageView iv = (ImageView) findViewById(R.id.passFailIo);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editIo));

		iv = (ImageView) findViewById(R.id.passFailTu);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editTu));

		iv = (ImageView) findViewById(R.id.passFailLei);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editLei));

		iv = (ImageView) findViewById(R.id.passFailNoi);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editNoi));

		iv = (ImageView) findViewById(R.id.passFailVoi);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editVoi));

		iv = (ImageView) findViewById(R.id.passFailLoro);
		iv.setOnClickListener(new HelpOnClickListener(R.id.editLoro));

		// Set the lock image based on the pref
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String cycleBy = prefs.getString(PREF_CYCLE_BY,
				PREF_CYCLE_BY_TENSE);
		if(PREF_CYCLE_BY_TENSE.equals(cycleBy)) {
			// Verb is locked
			findViewById(R.id.cycleVerb).setVisibility(View.INVISIBLE);
			findViewById(R.id.cycleTense).setVisibility(View.VISIBLE);
		} else if(PREF_CYCLE_BY_VERB.equals(cycleBy)) {
			// Tense is locked
			findViewById(R.id.cycleVerb).setVisibility(View.VISIBLE);
			findViewById(R.id.cycleTense).setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * Build spinner of verbs and tenses
	 */
	private void populateSpinner(ConjugatorSpinner spinner) {
		Cursor cursor = null;

//		String[] items = new String[] { "One", "Two", "Three" };
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//		android.R.layout.simple_spinner_item, tenses.toArray());

		try {
			cursor = spinner.loadLookups(this);
			int[] to = new int[]{android.R.id.text1};
	
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this,
					android.R.layout.simple_spinner_item,
					cursor,
					spinner.getAllColumns(),
					to);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} catch (Exception e) {
//			Log.e(ConjugatorActivity.class.getName(), e.getMessage());
		} finally {
			spinner.closeLookupDAO();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.setQwertyMode(true);
		MenuItem item = menu.add(0,MENU_ITEM_SETTINGS,0,R.string.settings);
		item.setIcon(R.drawable.ic_menu_settings_holo_light);
		item = menu.add(0,MENU_ITEM_UPGRADE,0,R.string.upgrade);
		item.setIcon(R.drawable.ic_menu_compass);
		item = menu.add(0,MENU_ITEM_FLASH,0,R.string.flash);
		item.setIcon(R.drawable.ic_menu_find);
		item = menu.add(0,MENU_ITEM_ABOUT,0,R.string.about);
		item.setIcon(R.drawable.ic_menu_help);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean b = false;
		switch(item.getItemId()) {
		case MENU_ITEM_SETTINGS:
			startActivityForResult(new Intent(this, SettingsActivity.class), 0);
			b = true;
			break;
		case MENU_ITEM_ABOUT:
			// Create the about box dialog
			showDialog(DIALOG_ABOUT);
			b = true;
			break;
		case MENU_ITEM_UPGRADE:
			// Start the Billing activity
			startActivityForResult(new Intent(this, BillingActivity.class), 0);
			b = true;
			break;
		case MENU_ITEM_FLASH:
			VerbSpinner listVerb = (VerbSpinner) findViewById(R.id.listVerb);
			int verbId = (int)listVerb.getSelectedItemId();
			TenseSpinner listTense = (TenseSpinner) findViewById(R.id.listTense);
			int tenseId = (int)listTense.getSelectedItemId();

			Intent flashIntent = new Intent(this, FlashCardActivity.class);
			flashIntent.putExtra(FlashCardActivity.VERB_ID, verbId);
			flashIntent.putExtra(FlashCardActivity.TENSE_ID, tenseId);
			startActivity(flashIntent);
			b = true;
			break;
/*
		case MENU_ITEM_BACKUP:
		    String state = Environment.getExternalStorageState();
	    	File externalStorageDir = Environment.getExternalStorageDirectory();
		    if (Environment.MEDIA_MOUNTED.equals(state)) {
		    	// Make sure a backup directory exists in external storage
				String rootPath = externalStorageDir.getAbsolutePath();

				File backupDir = new File(rootPath + "/ciardullo/backup");
		    	String backupFileAbsolutePath = backupDir + "/" + DBConstants.DATABASE_NAME;
				if(!backupDir.exists()) {
					if(!backupDir.mkdirs()) {
					}
				}

				// External storage state is available and writeable
		    	File dbToBackup = new File(DBConstants.DATABASE_PATH + DBConstants.DATABASE_NAME);
		    	try {
					InputStream myInput = new FileInputStream(dbToBackup);
					
					OutputStream myOutput = null;
					try {
						myOutput = new FileOutputStream(backupFileAbsolutePath);

						// transfer bytes from the inputfile to the outputfile
						byte[] buffer = new byte[1024];
						int length;
						while ((length = myInput.read(buffer)) > 0) {
							myOutput.write(buffer, 0, length);
						}
					} catch(IOException e) {
						throw e;
					} finally {
						// Close the streams
						if(myOutput != null) {
							myOutput.flush();
							myOutput.close();
						}
						if(myInput != null) {
							myInput.close();
						}
					}

		    	} catch(IOException e) {
		    	}
		    } else {
		        // Can't backup
		    }
*/
		}
		return b;
	}

	@Override
	/**
	 * Called after startActivityForResult() from onOptionsItemSelected()
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Populate the verb spinner only if the filter changed
		// or if a Verb Pack purchase was made 
		if(resultCode == RESULT_OK_POPULATE_VERB_SPINNER) {
			VerbSpinner listVerb = (VerbSpinner) findViewById(R.id.listVerb);
			populateSpinner(listVerb);
		}

		if(resultCode == RESULT_OK) {
			// Set the lock image based on the pref
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String cycleBy = prefs.getString(PREF_CYCLE_BY,
					PREF_CYCLE_BY_TENSE);
			if(PREF_CYCLE_BY_TENSE.equals(cycleBy)) {
				// Verb is locked
				findViewById(R.id.cycleVerb).setVisibility(View.INVISIBLE);
				findViewById(R.id.cycleTense).setVisibility(View.VISIBLE);
			} else if(PREF_CYCLE_BY_VERB.equals(cycleBy)) {
				// Tense is locked
				findViewById(R.id.cycleVerb).setVisibility(View.VISIBLE);
				findViewById(R.id.cycleTense).setVisibility(View.INVISIBLE);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// Clean up listeners
		ImageView iv = (ImageView) findViewById(R.id.passFailIo);
		iv.setOnClickListener(null);

		iv = (ImageView) findViewById(R.id.passFailTu);
		iv.setOnClickListener(null);

		iv = (ImageView) findViewById(R.id.passFailLei);
		iv.setOnClickListener(null);

		iv = (ImageView) findViewById(R.id.passFailNoi);
		iv.setOnClickListener(null);

		iv = (ImageView) findViewById(R.id.passFailVoi);
		iv.setOnClickListener(null);

		iv = (ImageView) findViewById(R.id.passFailLoro);
		iv.setOnClickListener(null);

		VerbSpinner listVerb = (VerbSpinner) findViewById(R.id.listVerb);
		listVerb.setOnItemSelectedListener(null);
		listVerb.setAdapter(null);

		TenseSpinner listTense = (TenseSpinner) findViewById(R.id.listTense);
		listTense.setOnItemSelectedListener(null);
		listTense.setAdapter(null);

		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		switch(id) {
		case DIALOG_ABOUT:
			BillingDAO dao = null;
			try {
				dao = new BillingDAO(this);
				try {
					dao.open();
				} catch(Exception e) {}
				Deque<Object> purchasedItems = dao.getPurchasedItemsList();
				dialog.setContentView(R.layout.about);
				dialog.setTitle(R.string.about);
				LinearLayout aboutList = (LinearLayout)dialog.findViewById(R.id.aboutList);
				while (!purchasedItems.isEmpty()) {
					// Create a list of each expression type
					Object o = purchasedItems.pop();
					String s = o.toString();
					TextView tv = LayoutUtil.getATextView(getApplicationContext());
					tv.setText(s);
					aboutList.addView(tv, 4); // If about.xml layout changes, change 4 here
				}
				Button btnOk = (Button)dialog.findViewById(R.id.btnOk);
				btnOk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						removeDialog(DIALOG_ABOUT);
					}
				});
				
				// Link to Vocabulent
				Button btnApps = (Button)dialog.findViewById(R.id.btnApps);
				btnApps.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						removeDialog(DIALOG_ABOUT);
						try {
							Intent intent = getSisterIntent(R.string.sisterAppsLink);
							startActivity(intent);
						} catch (android.content.ActivityNotFoundException anfe) {
							Intent intent = getSisterIntent(R.string.sisterAppsWebLink);
							startActivity(intent);
						}
					}
				});

//				createAboutBoxDialog(purchasedItems,
//						null,
//						(ViewGroup) findViewById(R.id.aboutLayout),
//						R.id.aboutList);
			} finally {
				if(dao != null) {
					dao.close();
				}
			}
			break;
			default:
				dialog = null;
		}

		return dialog;
	}

	/**
	 * Gets an Intent to go to Google Play for a sister app like Vocabulent
	 * @return
	 */
	private Intent getSisterIntent(int linkId) {
		String link = getResources().getString(linkId);

		StringBuffer sb = new StringBuffer();
		sb.append(link);
		
		String sisterAppPkg = getResources().getString(R.string.sisterAppsPkg);
		sb.append(sisterAppPkg);

		// Show Vocabulent package that corresponds
		// Default to 'it' because Italian is the default
		// package and has no language code
		String s = AppConstants.DEFAULT_LANG_CD;
		String packageName = getPackageName();
		if(packageName != null) {
			String t = packageName.substring(packageName.length()-2);
			if("or".equals(t)) {
				s = AppConstants.DEFAULT_LANG_CD;
			} else if ("pt".equals(t)) {
				// If Portuguese, show French Vocabulent
				s = AppConstants.FRENCH_LANG_CD;
			} else {
				// fr or es
				s = t;
			}
		}
		sb.append(s);

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
		return intent;
	}
}