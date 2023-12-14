package com.leestream.nativeinrcview;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.NativeAd;

public abstract class AdNativeListening extends AdListener implements NativeAd.OnNativeAdLoadedListener
{
    private AdLoader adLoader;

    public void setAdLoader(AdLoader adLoader)
    {
        this.adLoader = adLoader;
    }
}
