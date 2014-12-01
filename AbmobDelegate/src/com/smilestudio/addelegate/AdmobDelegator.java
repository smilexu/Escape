package com.smilestudio.addelegate;

import com.google.android.gms.ads.*;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AdmobDelegator implements AdInterface {

    private static final String AD_BANNER_ID = "ca-app-pub-5445005041859471/6919471748";
    private static final String AD_INTERSTITIAL_ID = "ca-app-pub-5445005041859471/5442738548";
    private AdView mBannerView;
    private Activity mActivity;
    private InterstitialAd mInterstitialAd;
    private static final String BANNER_TAG = "banner_tag";
    private static final String DEVICE_ID = "FEFB4FBC0A2A97532F1684B2D7064AAE";

    @Override
    public void init(Activity activity) {
        mActivity = activity;
        mBannerView = new AdView(activity);
        mBannerView.setAdUnitId(AD_BANNER_ID);
        mBannerView.setAdSize(AdSize.BANNER);

        mInterstitialAd = new InterstitialAd(mActivity);
        mInterstitialAd.setAdUnitId(AD_INTERSTITIAL_ID);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(DEVICE_ID).build();
        final AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                mInterstitialAd.loadAd(adRequest);
            }
        });
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void showTopBanner(boolean show, RelativeLayout parentView,
            LayoutParams params) {

    }

    @Override
    public void showButtomBanner(final boolean show, final RelativeLayout parentView,
            final LayoutParams params) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (show) {
                    if (null == parentView.findViewWithTag(BANNER_TAG)) {
                        parentView.addView(mBannerView, params);
//                        AdRequest adRequest = new AdRequest.Builder().addTestDevice(DEVICE_ID).build();
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mBannerView.loadAd(adRequest);
                    }
                } else {
                    parentView.removeView(mBannerView);
                }
            }});
    }

    @Override
    public void showInterstitialAd(Context context) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }});

    }

    @Override
    public void dispose() {
        if (mBannerView != null) {
            mBannerView.destroy();
        }
    }

}
