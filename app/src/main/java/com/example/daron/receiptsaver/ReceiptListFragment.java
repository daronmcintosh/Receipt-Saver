package com.example.daron.receiptsaver;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;

public class ReceiptListFragment extends ListFragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private SQLiteDatabase db;
    private Cursor receiptsCursor;
    private SQLiteOpenHelper databaseHelper;
    private ViewGroup viewGroup;
    private ActionBar actionBar;
    private ReceiptDataSource receiptDataSource;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        databaseHelper = new ReceiptDatabaseHelper(context);
        receiptDataSource = new ReceiptDataSource(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewGroup = container;
        if (container != null) {
            container.removeAllViews();
        }
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = sharedPreferences.getString(SettingsFragment.CURRENCY_KEY, "Dollar");


        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        setApplicationCurrency(currency);

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

    public void setApplicationCurrency(String currency) {
        DecimalFormat df = new DecimalFormat("#.00");
        receiptDataSource.open();
        if (currency.equals("Dollar")) {
            actionBar.setTitle("Total Expenses: " + getString(R.string.dollar) + df.format(receiptDataSource.getTotalExpenses()));
        } else if (currency.equals("Euro")) {
            actionBar.setTitle("Total Expenses: " + getString(R.string.euro) + df.format(receiptDataSource.getTotalExpenses()));
        }
        receiptDataSource.close();
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

