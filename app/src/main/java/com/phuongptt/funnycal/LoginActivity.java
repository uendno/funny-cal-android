package com.phuongptt.funnycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phuongptt.funnycal.services.ApiService;
import com.phuongptt.funnycal.services.RetrofitClient;
import com.phuongptt.funnycal.services.models.LoginResponse;
import com.phuongptt.funnycal.services.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editUsername;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("my_profile", "");
        User profile = gson.fromJson(json, User.class);
        if (profile != null) {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login(View v) {

        boolean result = validate();

        if (result) {
            ApiService service = RetrofitClient.getApiService();
            Call<LoginResponse> task = service.login(editUsername.getText().toString(), editPassword.getText().toString());
            task.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {

                        LoginResponse body = response.body();
                        if (body.success) {

                            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(body.profile);
                            prefsEditor.putString("my_profile", json);
                            prefsEditor.commit();

                            Authentication.getInstance().setProfile(body.profile);
                            Authentication.getInstance().setToken(body.token);

                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, body.message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void register(View v) {
        boolean result = validate();

        if (result) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("username", editUsername.getText().toString());
            intent.putExtra("password", editPassword.getText().toString());
            startActivity(intent);
        }
    }

    private boolean validate() {
        if (editUsername.getText().toString().equals("")) {
            Toast.makeText(this, "Your have to enter your username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editPassword.getText().toString().equals("")) {
            Toast.makeText(this, "You have to enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
