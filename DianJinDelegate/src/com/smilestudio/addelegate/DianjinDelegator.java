package com.smilestudio.addelegate;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bodong.dianjinweb.DianJinPlatform;
import com.bodong.dianjinweb.banner.DianJinBanner;
import com.bodong.dianjinweb.banner.DianJinMiniBanner;
import com.xyzstudio.wizardescape.Ddsx;

public class DianjinDelegator implements AdInterface {

    private Activity mActivity;
    private DianJinBanner mBanner;
    private DianJinMiniBanner mMiniBanner;
    private static final String BANNER_TAG = "banner_tag";
    private static final Object MINI_BANNER_TAG = "mini_banner_tag";

    @Override
    public void showTopBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {
        ((Activity)mActivity).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (show) {
                    if (null == parentView.findViewWithTag(MINI_BANNER_TAG)) {
                        parentView.addView(mMiniBanner, params);
                        mMiniBanner.startBanner();
                    }
                } else {
                    parentView.removeView(mMiniBanner);
                }
            }});
    }

    @Override
    public void showButtomBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {
        ((Activity)mActivity).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (show) {
                    if (null == parentView.findViewWithTag(BANNER_TAG)) {
                        parentView.addView(mBanner, params);
                        mBanner.startBanner();
                    }
                } else {
                    parentView.removeView(mBanner);
                }
            }});
    }

    @Override
    public void showInterstitialAd(Context context) {
        Ddsx spotManager = Ddsx.getInstance(context);
        spotManager.show(context, "f275be0f6ca5a9e2d8b1aa4be2ae4307", true, true, true);
    }

    @Override
    public void init(Activity activity) {
        mActivity = activity;
        //init ad
        DianJinPlatform.initialize(activity, 61609, "8839a8811fca305d9a9e281493f8d042", 1001);
        DianJinPlatform.hideFloatView(activity);

        mBanner = new DianJinBanner(activity);
        mBanner.setTag(BANNER_TAG);

        mMiniBanner = new DianJinMiniBanner(activity);
        mMiniBanner.setTag(MINI_BANNER_TAG);
    }

    @Override
    public void dispose() {
        DianJinPlatform.destory(mActivity);
    }

}
