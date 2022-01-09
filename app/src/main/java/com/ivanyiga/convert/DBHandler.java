package com.ivanyiga.convert;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "ivanyiga";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "currencies";

    private static final String CURR_COL = "currencyString";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + CURR_COL + " TEXT)";

        db.execSQL(query);
    }

    //add new currencies to our sqlite database.
    public void addNewCurrencies(String currenciesString) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(CURR_COL, currenciesString);

        db.delete(TABLE_NAME,null,null);
        db.insert(TABLE_NAME, null, values);

        db.close();
    }
    public String getCurrencies() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCurrencies = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        String currString = null;
        if (cursorCurrencies.moveToFirst()) {
            do {
                currString = cursorCurrencies.getString(0);
            } while (cursorCurrencies.moveToNext());
            // moving our cursor to next.
        }

        cursorCurrencies.close();
        return currString;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}