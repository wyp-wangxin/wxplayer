package com.wyp.android.wxvideoplayer.player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.wyp.android.wxvideoplayer.R;
import com.wyp.android.wxvideoplayer.WlTimeInfoBean;
import com.wyp.android.wxvideoplayer.listener.WxOnCompleteListener;
import com.wyp.android.wxvideoplayer.listener.WxOnErrorListener;
import com.wyp.android.wxvideoplayer.listener.WxOnLoadListener;
import com.wyp.android.wxvideoplayer.listener.WxOnParparedListener;
import com.wyp.android.wxvideoplayer.listener.WxOnPauseResumeListener;
import com.wyp.android.wxvideoplayer.listener.WxOnTimeInfoListener;
import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.opengl.WxGLSurfaceView;
import com.wyp.android.wxvideoplayer.util.LogUtil;
import com.wyp.android.wxvideoplayer.util.NiceUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wyp on 2021/2/3.
 */

public class WxVideoPlayer extends FrameLayout implements IWxVideoPlayer{

    public static final int STATE_ERROR = -1;          // 播放错误
    public static final int STATE_IDLE = 0;            // 播放未开始
    public static final int STATE_PREPARING = 1;       // 播放准备中
    public static final int STATE_PREPARED = 2;        // 播放准备就绪
    public static final int STATE_PLAYING = 3;         // 正在播放
    public static final int STATE_PAUSED = 4;          // 暂停播放
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     **/
    public static final int STATE_BUFFERING_PLAYING = 5;
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停)
     **/
    public static final int STATE_BUFFERING_PAUSED = 6;
    public static final int STATE_COMPLETED = 7;       // 播放完成

    public static final int PLAYER_NORMAL = 10;        // 普通播放器
    public static final int PLAYER_FULL_SCREEN = 11;   // 全屏播放器
    public static final int PLAYER_TINY_WINDOW = 12;   // 小窗口播放器

    private int mCurrentState = STATE_IDLE;
    private int mPlayerState = PLAYER_NORMAL;

    private FrameLayout mContainer;
    private Context mContext;
    private WxPlayerController mController;
    private String mUrl;
    private Map<String, String> mHeaders;
    private WxPlayer mWxPlayer;
    private WxGLSurfaceView mGLSurfaceView;
    private SurfaceTexture mSurfaceTexture;
    private int mBufferPercentage;
    private WlTimeInfoBean mWlTimeInfoBean;
    private WxOnCompleteListener mWxOnCompleteListener =new WxOnCompleteListener() {
        @Override
        public void onComplete() {

        }
    };

    private WxOnErrorListener mWxOnErrorListener = new WxOnErrorListener() {
        @Override
        public void onError(int code, String msg) {

        }
    };

    private WxOnLoadListener mWxOnLoadListener = new WxOnLoadListener() {
        @Override
        public void onLoad(Boolean bool) {

        }
    };

    private WxOnParparedListener mWxOnParparedListener = new WxOnParparedListener() {
        @Override
        public void onParpared() {
            MyLog.d("准备好了，可以开始播放视频了");
            mCurrentState = STATE_PREPARED;
            mController.setControllerState(mPlayerState, mCurrentState);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mWxPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.setControllerState(mPlayerState, mCurrentState);
        }
    };

    private WxOnPauseResumeListener mWxOnPauseResumeListener = new WxOnPauseResumeListener() {
        @Override
        public void onPause(boolean pause) {

        }
    };

    private WxOnTimeInfoListener mWxOnTimeInfoListener = new WxOnTimeInfoListener() {
        @Override
        public void onTimeInfo(WlTimeInfoBean timeInfoBean) {
            mWlTimeInfoBean=timeInfoBean;
        }
    };



    public WxVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, params);
    }

    public void setController(WxPlayerController controller) {
        mContainer.removeView(mController);
        mController = controller;
        mController.reset();
        mController.setNiceVideoPlayer(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController, params);
    }

    @Override
    public void setUp(String url, Map<String, String> headers) {
        mUrl = url;
        mHeaders = headers;
    }

    @Override
    public void start() {
        WxVideoPlayerManager.instance().releaseWxVideoPlayer();
        WxVideoPlayerManager.instance().setCurrentWxVideoPlayer(this);
        if (mCurrentState == STATE_IDLE
                || mCurrentState == STATE_ERROR
                || mCurrentState == STATE_COMPLETED) {
            initMediaPlayer();
            initTextureView();
            addTextureView();
            mWxPlayer.setSource(mUrl);
            mWxPlayer.parpared();
            mCurrentState = STATE_PREPARING;
            mController.setControllerState(mPlayerState, mCurrentState);
        }
    }

    @Override
    public void start(long position) {

    }

    @Override
    public void restart() {
        if (mCurrentState == STATE_PAUSED) {
            mWxPlayer.resume();
            mCurrentState = STATE_PLAYING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("STATE_PLAYING");
        }
      /*  if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("STATE_BUFFERING_PLAYING");
        }*/
    }

    @Override
    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mWxPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("STATE_PAUSED");
        }
       /* if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mWxPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("STATE_BUFFERING_PAUSED");
        }*/
    }

    @Override
    public void seekTo(int pos) {
       /* if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }*/
    }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void continueFromLastPosition(boolean continueFromLastPosition) {

    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }

    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }

    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public boolean isFullScreen() {
        return mPlayerState == PLAYER_FULL_SCREEN;
    }

    @Override
    public boolean isTinyWindow() {
        return mPlayerState == PLAYER_TINY_WINDOW;
    }

    @Override
    public boolean isNormal() {
        return mPlayerState == PLAYER_NORMAL;
    }

    @Override
    public int getMaxVolume() {
        return 0;
    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public int getDuration() {
        if(mWlTimeInfoBean==null)
            return 0;
        return mWlTimeInfoBean.getTotalTime();//mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        if(mWlTimeInfoBean==null)
            return 0;
        return mWlTimeInfoBean.getCurrentTime();//mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public int getBufferPercentage() {

        return mBufferPercentage;
    }

    @Override
    public float getSpeed(float speed) {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }


    /**
     * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
     * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期
     */
    @Override
    public void enterFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) return;
        //mGLSurfaceView.p
        // 隐藏ActionBar、状态栏，并横屏
        NiceUtil.scanForActivity(mContext)
               .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        NiceUtil.hideActionBar(mContext);

        this.removeView(mContainer);
        ViewGroup viewGroup = (ViewGroup)  this.getParent().getParent().getParent().getParent();
        viewGroup.removeAllViews();
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mContainer, params);


        mPlayerState = PLAYER_FULL_SCREEN;
        mController.setControllerState(mPlayerState, mCurrentState);
        LogUtil.d("PLAYER_FULL_SCREEN");
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    @Override
    public boolean exitFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) {
            NiceUtil.showActionBar(mContext);
            NiceUtil.scanForActivity(mContext)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mPlayerState = PLAYER_NORMAL;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("PLAYER_NORMAL");
            return true;
        }
        return false;
    }


    /**
     * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
     */
    @Override
    public void enterTinyWindow() {
        if (mPlayerState == PLAYER_TINY_WINDOW) return;
        this.removeView(mContainer);

        ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (NiceUtil.getScreenWidth(mContext) * 0.6f),
                (int) (NiceUtil.getScreenWidth(mContext) * 0.6f * 9f / 16f));
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.rightMargin = NiceUtil.dp2px(mContext, 8f);
        params.bottomMargin = NiceUtil.dp2px(mContext, 8f);

        contentView.addView(mContainer, params);

        mPlayerState = PLAYER_TINY_WINDOW;
        mController.setControllerState(mPlayerState, mCurrentState);
        LogUtil.d("PLAYER_TINY_WINDOW");
    }

    /**
     * 退出小窗口播放
     */
    @Override
    public boolean exitTinyWindow() {
        if (mPlayerState == PLAYER_TINY_WINDOW) {
            ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mPlayerState = PLAYER_NORMAL;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("PLAYER_NORMAL");
            return true;
        }
        return false;
    }

    @Override
    public void releasePlayer() {

    }

    @Override
    public void stopAndrelease() {

        if (mWxPlayer != null) {
            mWxPlayer.stop();
            mWxPlayer = null;
        }
        if(mGLSurfaceView!=null) {
            mContainer.removeView(mGLSurfaceView);
            mGLSurfaceView=null;
        }
        if (mController != null) {
            mController.reset();
        }
        mCurrentState = STATE_IDLE;
        mPlayerState = PLAYER_NORMAL;
        mController.setControllerState(mPlayerState, mCurrentState);
    }



    private void initMediaPlayer() {
        if (mWxPlayer == null) {
            mWxPlayer = new WxPlayer();
            String is =(mWxOnCompleteListener==null)?"true":"false";
            MyLog.d("mWxOnCompleteListener is null ? "+is);
            mWxPlayer.setWxOnCompleteListener(mWxOnCompleteListener);
            mWxPlayer.setWxOnErrorListener(mWxOnErrorListener);
            mWxPlayer.setWxOnLoadListener(mWxOnLoadListener);
            mWxPlayer.setWxOnParparedListener(mWxOnParparedListener);
            mWxPlayer.setWlOnPauseResumeListener(mWxOnPauseResumeListener);
            mWxPlayer.setWxOnTimeInfoListener(mWxOnTimeInfoListener);
        }
    }

    private void initTextureView() {
        if (mGLSurfaceView == null) {
            MyLog.d("mGLSurfaceView == null ");
            mGLSurfaceView = new WxGLSurfaceView(mContext);
            mWxPlayer.setWxGLSurfaceView(mGLSurfaceView);
        //mTextureView.setSurfaceTextureListener(this);
        }
    }

    private void addTextureView() {
        mContainer.removeView(mGLSurfaceView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mGLSurfaceView, 0, params);
    }

   /* private void openMediaPlayer() {
        try {
            mMediaPlayer.setDataSource(mContext.getApplicationContext(), Uri.parse(mUrl), mHeaders);
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtil.d("STATE_PREPARING");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("打开播放器发生错误", e);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }*/

   public void pauseGLSurfaceView(){
       mGLSurfaceView.onPause();
   }

   public void resumeGLSurfaceView(){
       mGLSurfaceView.onResume();
   }
}
