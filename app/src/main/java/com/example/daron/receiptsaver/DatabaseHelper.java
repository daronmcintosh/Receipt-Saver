package com.example.daron.receiptsaver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ReceiptInfo"; // the name of our database
    public static final int DB_VERSION = 1; // the version of the database

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE RECEIPTINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "CATEGORY TEXT, "
                    + "DATE TEXT, "
                    + "TOTAL REAL, "
                    + "DESCRIPTION TEXT);");
//            insertSong(db, StreamMusicFragment.defaultURL, true);
        }
    }
}
