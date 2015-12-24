package com.ciardullo.conjugator.dao;

public interface DBConstants {
	public static String DATABASE_PATH = "/data/data/{package}/databases/";
	public static final String DATABASE_NAME = "conjugator.db";

	public static final String TBL_NM_CONJUGATION = "conjugation";
	public static final String TBL_NM_PURCHASE_HISTORY = "iab_hist";
	public static final String TBL_NM_PURCHASE = "iab";

	public static final String SQL_CONJUGATION = "SELECT conjugated FROM " + TBL_NM_CONJUGATION +
			" ORDER BY verb_id, tense_id, sp_id ";
	
	public static final String SQL_TENSE_COUNT = "SELECT count(*) FROM tense";

	public static final int DATABASE_VERSION = 1;
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LKUP_NAME = "lkup_name";
	public static final String COLUMN_CONJUGATION_CONJUGATED = "conjugated";
	
    // These are the column names for the purchase history table. We need a
    // column named "_id" if we want to use a CursorAdapter. The primary key is
    // the orderId so that we can be robust against getting multiple messages
    // from the server for the same purchase.
    public static final String IAB_HIST_ORDER_ID_COL = "_id";
    public static final String IAB_HIST_STATE_COL = "state";
    public static final String IAB_HIST_PRODUCT_ID_COL = "productId";
    public static final String IAB_HIST_PURCHASE_TIME_COL = "transTime";
    public static final String IAB_HIST_DEVELOPER_PAYLOAD_COL = "payload";

    // These are the column names for the "purchased items" table.
    public static final String IAB_PRODUCT_ID_COL = "_id";
    public static final String IAB_QUANTITY_COL = "quantity";
    public static final String IAB_PRODUCT_DESC_COL = "description";

}
