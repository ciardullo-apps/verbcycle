package com.ciardullo.conjugator.dao;

import android.content.Context;
import android.database.Cursor;

public class ConjugationDAO extends AbstractDAO {
	private static final String GET_CONJUGATION = "conjugation";
	private String[] allColumns = { 
			COLUMN_CONJUGATION_CONJUGATED
	};
	private int tenseId;
	private int verbId;

	public ConjugationDAO(Context context) {
		super(context);
		setSqlStmt(GET_CONJUGATION);
	}

	@Override
	public String[] getAllColumns() {
		return allColumns;
	}
	@Override
	public Cursor getAllRows() {
//		List<CommonVO> voList = new ArrayList<CommonVO>();
		Cursor cursor = null;
//		try {
			cursor = dbHelper.getDatabase().query(
					sqlStmt, 
					allColumns, 
					"verb_id = ? AND tense_id = ?", 
					getSelectionArgs(), 
					null, null, "sp_id");

//			cursor.moveToFirst();
//			do {
//				int theId = cursor.getInt(0);
//				String theName = cursor.getString(1);
//
//				// Call the handy Factory method
//				CommonVO commonVO = createVO(theId, theName);
//				voList.add(commonVO);
//			} while(cursor.moveToNext());
//
//		} finally {
//			try {
//				if(cursor != null) {
//					cursor.close();
//				}
//			} catch(Exception e) {}
		return cursor;
	}

	@Override
	protected String[] getSelectionArgs() {
		return new String[] { String.valueOf(verbId), String.valueOf(tenseId) };
	}

	public int getTenseId() {
		return tenseId;
	}

	public void setTenseId(int tenseId) {
		this.tenseId = tenseId;
	}

	public int getVerbId() {
		return verbId;
	}

	public void setVerbId(int verbId) {
		this.verbId = verbId;
	}
}
