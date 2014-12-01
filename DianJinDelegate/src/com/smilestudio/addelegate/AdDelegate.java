package com.smilestudio.addelegate;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AdDelegate implements AdInterface {

    private DianjinDelegator mAdDelegator;

    public AdDelegate() {
        mAdDelegator = new DianjinDelegator();
    }

    @Override
    public void init(Activity activity) {
        mAdDelegator.init(activity);
    }

    @Override
    public void showTopBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {
        if (show && (null == parentView || null == params)) {
            throw new IllegalArgumentException("if show is true, parentView and params MUST NOT be null");
        }
        mAdDelegator.showTopBanner(show, parentView, params);
    }

    @Override
    public void showButtomBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {
        if (show && (null == parentView || null == params)) {
            throw new IllegalArgumentException("if show is true, parentView and params MUST NOT be null");
        }
        mAdDelegator.showButtomBanner(show, parentView, params);
    }

    @Override
    public void showInterstitialAd(Context context) {
        mAdDelegator.showInterstitialAd(context);
    }

    @Override
    public void dispose() {
        mAdDelegator.dispose();
    }

}
