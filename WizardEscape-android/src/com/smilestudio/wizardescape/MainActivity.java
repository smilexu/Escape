package com.smilestudio.wizardescape;

import java.io.File;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.smilestudio.addelegate.AdDelegate;
import com.smilestudio.wizardescape.utils.GameConfig;
import com.umeng.analytics.game.UMGameAgent;

public class MainActivity extends AndroidApplication implements AnalyticsListener, AdListener, GeneralListener{
    private RelativeLayout mContainer;
    private LayoutParams mBannerParams;
    private LayoutParams mMiniBannerParams;
    private long mLastAdTime;
    private AdDelegate mAdDelegate;
    private static final long MIN_SCREEN_AD_TIME = 1000 * 60 * 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContainer = new RelativeLayout(this);
        mLastAdTime = System.currentTimeMillis();

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //init Umeng
        if (GameConfig.DEBUG) {
            UMGameAgent.setDebugMode(true);
        }
        UMGameAgent.init( this );

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        WizardEscape escape = new WizardEscape();
        escape.setAnalyticsListener(this);
        escape.setAdListener(this);
        escape.setGeneralListener(this);

        // Create the libgdx View
        View gameView = initializeForView(escape, cfg);
        mContainer.addView(gameView);

        mBannerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
            mBannerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        mMiniBannerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mMiniBannerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMiniBannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mAdDelegate = new AdDelegate();
        mAdDelegate.init(this);

        setContentView(mContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAdDelegate.dispose();
    }

    @Override
    public void startMission(String mission) {
        UMGameAgent.startLevel(mission);
    }

    @Override
    public void failMission(String mission) {
        UMGameAgent.failLevel(mission);
    }

    @Override
    public void finishMission(String mission) {
        UMGameAgent.finishLevel(mission);
    }

    @Override
    public void use(String mission, int steps) {
        UMGameAgent.use(mission, steps, 0);
    }

    @Override
    public void showBanner(final boolean show) {
        if (!GameConfig.SHOW_BANNER_AD) {
            return;
        }

        mAdDelegate.showButtomBanner(show, mContainer, mBannerParams);

    }

    @Override
    public void showMiniBanner(final boolean show) {
        if (!GameConfig.SHOW_BANNER_AD) {
            return;
        }

        mAdDelegate.showTopBanner(show, mContainer, mMiniBannerParams);
    }

    @Override
    public void showScreenAd() {
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - mLastAdTime) > MIN_SCREEN_AD_TIME) {
            mLastAdTime = currentTime;
            System.out.println("======== showScreenAd");
            mAdDelegate.showInterstitialAd(getApplicationContext());
        }
    }

    @Override
    public void onWeiboShare(String path, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        ComponentName component = new ComponentName("com.sina.weibo", "com.sina.weibo.EditActivity");
        intent.setComponent(component);
        File file = new File(path);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "恭喜过关");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        Intent.createChooser(intent,"分享你的胜利");

        startActivity(intent);
    }

    @Override
    public void onWeixinShare(String path, final String content) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT,content);
        File file = new File(path);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Copied", content));

                Toast.makeText(MainActivity.this, "内容已复制，请长按输入框选择粘贴即可", Toast.LENGTH_LONG).show();
            }});


        startActivity(intent);
    }

}