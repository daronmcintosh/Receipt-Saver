package com.example.daron.receiptsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;
public class ReceiptDataSource {

    private final String LOG_TAG = this.getClass().getSimpleName();


    // Database fields
    private SQLiteDatabase database;
    private ReceiptDatabaseHelper dbHelper;
    private Context context;

    public ReceiptDataSource(Context context) {
        dbHelper = new ReceiptDatabaseHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void close() {
        database.close();
    }

    public void createReceipt(Receipt receipt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", receipt.getName());
        contentValues.put("CATEGORY", receipt.getCategory());
        contentValues.put("DATE", receipt.getDate());
        contentValues.put("TOTAL", receipt.getTotal());
        contentValues.put("DESCRIPTION", receipt.getDescription());
        contentValues.put("FILENAME", receipt.getFilename());
        database.insert(ReceiptDatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public void deleteReceipt(Receipt receipt) {
        long id = receipt.getId();
        database.delete(ReceiptDatabaseHelper.TABLE_NAME, "_id =?", new String[]{Long.toString(id)});
    }

    public Receipt getReceipt(int id) {
        Receipt receipt;
        try {
            Cursor cursor = database.query(ReceiptDatabaseHelper.TABLE_NAME, null, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);
            cursor.moveToFirst();
            int receiptId = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            String category = cursor.getString(cursor.getColumnIndex("CATEGORY"));
            String date = cursor.getString(cursor.getColumnIndex("DATE"));
            double total = cursor.getDouble(cursor.getColumnIndex("TOTAL"));
            String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            String filename = cursor.getString(cursor.getColumnIndex("FILENAME"));
            receipt = new Receipt(receiptId, name, category, date, total, description, filename);
            cursor.close();
            return receipt;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }
    public Double getTotalExpenses() {
        double total = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(TOTAL) FROM RECEIPTINFO", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            return total;
        }
        cursor.close();
        return total;
    }

}
