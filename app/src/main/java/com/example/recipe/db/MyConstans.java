package com.example.recipe.db;

public class MyConstans
{
    public static final String EDIT_STATE = "edit_state";
    public static final String LIST_ITEM_INTENT = "list_item_intent";
    public static final String TABLE_NAME = "my_table";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String PRODUCTS = "products";
    public static final String DESC = "desc";
    public static final String CALORIES = "calories";
    public static final String DB_NAME = "my_db.db";
    public static final String URI = "uri";
    public static final String FAVORITE = "favorite";
    public static final int DB_VERSION = 4;

    public static final String TABLE_STR = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + PRODUCTS +
            " TEXT," + DESC + " TEXT," +
            CALORIES + " TEXT," + URI + " TEXT," + FAVORITE + " INTEGER)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
