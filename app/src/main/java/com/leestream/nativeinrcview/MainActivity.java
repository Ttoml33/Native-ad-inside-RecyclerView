package com.leestream.nativeinrcview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AdapterClass adapter;
    private final List<NativeAd> ads = new ArrayList<>();
    private int count = 0;
    private int AD_COUNT = 2;
    private RequestQueue queue;
    private AdView adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//instantiating admob to our app
        AdsManager adsManager = new AdsManager(this);
//Initializing RCViEW
        RecyclerView rv = findViewById(R.id.testRc);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        adapter = new AdapterClass(this);
        rv.setAdapter(adapter);

        //requesting to read data from api
        sendRequest();

        //Loading Native ad from admob
        adsManager.createUnifiedAds(AD_COUNT, R.string.admob_native_ad, new AdNativeListening() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                count++;
                Log.e("AdNativeListening", "Ad failed to load: " + loadAdError.getMessage());
                if (count == AD_COUNT) {
                    adapter.mergeData();
                }
            }
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                count++;
                ads.add(nativeAd);
                Log.d("AdNativeListening", "Ad loaded successfully");
                if (count == AD_COUNT) {
                    adapter.setAds(ads);
                    adapter.mergeData();
                }
            }
        });
    }

//Requesting && Reading to read data from reqres api
    private void sendRequest() {
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://reqres.in/api/users?delay=3",
                response -> {
            //settingData to our model
                    adapter.setData(getEmployees(response));
                    if (count == AD_COUNT) {
                        adapter.mergeData();
                    }
                }, error -> Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());
        stringRequest.setTag("T");
        queue.add(stringRequest);
    }
    private List<UserModel> getEmployees(String json) {
        List<UserModel> users = new ArrayList<>();
        try {
            String jsonString = json;
            JSONObject job = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray(job.getString("data"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UserModel user = new UserModel();
                user.setName(jsonObject.getString("first_name"));
                user.setEmail(jsonObject.getString("email"));
                user.setImgUrl(jsonObject.getString("avatar"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.addAll(users);
        users.addAll(users);
        return users;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll("T");
        }
    }
}