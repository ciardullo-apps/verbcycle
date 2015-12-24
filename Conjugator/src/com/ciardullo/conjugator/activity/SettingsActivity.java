package com.ciardullo.conjugator.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.R;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, AppConstants {

	boolean filterByChanged;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setContentView(R.layout.menu);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setPrefSummaries();
		filterByChanged = false;
	}

	@Override
	public void finish() {
		if(filterByChanged)
			setResult(RESULT_OK_POPULATE_VERB_SPINNER);
		else
			setResult(RESULT_OK);

		super.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key != null && PREF_FILTER_BY.equals(key)) {
			// Filter by changed, tell ConjugatorActivity to populate the verb spinner
			filterByChanged = true;
		}

		setPrefSummaries();
	}

	/**
	 * Sets the preference summaries to display the current values
	 */
	private void setPrefSummaries() {
		Preference pref = findPreference(PREF_FILTER_BY);
		setPrefSummary(R.string.labelFilterBy, pref);
		pref = findPreference(PREF_CYCLE_BY);
		setPrefSummary(R.string.labelCycleBy, pref);
	}
	
	/**
	 * Sets the summary of a Preference to labelPrefix + current value
	 * @param labelPrefix
	 * @param pref
	 */
	private void setPrefSummary(int resId, Preference pref) {
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(resId);
			StringBuffer sb = new StringBuffer();
			sb.append(pref.getSummary());
			sb.append(" ");
			sb.append(listPref.getEntry());
			pref.setSummary(sb.toString());
		}
	}
}
