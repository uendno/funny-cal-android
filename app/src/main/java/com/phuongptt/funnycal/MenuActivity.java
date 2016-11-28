package com.phuongptt.funnycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.phuongptt.funnycal.services.models.User;

public class MenuActivity extends AppCompatActivity {

    private TextView username;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        username = (TextView) findViewById(R.id.menu_username);
        score = (TextView) findViewById(R.id.menu_score);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("my_profile", "");
        User profile = gson.fromJson(json, User.class);

        username.setText(profile.fullName);
        score.setText(String.valueOf(profile.score));

    }

    public void start(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void highScore(View v) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }

    public void exit(View v) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("my_profile", "");
        prefsEditor.commit();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
