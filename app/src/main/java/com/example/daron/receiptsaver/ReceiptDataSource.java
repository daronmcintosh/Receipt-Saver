package com.example.daron.receiptsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDataSource {

    private final String LOG_TAG = this.getClass().getSimpleName();


    // Database fields
    private SQLiteDatabase database;
    private ReceiptDatabaseHelper dbHelper;
    private Context context;
//    private String[] allColumns = {"_id",
//            "NAME"};

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
        database.insert(ReceiptDatabaseHelper.TABLE_NAME, null, contentValues);
//        Cursor cursor = database.query("RECEIPTINFO", allColumns,   "_id =", new String[]{Long.toString(insertID)},
//                null, null, null, null);
//        cursor.moveToFirst();
//        Comment newComment = cursorToComment(cursor);
//        return newComment;
    }

    public void deleteReceipt(Receipt receipt) {
        long id = receipt.getId();
        database.delete(ReceiptDatabaseHelper.TABLE_NAME, "_id =" + id, null);
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
            receipt = new Receipt(receiptId, name, category, date, total, description);
            cursor.close();
            return receipt;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }
//    public List<Comment> getAllComments() {
//        List<Comment> comments = new ArrayList<Comment>();
//        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null);
//        cursor.moveToFirst();
//        while (cursor.isAfterLast()) {
//            Comment comment = cursorToComment(cursor);
//            comments.add(comment);
//            cursor.moveToNext();
//        }
//        return comments;
//    }

//    private Comment cursorToComment(Cursor cursor) {
//        Comment comment = new Comment();
//        if (cursor.getCount() > 0) {
//            comment.setId(cursor.getLong(0));
//            comment.setComment(cursor.getString(1));
//        }
//        return comment;
//    }
}
