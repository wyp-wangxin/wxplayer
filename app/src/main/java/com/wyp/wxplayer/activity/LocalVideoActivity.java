package com.wyp.wxplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.wyp.android.wxvideoplayer.player.WxPlayerController;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayer;
import com.wyp.wxplayer.R;

public class LocalVideoActivity extends AppCompatActivity {
    private String pathurl;
    private WxVideoPlayer mWxVideoPlayer;
    private WxPlayerController mWxPlayerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_local_video);

        mWxVideoPlayer = (WxVideoPlayer) findViewById(R.id.nice_video_player);
        mWxPlayerController = new WxPlayerController(getApplicationContext());
        mWxVideoPlayer.setController(mWxPlayerController);

        pathurl = getIntent().getExtras().getString("url");
        mWxVideoPlayer.setUp(pathurl,null);
        mWxVideoPlayer.start();
    }
}
