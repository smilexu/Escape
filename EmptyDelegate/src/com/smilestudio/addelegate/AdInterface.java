package com.smilestudio.addelegate;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public interface AdInterface {

    /**
     * init Ad resource
     * 
     * @param activity
     */
    public void init(Activity activity);

    /**
     * show or hide banner view.
     * 
     * @param show Show or hide banner
     * @param parentView Parent view, if show is true, parent view MUST not be null
     * @param params View params, if show is true, parmas MUST not be null
     */
    public void showTopBanner(boolean show, RelativeLayout parentView, LayoutParams params);

    /**
     * show or hide banner view.
     * 
     * @param show Show or hide banner
     * @param parentView Parent view, if show is true, parent view MUST not be null
     * @param params View params, if show is true, parmas MUST not be null
     */
    public void showButtomBanner(boolean show, RelativeLayout parentView, LayoutParams params);

    /**
     * show or hide instance screen ad
     * 
     * @param context Application context
     */
    public void showInterstitialAd(Context context);

    public void dispose();
}
