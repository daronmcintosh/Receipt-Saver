package com.example.daron.receiptsaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.users.FullAccount;
import com.example.daron.receiptsaver.dropbox.DropboxActivity;
import com.example.daron.receiptsaver.dropbox.DropboxClientFactory;
import com.example.daron.receiptsaver.dropbox.GetCurrentAccountTask;

public class LoginActivity extends DropboxActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString(SettingsFragment.THEME_KEY, "Light");
        String font = sharedPreferences.getString(SettingsFragment.FONT_KEY, "Casual");
        setApplicationTheme(theme);
        setApplicationFont(font);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasToken()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            LoginActivity.this.startActivity(intent);
        }
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
    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                Toast.makeText(getApplicationContext(), "Signed in as:  " + result.getEmail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

    public void logInDropbox(View view){
        Auth.startOAuth2Authentication(LoginActivity.this, getString(R.string.app_key));

    }
}
