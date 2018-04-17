package com.example.daron.receiptsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReceiptDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ReceiptInfo"; // the name of our database
    public static final int DB_VERSION = 1; // the version of the database

    ReceiptDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE RECEIPTINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "CATEGORY TEXT, "
                    + "DATE TEXT, "
                    + "TOTAL REAL, "
                    + "DESCRIPTION TEXT);");
            insertReceipt(db, "Walmart", "Grocery", "4/17/2018", 150.50, "Weekly grocery");
            insertReceipt(db, "Chinese Restaurant", "Food", "4/14/2018", 25.20, "Weekly grocery");
            insertReceipt(db, "Auto Shop", "Maintenance", "4/14/2018", 43.15, "Oil change");
        }
    }

    public void insertReceipt(SQLiteDatabase db, String name, String category, String date, double total, String description) {
        ContentValues receiptValues = new ContentValues();
        receiptValues.put("NAME", name);
        receiptValues.put("CATEGORY", category);
        receiptValues.put("DATE", date);
        receiptValues.put("TOTAL", total);
        receiptValues.put("DESCRIPTION", description);
        db.insert("RECEIPTINFO", null, receiptValues);
    }
}
