package com.smilestudio.wizardescape;

import java.io.File;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bodong.dianjinweb.DianJinPlatform;
import com.smilestudio.wizardescape.utils.GameConfig;
import com.smilestudio.wizardescape.utils.ScreenshotFactory;
import com.umeng.analytics.game.UMGameAgent;

public class MainActivity extends AndroidApplication implements AnalyticsListener, AdListener, GeneralListener{
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        //init Umeng
        if (GameConfig.DEBUG) {
            UMGameAgent.setDebugMode(true);
        }
        UMGameAgent.init( this );

        //init ad
        DianJinPlatform.initialize(this, 61609, "8839a8811fca305d9a9e281493f8d042", 1001);
        DianJinPlatform.hideFloatView(this);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        WizardEscape escape = new WizardEscape();
        escape.setAnalyticsListener(this);
        escape.setAdListener(this);
        escape.setGeneralListener(this);
        initialize(escape, cfg);
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

        DianJinPlatform.destory(this);
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
    public void showAdWall() {
        DianJinPlatform.showOfferWall(this);
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

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Copied", content));

                Toast.makeText(MainActivity.this, "内容已复制，请长按输入框选择粘贴即可", Toast.LENGTH_LONG).show();
            }});


        startActivity(intent);
    }
}