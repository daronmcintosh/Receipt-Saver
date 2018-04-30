package com.example.daron.receiptsaver;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import static android.app.Activity.RESULT_OK;


public class AddReceiptFragment extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ReceiptDataSource receiptDataSource;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText nameView, categoryView, dateView, totalView, descriptionView;
    private String name, category, date, total, description;

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

        nameView = (EditText) view.findViewById(R.id.receiptNameInput);
        categoryView = (EditText) view.findViewById(R.id.categoryInput);
        dateView = (EditText) view.findViewById(R.id.dateInput);
        totalView = (EditText) view.findViewById(R.id.totalInput);
        descriptionView = (EditText) view.findViewById(R.id.descriptionInput);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (!textFieldsAreEmpty()) {
                    name = nameView.getText().toString();
                    category = categoryView.getText().toString();
                    date = dateView.getText().toString();
                    total = totalView.getText().toString();
                    description = descriptionView.getText().toString();
                    Receipt newReceipt = new Receipt(name, category, date, Double.parseDouble(total), description);
                    receiptDataSource.open();
                    receiptDataSource.createReceipt(newReceipt);
                    receiptDataSource.close();
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(LOG_TAG, Boolean.toString(!textFieldsAreEmpty()));
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.takeAPicture:
                dispatchTakePictureIntent();
                break;
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

    public boolean textFieldsAreEmpty() {
        name = nameView.getText().toString();
        category = categoryView.getText().toString();
        date = dateView.getText().toString();
        total = totalView.getText().toString();
        description = descriptionView.getText().toString();
        return name.trim().length() == 0 || category.trim().length() == 0 || date.trim().length() == 0 || total.trim().length() == 0 || description.trim().length() == 0;
    }
}
