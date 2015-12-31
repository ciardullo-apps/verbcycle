package com.ciardullo.conjugator.dao;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public abstract class AbstractDAO implements DBConstants {
	protected ConjugatorDBHelper dbHelper;
	protected String sqlStmt;

	public abstract String[] getAllColumns();
	public abstract Cursor getAllRows();
	protected abstract String[] getSelectionArgs();

	
	public AbstractDAO(Context context) {
		dbHelper = new ConjugatorDBHelper(context);
	}

	public void open() throws SQLException, IOException {
		try {
			dbHelper.createDatabase();
		} catch(IOException e) {
			Log.e(LoadLookupTableDAO.class.getName(), e.getMessage());
			throw e;
		}
		
		try {
			dbHelper.openDatabase();
		} catch(SQLException e) {
			Log.e(LoadLookupTableDAO.class.getName(), e.getMessage());
			throw e;
		}
	}
	
	public void close() throws SQLException {
		dbHelper.close();
	}

	public String getSqlStmt() {
		return sqlStmt;
	}

	public void setSqlStmt(String sqlStmt) {
		this.sqlStmt = sqlStmt;
	}
}
