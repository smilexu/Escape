package com.smilestudio.wizardescape;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.wandoujia.ads.sdk.AdListener;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.InterstitialAd;
import com.wandoujia.ads.sdk.loader.Fetcher;

public class MainActivity extends AndroidApplication {
    private static final String AD_TAG_APPWALL_GAME = "8846790fb06b5595307f7bd1a823cdb3";
    private static final String AD_TAG_INTERSTITIAL = "991f667a53574f5f60ff73aaedd68c48";
    private static final String ADS_APP_ID = "100015729";
    private static final String ADS_SECRET_KEY = "a260992bb6185e30c1b6d3073929c1c4";
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        //initial ad
        initialAd();

        final InterstitialAd interstitialAd = new InterstitialAd(this, AD_TAG_INTERSTITIAL);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdReady() {
              interstitialAd.show();
            }

            @Override
            public void onLoadFailure() {

            }

            @Override
            public void onAdPresent() {
            }

            @Override
            public void onAdDismiss() {
            }
          });

        AdCallback listener = new AdCallback() {

            @Override
            public void loadInterstitialAd() {
                if (!interstitialAd.isLoaded()) {
                    return;
                }

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        interstitialAd.load();
                    }});

            }

            @Override
            public void loadAppWallAd() {
                Ads.showAppWall(MainActivity.this, AD_TAG_APPWALL_GAME);
            }
        };

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        WizardEscape game = new WizardEscape(listener);
        initialize(game, cfg);
    }

    private void initialAd() {
        try {
            Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          Ads.preLoad(this, Fetcher.AdFormat.appwall, "GAME", AD_TAG_APPWALL_GAME, new AdListener() {

            @Override
            public void onAdReady() {
                System.out.println("================== onAdReady : " + Ads.getUpdateAdCount("GAME"));
              if (Ads.getUpdateAdCount("GAME") > 0) {
                  Toast.makeText(MainActivity.this, "game load completed", Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onLoadFailure() {
                System.out.println("================== onLoadFailure");
              Toast.makeText(MainActivity.this, "网络异常，广告加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdPresent() {

            }

            @Override
            public void onAdDismiss() {

            }
          });

          int test = 0;
    }
}