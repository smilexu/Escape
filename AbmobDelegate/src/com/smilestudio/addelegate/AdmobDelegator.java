package com.smilestudio.addelegate;

import com.google.android.gms.ads.*;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AdmobDelegator implements AdInterface {

    private static final String AD_BOTTOM_BANNER_ID = "ca-app-pub-5445005041859471/6919471748";
    private static final String AD_INTERSTITIAL_ID = "ca-app-pub-5445005041859471/5442738548";
    private static final String AD_UPPER_BANNER_ID = "ca-app-pub-5445005041859471/4466716143";
    private AdView mBottomBannerView;
    private AdView mUpperBannerView;
    private Activity mActivity;
    private InterstitialAd mInterstitialAd;
    private static final String BOTTOM_BANNER_TAG = "bottom_banner_tag";
    private static final String UPPER_BANNER_TAG = "upper_banner_tag";
    private static final String DEVICE_ID = "FEFB4FBC0A2A97532F1684B2D7064AAE";
    private static final boolean sShowTopBanner = true;
    private static final boolean sShowBottomBanner = false;

    @Override
    public void init(Activity activity) {
        mActivity = activity;

        mUpperBannerView = new AdView(activity);
        mUpperBannerView.setAdUnitId(AD_UPPER_BANNER_ID);
        mUpperBannerView.setAdSize(AdSize.BANNER);
        mUpperBannerView.setTag(UPPER_BANNER_TAG);

        mBottomBannerView = new AdView(activity);
        mBottomBannerView.setAdUnitId(AD_BOTTOM_BANNER_ID);
        mBottomBannerView.setAdSize(AdSize.BANNER);
        mBottomBannerView.setTag(BOTTOM_BANNER_TAG);

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
    public void showTopBanner(final boolean show, final RelativeLayout parentView,
            final LayoutParams params) {

        if (!sShowTopBanner) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (show) {
                    if (null == parentView.findViewWithTag(UPPER_BANNER_TAG)) {
                        parentView.addView(mUpperBannerView, params);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mUpperBannerView.loadAd(adRequest);
                    }
                } else {
                    parentView.removeView(mUpperBannerView);
                }
            }});
    }

    @Override
    public void showButtomBanner(final boolean show, final RelativeLayout parentView,
            final LayoutParams params) {

        if (!sShowBottomBanner) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (show) {
                    if (null == parentView.findViewWithTag(BOTTOM_BANNER_TAG)) {
                        parentView.addView(mBottomBannerView, params);
//                        AdRequest adRequest = new AdRequest.Builder().addTestDevice(DEVICE_ID).build();
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mBottomBannerView.loadAd(adRequest);
                    }
                } else {
                    parentView.removeView(mBottomBannerView);
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
        if (mBottomBannerView != null) {
            mBottomBannerView.destroy();
        }
    }

}
