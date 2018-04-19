package com.example.daron.receiptsaver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PreviewReceiptFragment extends Fragment {
    ReceiptDataSource receiptDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preview_receipt, container, false);
        Bundle bundle;
        bundle = getArguments();
        int id = (int) bundle.getLong("id");
        receiptDataSource = new ReceiptDataSource(view.getContext());
        receiptDataSource.open();
        Receipt receipt = receiptDataSource.getReceipt(id);
        TextView total = (TextView) view.findViewById(R.id.total);
        TextView category = (TextView) view.findViewById(R.id.category);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        total.setText(Double.toString(receipt.getTotal()));
        category.setText(receipt.getCategory());
        date.setText(receipt.getDate());
        description.setText(receipt.getDescription());
        // Inflate the layout for this fragment
        return view;
    }

}
