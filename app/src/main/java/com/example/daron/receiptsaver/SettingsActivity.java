package com.example.daron.receiptsaver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.users.FullAccount;
import com.example.daron.receiptsaver.dropbox.DropboxActivity;
import com.example.daron.receiptsaver.dropbox.DropboxClientFactory;
import com.example.daron.receiptsaver.dropbox.GetCurrentAccountTask;

import static com.dropbox.core.android.AuthActivity.result;

public class SettingsActivity extends AppCompatActivity {

    private final String LOG_TAG = SettingsActivity.class.getSimpleName();
    private TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the default shared preferences name and mode
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Creates an instance that can be used to retrieve and listen to values of the preferences.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //it gets the THEME_KEY & FONT_KEY from the settings fragment
        String theme = sharedPreferences.getString(SettingsFragment.THEME_KEY, "Light");
        String font = sharedPreferences.getString(SettingsFragment.FONT_KEY, "Casual");

        //applies the theme and font to the activity
        setApplicationTheme(theme);
        setApplicationFont(font);

        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();

        account = findViewById(R.id.account);
        loadData();
    }


    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(FullAccount result) {
                account.setText("Signed in as: " + result.getEmail());
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
