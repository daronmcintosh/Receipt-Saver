package com.example.daron.receiptsaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PreviewReceiptFragment extends Fragment {
    ReceiptDataSource receiptDataSource;
    Receipt receipt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preview_receipt, container, false);

        Bundle bundle;
        bundle = getArguments();
        int id = (int) bundle.getLong("id");

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        receiptDataSource = new ReceiptDataSource(view.getContext());
        receiptDataSource.open();

        receipt = receiptDataSource.getReceipt(id);
        TextView total = (TextView) view.findViewById(R.id.total);
        TextView category = (TextView) view.findViewById(R.id.category);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        total.setText("$ " + Double.toString(receipt.getTotal()));
        category.setText(receipt.getCategory());
        date.setText(receipt.getDate());
        description.setText(receipt.getDescription());

        actionBar.setTitle("Receipt Name: " + receipt.getName());

        receiptDataSource.close();
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (this.isVisible()) {
            menu.findItem(R.id.action_delete).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(getActivity(), AddReceiptActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                receiptDataSource.open();
                receiptDataSource.deleteReceipt(receipt);
                receiptDataSource.close();
                intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
