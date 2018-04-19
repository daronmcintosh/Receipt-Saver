package com.example.daron.receiptsaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString(SettingsFragment.THEME_KEY, "Light");
        String font = sharedPreferences.getString(SettingsFragment.FONT_KEY, "Casual");
        setApplicationTheme(theme);
        setApplicationFont(font);
        setContentView(R.layout.activity_home);

        showFavorites();

    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        HomeActivity.this.startActivity(intent);
    }

    public void showFavorites() {
        ReceiptListFragment receiptListFragment = new ReceiptListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, receiptListFragment)
                .addToBackStack(null)
                .commit();
    }


    public void setApplicationTheme(String theme) {
        if (theme.equals("Light")) {
            this.setTheme(R.style.Light);
        } else if (theme.equals("Dark")) {
            this.setTheme(R.style.Dark);
        }
    }

    public void setApplicationFont(String font) {
        if (font.equals("Casual")) {
            this.setTheme(R.style.FontCasual);
        } else if (font.equals("Cursive")) {
            this.setTheme(R.style.FontCursive);
        }
    }

}
