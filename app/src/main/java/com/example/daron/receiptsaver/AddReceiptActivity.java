package com.example.daron.receiptsaver;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);
        addReceiptFragment();
    }

    public void addReceiptFragment() {
        AddReceiptFragment addReceiptFragment = new AddReceiptFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, addReceiptFragment)
                .commit();
    }


}
