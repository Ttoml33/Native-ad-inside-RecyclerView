package com.leestream.nativeinrcview;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

public class AdsManager
{
    private Context context;

    //instantiating admob to our app
    public AdsManager(Context context)
    {
        this.context = context;
        MobileAds.initialize(context, initializationStatus -> {
        });
    }

    //showing native add on the layout
    public void createUnifiedAds(int numads, int unitid, AdNativeListening listening)
    {
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(unitid));
        builder.forNativeAd(listening);
        builder.withAdListener(listening);
        AdLoader adload= builder.build();
        adload.loadAds(new AdRequest.Builder().build(),numads);
        listening.setAdLoader(adload);
    }
}

