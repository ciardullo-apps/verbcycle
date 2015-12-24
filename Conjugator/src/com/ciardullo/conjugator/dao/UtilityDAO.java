package com.ciardullo.conjugator.dao;

import android.content.Context;
import android.database.Cursor;

/**
 * Contains static methods to invoke simple DML
 */
public class UtilityDAO implements DBConstants{

	/**
	 * Returns the count of rows in the tense table
	 * @param context
	 * @return
	 */
	public static int getTenseCount(Context context) {
		ConjugatorDBHelper dbHelper = null;
		Cursor cursor = null;
		int n = 0;
		try {
			dbHelper = new ConjugatorDBHelper(context);
			dbHelper.openDatabase();
			cursor = dbHelper.getReadableDatabase()
					.rawQuery(SQL_TENSE_COUNT, null);
			if(cursor != null && cursor.moveToFirst())
				n = cursor.getInt(0);
		} finally {
			try {
				if(cursor != null)
					cursor.close();
			} catch(Exception e) {}
			try {
				if(dbHelper != null)
					dbHelper.close();
			} catch(Exception e) {}
		}
		
		return n;
	}

}
