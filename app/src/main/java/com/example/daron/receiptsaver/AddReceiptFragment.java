package com.example.daron.receiptsaver;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddReceiptFragment extends Fragment implements View.OnClickListener {

    private ReceiptDataSource receiptDataSource;

    public AddReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_receipt, container, false);
        receiptDataSource =  new ReceiptDataSource(view.getContext());
        Button saveButton = view.findViewById(R.id.save);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.add);
        saveButton.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        EditText nameView = (EditText) getView().findViewById(R.id.nameInput);
        EditText categoryView = (EditText) getView().findViewById(R.id.categoryInput);
        EditText dateView = (EditText) getView().findViewById(R.id.dateInput);
        EditText totalView = (EditText) getView().findViewById(R.id.totalInput);
        EditText descriptionView = (EditText) getView().findViewById(R.id.descriptionInput);

        String name = nameView.getText().toString();
        String category = categoryView.getText().toString();
        String date = dateView.getText().toString();
        Double total = Double.parseDouble(totalView.getText().toString());
        String description = descriptionView.getText().toString();

        Receipt newReceipt = new Receipt(name, category, date, total, description);
        receiptDataSource.open();
        receiptDataSource.createReceipt(newReceipt);
        receiptDataSource.close();
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }
}
