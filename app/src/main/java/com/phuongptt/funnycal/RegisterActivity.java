package com.phuongptt.funnycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phuongptt.funnycal.services.ApiService;
import com.phuongptt.funnycal.services.RetrofitClient;
import com.phuongptt.funnycal.services.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private String username;
    private String password;

    EditText editFullName;
    EditText editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        editFullName = (EditText) findViewById(R.id.edit_fullname);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_done){

            if (!editConfirmPassword.getText().toString().equals(password)) {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            ApiService service = RetrofitClient.getApiService();
            Call<LoginResponse> task = service.register(username, password, editFullName.getText().toString());
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

                            Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, body.message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}
