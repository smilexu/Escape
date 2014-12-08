package com.smilestudio.wizardescape;

import java.io.File;
import java.util.List;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
    private static final long MIN_SCREEN_AD_TIME = 1000 * 60 * 3;
    private static String mUID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUID = ((TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();

        mLastAdTime = System.currentTimeMillis();

        Bmob.initialize(this, "d8ced21016fab437a633e20d92f9dd9b");

        mContainer = new RelativeLayout(this);

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
        if (!GameConfig.SHOW_INTERSTITIAL_AD) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - mLastAdTime) > MIN_SCREEN_AD_TIME) {
            mLastAdTime = currentTime;
            mAdDelegate.showInterstitialAd(getApplicationContext());
        }
    }

    @Override
    public void onWeiboShare(String path, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        ComponentName component = new ComponentName("com.sina.weibo", "com.sina.weibo.EditActivity");
        intent.setComponent(component);
        File file = new File(path);
        if (file.exists()) {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.shared_weibo_no_pic), Toast.LENGTH_LONG).show();
                }});
        }
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

        startActivity(intent);
    }

    @Override
    public void onWeixinShare(String path, final String content) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");

        File file = new File(path);
        if (file.exists()) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.shared_weixin_no_pic), Toast.LENGTH_LONG).show();
                }});
            return;
        }
        intent.putExtra(Intent.EXTRA_TEXT,content);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Copied", content));
                Toast.makeText(MainActivity.this, getResources().getString(R.string.shared_content_copied), Toast.LENGTH_LONG).show();
            }});


        startActivity(intent);
    }

    @Override
    public void onGameFullStarCompleted(final String mission, final int steps) {
        if (null == mUID) {
            return;
        }

        //check if have record already
        BmobQuery<GameScoreData> bmobQuery = new BmobQuery<GameScoreData>();
        bmobQuery.addWhereEqualTo("uid", mUID);
        bmobQuery.addWhereEqualTo("mission", mission);
        bmobQuery.findObjects(this, new FindListener<GameScoreData>() {

            @Override
            public void onSuccess(List<GameScoreData> list) {
                System.out.println("============ size : " + list.size());
                GameScoreData data = list.get(0);
                data.setSteps(steps);
                data.save(MainActivity.this, new SaveListener() {
                    
                    @Override
                    public void onSuccess() {
                        System.out.println("=========== save sucess");
                    }
                    
                    @Override
                    public void onFailure(int code, String msg) {
                        System.out.println("=========== save failed : " + code);
                    }
                });
            }
            
            @Override
            public void onError(int code, String msg) {
                System.out.println("=========== code : " + code);
                GameScoreData data = new GameScoreData(mUID, mission, steps);
                data.save(MainActivity.this, new SaveListener() {

                    @Override
                    public void onFailure(int code, String msg) {
                        System.out.println("======== no record found, insert new one failed : " + code);
                    }

                    @Override
                    public void onSuccess() {
                        System.out.println("======== no record found, insert new one sucsss");
                    }});
            }
        });


    }

}