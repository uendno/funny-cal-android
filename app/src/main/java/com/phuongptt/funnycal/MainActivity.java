package com.phuongptt.funnycal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phuongptt.funnycal.services.ApiService;
import com.phuongptt.funnycal.services.RetrofitClient;
import com.phuongptt.funnycal.services.models.BaseResponse;
import com.phuongptt.funnycal.services.models.User;
import com.phuongptt.funnycal.services.models.UsersResponse;

import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.webview);

        // Let's display the progress in the activity title bar, like the
        // browser app does.

        Scanner s = new Scanner(getResources().openRawResource(R.raw.html));

        String html = "";

        try {
            while (s.hasNext()) {
                String word = s.next();
                html = html.concat(word);
            }
        } finally {
            s.close();
        }


        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowContentAccess(true);

//        if (Build.VERSION.SDK_INT >= 19) {
//            // chromium, enable hardware acceleration
//            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            // older android version, disable hardware acceleration
//            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//
//        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        final Activity activity = this;

        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                boolean result = url.contains("file:///score=");

                if (result) {

                    int score = Integer.parseInt(url.substring(14));

                    final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Gson gson = new Gson();
                    String json = mPrefs.getString("my_profile", "");
                    final User profile = gson.fromJson(json, User.class);

                    if (score > profile.score) {
                        profile.score = score;
                        ApiService service = RetrofitClient.getApiService();
                        Call<BaseResponse> task = service.updateScore(score, Authentication.getInstance().getToken());
                        task.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if (response.isSuccessful()) {

                                    BaseResponse body = response.body();
                                    if (body.success) {
                                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(profile);
                                        prefsEditor.putString("my_profile", json);
                                        prefsEditor.commit();
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, body.message, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    if (response.code() == 401) {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }

            }

        });

        webview.loadUrl("file:///android_res/raw/html.html");
    }
}
