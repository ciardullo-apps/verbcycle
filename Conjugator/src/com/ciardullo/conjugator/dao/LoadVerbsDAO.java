package com.ciardullo.conjugator.dao;

import android.content.Context;
import android.database.Cursor;

import com.ciardullo.conjugator.vo.CommonVO;
import com.ciardullo.conjugator.vo.Verb;

/**
 * Performs DML on the verb table. Overrides parent's
 * getAllRows() to use SQLiteDatabase.rawQuery()
 */
public class LoadVerbsDAO extends LoadLookupTableDAO {

	private static final String GET_VERBS = "select v.lkup_name as lkup_name, v._id as _id " +
		"from verb v, iab i, iab_asset a ";
	private String[] selectionArgs;

	// Add support for purchase_level column
	private static String WHERE_CLAUSE = "where i._id = a._id and v.purchase_level = a.purchase_level ";
	private static String ORDER_BY = "order by ";

	@Override
	protected CommonVO createVO(int theId, String theName) {
		return new Verb(theId, theName);
	}

	public LoadVerbsDAO(Context context) {
		super(context);
		setSqlStmt(GET_VERBS);
	}

	@Override
	public String[] getSelectionArgs() {
		return selectionArgs;
	}

	@Override
	public void setSelectionArgs(String[] args) {
		selectionArgs = args;
	}

	public String getOrderBy() {
		return COLUMN_LKUP_NAME;
	}

	/**
	 * In order to support verb.purchase_level,
	 * support multi-column where clause. Always
	 * set purchase_level in the where clause
	 * @return
	 */
	public String getSelection() {
		StringBuffer sb = new StringBuffer();
		sb.append(WHERE_CLAUSE);
		if(this.selection != null && !"".equals(this.selection)) {
			// There is already a where clause... append to it
			sb.append(" AND ");
			sb.append(this.selection);
		}
		
		return sb.toString();
	}

	/**
	 * Need to override getAllRows() to use rawQuery()
	 */
	@Override
	public Cursor getAllRows() {
		StringBuffer sb = new StringBuffer();
		sb.append(GET_VERBS);
		sb.append(getSelection());
		if(getSelectionArgs() != null && getSelectionArgs().length > 0) {
			// Filter verbs by type if preference is set
			sb.append(getSelectionArgs()[0]);
			sb.append(" ");
		}
		sb.append(ORDER_BY);
		sb.append(getOrderBy());

		Cursor cursor = null;
		cursor = dbHelper.getDatabase().rawQuery(
			sb.toString(), 
			null);
		return cursor;
	}

}
