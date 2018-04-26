package com.example.daron.receiptsaver;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;


public class AddReceiptFragment extends Fragment implements View.OnClickListener {

    private ReceiptDataSource receiptDataSource;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public AddReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_receipt, container, false);
        receiptDataSource = new ReceiptDataSource(view.getContext());
        Button saveButton = view.findViewById(R.id.save);
        ImageButton takeAPicButton = view.findViewById(R.id.takeAPicture);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.add);
        saveButton.setOnClickListener(this);
        takeAPicButton.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
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
            case R.id.takeAPicture:
                dispatchTakePictureIntent();
        }

    }

    // Used to take a photo
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Picture is displayed as a bitmap
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) getView().findViewById(R.id.picture);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
