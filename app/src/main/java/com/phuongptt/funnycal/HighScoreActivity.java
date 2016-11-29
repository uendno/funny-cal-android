package com.phuongptt.funnycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phuongptt.funnycal.services.ApiService;
import com.phuongptt.funnycal.services.RetrofitClient;
import com.phuongptt.funnycal.services.models.LoginResponse;
import com.phuongptt.funnycal.services.models.User;
import com.phuongptt.funnycal.services.models.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighScoreActivity extends AppCompatActivity {

    private List<User> users;
    private RecyclerView recyclerView;
    private HighScoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        recyclerView = (RecyclerView) findViewById(R.id.list_high_score);

        adapter = new HighScoreListAdapter(new ArrayList<User>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        ApiService service = RetrofitClient.getApiService();
        Call<UsersResponse> task = service.getAllUsers(Authentication.getInstance().getToken());
        task.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {

                    UsersResponse body = response.body();
                    if (body.success) {
                        users = body.users;
                        recyclerView.swapAdapter(new HighScoreListAdapter(users), true);
                    } else {
                        Toast.makeText(HighScoreActivity.this, body.message, Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if (response.code() == 401) {
                        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.putString("my_profile", "");
                        prefsEditor.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HighScoreActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {

                Toast.makeText(HighScoreActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
