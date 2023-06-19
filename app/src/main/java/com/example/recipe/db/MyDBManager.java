package com.example.recipe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.recipe.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDBManager
{
    private Context context;
    private MyDBHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDBManager(Context context)
    {
        this.context = context;
        myDbHelper = new MyDBHelper(context);
    }

    public void openDb()
    {
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String title, String products, String desc, String calories, String uri, int fav)
    {
        ContentValues cv = new ContentValues();
        cv.put(MyConstans.TITLE, title);
        cv.put(MyConstans.DESC, desc);
        cv.put(MyConstans.URI, uri);
        cv.put(MyConstans.PRODUCTS, products);
        cv.put(MyConstans.CALORIES, calories);
        cv.put(MyConstans.FAVORITE, fav);
        db.insert(MyConstans.TABLE_NAME, null, cv);
    }

    public void update(String title, String products, String desc, String calories, String uri, int id, int fav)
    {
        String selection = MyConstans._ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstans.TITLE, title);
        cv.put(MyConstans.DESC, desc);
        cv.put(MyConstans.URI, uri);
        cv.put(MyConstans.PRODUCTS, products);
        cv.put(MyConstans.CALORIES, calories);
        cv.put(MyConstans.FAVORITE, fav);
        db.update(MyConstans.TABLE_NAME, cv, selection, null);
    }

    public void delete(int id)
    {
        String selection = MyConstans._ID + "=" + id;
        db.delete(MyConstans.TABLE_NAME, selection, null);
    }

    public void getFromDb(String titleSearch, OnDataRecieved onDataRecieved)
    {
        List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstans.TITLE + " like ?";
        Cursor cursor = db.query(MyConstans.TABLE_NAME, null,
                selection, new String[]{"%" + titleSearch + "%"}, null, null, null);

        while (cursor.moveToNext())
        {
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.TITLE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.DESC));
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.URI));
            String products = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.PRODUCTS));
            String calories = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.CALORIES));
            int _id = cursor.getInt(cursor.getColumnIndexOrThrow(MyConstans._ID));
            item.setTitle(title);
            item.setDesc(desc);
            item.setUri(uri);
            item.setProducts(products);
            item.setCalories(calories);
            item.setId(_id);
            tempList.add(item);
        }

        cursor.close();
        onDataRecieved.onRecived(tempList);
    }

    public void getFromDbFav(String titleSearch, OnDataRecieved onDataRecieved)
    {
        List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstans.FAVORITE + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(MyConstans.TABLE_NAME, null, selection, selectionArgs,
                null, null, null);
        while (cursor.moveToNext())
        {
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.TITLE));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.DESC));
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.URI));
            String products = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.PRODUCTS));
            String calories = cursor.getString(cursor.getColumnIndexOrThrow(MyConstans.CALORIES));
            int _id = cursor.getInt(cursor.getColumnIndexOrThrow(MyConstans._ID));
            item.setTitle(title);
            item.setDesc(desc);
            item.setUri(uri);
            item.setProducts(products);
            item.setCalories(calories);
            item.setId(_id);
            tempList.add(item);
        }
        cursor.close();
        onDataRecieved.onRecived(tempList);
    }

    public void closeDb()
    {
        myDbHelper.close();
    }
}
