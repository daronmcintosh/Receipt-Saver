package com.example.daron.receiptsaver;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ReceiptListFragment extends ListFragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private SQLiteDatabase db;
    private Cursor receiptsCursor;
    private SQLiteOpenHelper databaseHelper;
    private ViewGroup viewGroup;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        databaseHelper = new ReceiptDatabaseHelper(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewGroup = container;
        if (container != null) {
            container.removeAllViews();
        }
        try {
            db = databaseHelper.getReadableDatabase();
            receiptsCursor = db.query(ReceiptDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", "NAME"},
                    null,
                    null, null, null, null);
            CursorAdapter receiptAdapter = new SimpleCursorAdapter(inflater.getContext(), android.R.layout.simple_list_item_1, receiptsCursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            setListAdapter(receiptAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(inflater.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e(LOG_TAG, Long.toString(id));
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        PreviewReceiptFragment previewReceiptFragment = new PreviewReceiptFragment();
        previewReceiptFragment.setArguments(bundle);
        if (getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(viewGroup.getId(), previewReceiptFragment)
                    .addToBackStack(null)
                    .commit();

        }
    }
}

