package com.ciardullo.conjugator;

public interface AppConstants {
	public enum SpinnerType { VERB, TENSE };
	public static final String PREF_FILTER_BY = "PREF_FILTER_BY";
	public static final String PREF_FILTER_BY_NO_FILTER = "9";
	public static final String PREF_FILTER_BY_FORM_OTHER = "0";
	public static final String PREF_FILTER_BY_FORM_ARE = "1";
	public static final String PREF_FILTER_BY_FORM_ERE = "2";
	public static final String PREF_FILTER_BY_FORM_IRE = "3";
	public static final String PREF_FILTER_BY_TRANS_TRANSITIVE = "4";
	public static final String PREF_FILTER_BY_TRANS_INTRANSITIVE = "5";
	public static final String PREF_FILTER_BY_FORM = "forma = ";
	public static final String PREF_FILTER_BY_TRANS = "transitivo = ";
	public static final String WHERE_BY_ID = "_id = ";
	public static final String WHERE_BY_ID_PARAM = "_id = ?";

	public static final String PREF_CYCLE_BY = "PREF_CYCLE_BY";
	public static final String PREF_CYCLE_BY_TENSE = "0";
	public static final String PREF_CYCLE_BY_VERB = "1";

	// Used by SettingsActivity as result to ConjugatorActivity
	public static final int RESULT_OK_POPULATE_VERB_SPINNER = 901;

	public static final int MENU_ITEM_SETTINGS = 0;
	public static final int MENU_ITEM_ABOUT = 5;
	public static final int MENU_ITEM_UPGRADE = 1;
	public static final int MENU_ITEM_FLASH = 2;
	
	public static final int DIALOG_ABOUT = 0;

	public static String DEFAULT_LANG_CD = "it";
	public static String FRENCH_LANG_CD = "fr";
	
	// TODO Remove backup from conjugator
//	public static final int MENU_ITEM_BACKUP = 2;
}
