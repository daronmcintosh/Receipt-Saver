package com.example.daron.receiptsaver;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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

import com.dropbox.core.v2.files.FileMetadata;
import com.example.daron.receiptsaver.dropbox.DropboxClientFactory;
import com.example.daron.receiptsaver.dropbox.UploadFileTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class AddReceiptFragment extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ReceiptDataSource receiptDataSource;
    static final int REQUEST_TAKE_PHOTO = 1;
    private EditText nameView, categoryView, dateView, totalView, descriptionView;
    private String name, category, date, total, description;
    private String currentPhotoPath, filename;
    private ImageView receiptImage;

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
        receiptImage = (ImageView) view.findViewById(R.id.receiptImage);

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
                    Receipt newReceipt = new Receipt(name, category, date, Double.parseDouble(total), description, filename);
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
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.daron.receiptsaver.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "receipt_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        filename = image.getName();
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uploadPhoto();
        setPic();
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = receiptImage.getWidth();
        int targetH = receiptImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        receiptImage.setImageBitmap(bitmap);
    }


    public void uploadPhoto() {
        File file = new File(currentPhotoPath);
        Uri uri = Uri.fromFile(file);
        new UploadFileTask(getContext(), DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                // since file is saved on dropbox, we don't want it to save on the device
                deletePhotoFromDevice();
            }
            @Override
            public void onError(Exception e) {

            }
        }).execute(uri.toString(), "");
    }

    public boolean deletePhotoFromDevice(){
        return new File(currentPhotoPath).delete();
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
