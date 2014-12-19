package com.smilestudio.addelegate;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.xyzstudio.wizardescape.Ddsx;

public class EmptyDelegate implements AdInterface {

    @Override
    public void showTopBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {

    }

    @Override
    public void showButtomBanner(final boolean show, final RelativeLayout parentView, final LayoutParams params) {

    }

    @Override
    public void showInterstitialAd(Context context) {
        Ddsx spotManager = Ddsx.getInstance(context);
        spotManager.show(context, "f275be0f6ca5a9e2d8b1aa4be2ae4307", true, false, false);
    }

    @Override
    public void init(Activity activity) {

    }

    @Override
    public void dispose() {

    }

}
