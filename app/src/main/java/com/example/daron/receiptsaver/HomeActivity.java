package com.example.daron.receiptsaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;

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

        showFavoritesFragment();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void showFavoritesFragment() {
        ReceiptListFragment receiptListFragment = new ReceiptListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, receiptListFragment)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                HomeActivity.this.startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(HomeActivity.this, AddReceiptActivity.class);
                HomeActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
    }
}