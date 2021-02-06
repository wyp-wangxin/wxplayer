package com.wyp.android.wxvideoplayer.player;


import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wyp.android.wxvideoplayer.R;
import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.util.NiceUtil;

import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by Administrator on 2021/2/3.
 */

public class WxPlayerController extends FrameLayout implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView mImage;
    private ImageView mCenterStart;

    private LinearLayout mTop;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mBatteryTime;
    private ImageView mBattery;
    private TextView mTime;

    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private TextView mClarity;
    private ImageView mFullScreen;

    private TextView mLength;

    private LinearLayout mLoading;
    private TextView mLoadText;

    private LinearLayout mChangePositon;
    private TextView mChangePositionCurrent;
    private ProgressBar mChangePositionProgress;

    private LinearLayout mChangeBrightness;
    private ProgressBar mChangeBrightnessProgress;

    private LinearLayout mChangeVolume;
    private ProgressBar mChangeVolumeProgress;

    private LinearLayout mError;
    private TextView mRetry;

    private LinearLayout mCompleted;
    private TextView mReplay;
    private TextView mShare;

    private Context mContext;
    private CountDownTimer mDismissTopBottomCountDownTimer;
    private boolean topBottomVisible;

    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;

    private WxVideoPlayer mWxVideoPlayer;
    private static final  int MSG_STATE_IDLE=0;
    private static final int MSG_STATE_PREPARING=1;
    private static final int MSG_STATE_PREPARED =2;
    private static final int MSG_STATE_PLAYING=3;
    private static final int MSG_STATE_PAUSED=4;
    private static final int MSG_STATE_BUFFERING_PLAYING=5;
    private static final int MSG_STATE_BUFFERING_PAUSED=6;
    private static final int MSG_STATE_COMPLETED=7;
    private static final int MSG_STATE_ERROR=8;
    private static final int MSG_PLAYER_NORMAL=9;
    private static final int MSG_PLAYER_FULL_SCREEN=10;
    private static final int MSG_PLAYER_TINY_WINDOW=11;
    private static final int MSG_RESET=12;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_STATE_IDLE:
                    break;
                case MSG_STATE_PREPARING:
                    // 只显示准备中动画，其他不显示
                    mImage.setVisibility(View.GONE);
                    mLoading.setVisibility(View.VISIBLE);
                    mLoadText.setText("正在准备...");
                    mError.setVisibility(View.GONE);
                    mCompleted.setVisibility(View.GONE);
                    mTop.setVisibility(View.GONE);
                    mCenterStart.setVisibility(View.GONE);
                    break;
                case MSG_STATE_PREPARED:
                    startUpdateProgressTimer();
                    break;
                case MSG_STATE_PLAYING:
                    mLoading.setVisibility(View.GONE);
                    mRestartPause.setImageResource(R.drawable.ic_player_pause);
                    startDismissTopBottomTimer();
                    break;
                case MSG_STATE_PAUSED:
                    mLoading.setVisibility(View.GONE);
                    mRestartPause.setImageResource(R.drawable.ic_player_start);
                    cancelDismissTopBottomTimer();
                    break;
                case MSG_STATE_BUFFERING_PLAYING:
                    mLoading.setVisibility(View.VISIBLE);
                    mRestartPause.setImageResource(R.drawable.ic_player_pause);
                    mLoadText.setText("正在缓冲...");
                    startDismissTopBottomTimer();
                    break;
                case MSG_STATE_BUFFERING_PAUSED:
                    mLoading.setVisibility(View.VISIBLE);
                    mRestartPause.setImageResource(R.drawable.ic_player_start);
                    mLoadText.setText("正在缓冲...");
                    cancelDismissTopBottomTimer();
                    break;
                case MSG_STATE_COMPLETED:
                    cancelUpdateProgressTimer();
                    setTopBottomVisible(false);
                    mImage.setVisibility(View.VISIBLE);
                    mCompleted.setVisibility(View.VISIBLE);
                    if (mWxVideoPlayer.isFullScreen()) {
                        mWxVideoPlayer.exitFullScreen();
                    }
                    if (mWxVideoPlayer.isTinyWindow()) {
                        mWxVideoPlayer.exitTinyWindow();
                    }
                    break;
                case MSG_STATE_ERROR:
                    cancelUpdateProgressTimer();
                    setTopBottomVisible(false);
                    mTop.setVisibility(View.VISIBLE);
                    mError.setVisibility(View.VISIBLE);
                    break;
                case MSG_PLAYER_NORMAL:
                    mBack.setVisibility(View.GONE);
                    mFullScreen.setVisibility(View.VISIBLE);
                    mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
                    break;
                case MSG_PLAYER_FULL_SCREEN:
                    mBack.setVisibility(View.VISIBLE);
                    mFullScreen.setVisibility(View.VISIBLE);
                    mFullScreen.setImageResource(R.drawable.ic_player_shrink);
                    break;
                case MSG_PLAYER_TINY_WINDOW:
                    mFullScreen.setVisibility(View.GONE);
                    break;
                case MSG_RESET:
                    topBottomVisible = false;
                    cancelUpdateProgressTimer();
                    cancelDismissTopBottomTimer();
                    mSeek.setProgress(0);
                    mSeek.setSecondaryProgress(0);

                    mCenterStart.setVisibility(View.VISIBLE);
                    mImage.setVisibility(View.VISIBLE);

                    mBottom.setVisibility(View.GONE);
                    mFullScreen.setImageResource(R.drawable.ic_player_enlarge);

                    mTop.setVisibility(View.VISIBLE);
                    mBack.setVisibility(View.GONE);

                    mLoading.setVisibility(View.GONE);
                    mError.setVisibility(View.GONE);
                    mCompleted.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public WxPlayerController(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tx_video_palyer_controller, WxPlayerController.this, true);

        mCenterStart =  view.findViewById(R.id.center_start);
        mImage = (ImageView) view.findViewById(R.id.image);

        mTop = (LinearLayout) view.findViewById(R.id.top);
        mBack = (ImageView) view.findViewById(R.id.back);
        mTitle = (TextView) view.findViewById(R.id.title);
        mBatteryTime = (LinearLayout) view.findViewById(R.id.battery_time);
        mBattery = (ImageView) view.findViewById(R.id.battery);
        mTime = (TextView) view.findViewById(R.id.time);

        mBottom = (LinearLayout) view.findViewById(R.id.bottom);
        mRestartPause = (ImageView) view.findViewById(R.id.restart_or_pause);
        mPosition = (TextView) view.findViewById(R.id.position);
        mDuration = (TextView) view.findViewById(R.id.duration);
        mSeek = (SeekBar) view.findViewById(R.id.seek);
        mFullScreen = (ImageView) view.findViewById(R.id.full_screen);
        mClarity = (TextView) view.findViewById(R.id.clarity);
        mLength = (TextView) view.findViewById(R.id.length);

        mLoading = (LinearLayout) view.findViewById(R.id.loading);
        mLoadText = (TextView) view.findViewById(R.id.load_text);

        mChangePositon = (LinearLayout) view.findViewById(R.id.change_position);
        mChangePositionCurrent = (TextView) view.findViewById(R.id.change_position_current);
        mChangePositionProgress = (ProgressBar) view.findViewById(R.id.change_position_progress);

        mChangeBrightness = (LinearLayout) view.findViewById(R.id.change_brightness);
        mChangeBrightnessProgress = (ProgressBar) view.findViewById(R.id.change_brightness_progress);

        mChangeVolume = (LinearLayout) view.findViewById(R.id.change_volume);
        mChangeVolumeProgress = (ProgressBar) view.findViewById(R.id.change_volume_progress);

        mError = (LinearLayout) view.findViewById(R.id.error);
        mRetry = (TextView) view.findViewById(R.id.retry);

        mCompleted = (LinearLayout) view.findViewById(R.id.completed);
        mReplay = (TextView) view.findViewById(R.id.replay);
        mShare = (TextView) view.findViewById(R.id.share);

        mCenterStart.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRestartPause.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mClarity.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
    }


    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public ImageView imageView() {
        return mImage;
    }


    public void setImage(String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(mImage);
    }

    public void setImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    public void setLenght(int length) {
         mLength.setText(NiceUtil.formatTime(length));
    }


    public void setNiceVideoPlayer(WxVideoPlayer niceVideoPlayer) {
        mWxVideoPlayer = niceVideoPlayer;
        if (mWxVideoPlayer.isIdle()) {
            mBack.setVisibility(View.GONE);
            mTop.setVisibility(View.VISIBLE);
            mBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private void setTopBottomVisible(boolean visible) {
        mTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        topBottomVisible = visible;
        if (visible) {
            if (!mWxVideoPlayer.isPaused() && !mWxVideoPlayer.isBufferingPaused()) {
                startDismissTopBottomTimer();
            }
        } else {
            cancelDismissTopBottomTimer();
        }
    }
    public void setControllerState(int playerState, int playState) {
        switch (playerState) {
            case WxVideoPlayer.PLAYER_NORMAL:
                mHandler.sendEmptyMessage(MSG_PLAYER_NORMAL);
                break;
            case WxVideoPlayer.PLAYER_FULL_SCREEN:
                mHandler.sendEmptyMessage(MSG_PLAYER_FULL_SCREEN);
                break;
            case WxVideoPlayer.PLAYER_TINY_WINDOW:
                mHandler.sendEmptyMessage(MSG_PLAYER_TINY_WINDOW);
                break;
        }
        switch (playState) {
            case WxVideoPlayer.STATE_IDLE:
                break;
            case WxVideoPlayer.STATE_PREPARING:
                mHandler.sendEmptyMessage(MSG_STATE_PREPARING);
                break;
            case WxVideoPlayer.STATE_PREPARED:
                mHandler.sendEmptyMessage(MSG_STATE_PREPARED);
                break;
            case WxVideoPlayer.STATE_PLAYING:
                mHandler.sendEmptyMessage(MSG_STATE_PLAYING);
                break;
            case WxVideoPlayer.STATE_PAUSED:
                mHandler.sendEmptyMessage(MSG_STATE_PAUSED);
                break;
            case WxVideoPlayer.STATE_BUFFERING_PLAYING:
                mHandler.sendEmptyMessage(MSG_STATE_BUFFERING_PLAYING);
                break;
            case WxVideoPlayer.STATE_BUFFERING_PAUSED:
                mHandler.sendEmptyMessage(MSG_STATE_BUFFERING_PAUSED);
            case WxVideoPlayer.STATE_COMPLETED:
                mHandler.sendEmptyMessage(MSG_STATE_COMPLETED);
                break;
            case WxVideoPlayer.STATE_ERROR:
                mHandler.sendEmptyMessage(MSG_STATE_ERROR);
                break;
        }
    }


    private void startUpdateProgressTimer() {
        cancelUpdateProgressTimer();
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = new Timer();
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    WxPlayerController.this.post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();
                        }
                    });
                }
            };
        }
        mUpdateProgressTimer.schedule(mUpdateProgressTimerTask, 0, 300);
    }

    private void updateProgress() {
        int position = mWxVideoPlayer.getCurrentPosition();
        int duration = mWxVideoPlayer.getDuration();
        int bufferPercentage = mWxVideoPlayer.getBufferPercentage();
        mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        mSeek.setProgress(progress);
        mPosition.setText(NiceUtil.formatTime(position));
        mDuration.setText(NiceUtil.formatTime(duration));
    }

    /**
     * 取消更新进度的计时器。
     */
    protected void cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }


    /**
     * 开启top、bottom自动消失的timer
     */
    private void startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer();
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    setTopBottomVisible(false);
                }
            };
        }
        mDismissTopBottomCountDownTimer.start();
    }

    /**
     * 取消top、bottom自动消失的timer
     */
    private void cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer.cancel();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelDismissTopBottomTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mWxVideoPlayer.isBufferingPaused() || mWxVideoPlayer.isPaused()) {
            mWxVideoPlayer.restart();
        }
        int position = (int) (mWxVideoPlayer.getDuration() * seekBar.getProgress() / 100f);
        mWxVideoPlayer.seekTo(position);
        startDismissTopBottomTimer();
    }

    /**
     * 控制器恢复到初始状态
     */
    public void reset() {
        mHandler.sendEmptyMessage(MSG_RESET);

    }
//
//    /**
//     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
//     * UI相关的逻辑都尽量到{@link #onPlayStateChanged}和{@link #onPlayModeChanged}中处理.
//     */
    @Override
    public void onClick(View v) {
        if (v == mCenterStart) {
            if (mWxVideoPlayer.isIdle()) {
                MyLog.d("mCenterStart mWxVideoPlayer.isIdle");
                mWxVideoPlayer.start();
            }
        } else if (v == mBack) {
            if (mWxVideoPlayer.isFullScreen()) {
                mWxVideoPlayer.exitFullScreen();
            } else if (mWxVideoPlayer.isTinyWindow()) {
                mWxVideoPlayer.exitTinyWindow();
            }
        } else if (v == mRestartPause) {
            if (mWxVideoPlayer.isPlaying() || mWxVideoPlayer.isBufferingPlaying()) {
                MyLog.d("mRestartPause mWxVideoPlayer.isPlaying()");
                mWxVideoPlayer.pause();
            } else if (mWxVideoPlayer.isPaused() || mWxVideoPlayer.isBufferingPaused()) {
                MyLog.d("mRestartPause mWxVideoPlayer.isPlaying()");
                mWxVideoPlayer.restart();
            }
        } else if (v == mFullScreen) {
            if (mWxVideoPlayer.isNormal()) {
                mWxVideoPlayer.enterFullScreen();
            } else if (mWxVideoPlayer.isFullScreen()) {
                mWxVideoPlayer.exitFullScreen();
            }
        } else if (v == mRetry) {
            mWxVideoPlayer.stopAndrelease();
            mWxVideoPlayer.start();
        } else if (v == mReplay) {
            mRetry.performClick();
        } else if (v == mShare) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
        } else if (v == this) {
            if (mWxVideoPlayer.isPlaying()
                    || mWxVideoPlayer.isPaused()
                    || mWxVideoPlayer.isBufferingPlaying()
                    || mWxVideoPlayer.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible);
            }
        }
    }





//    /**
//     * 开启top、bottom自动消失的timer
//     */
//    private void startDismissTopBottomTimer() {
//        cancelDismissTopBottomTimer();
//        if (mDismissTopBottomCountDownTimer == null) {
//            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                    setTopBottomVisible(false);
//                }
//            };
//        }
//        mDismissTopBottomCountDownTimer.start();
//    }
//
//    /**
//     * 取消top、bottom自动消失的timer
//     */
//    private void cancelDismissTopBottomTimer() {
//        if (mDismissTopBottomCountDownTimer != null) {
//            mDismissTopBottomCountDownTimer.cancel();
//        }
//    }
//
//
//
//    protected void hideChangePosition() {
//        mChangePositon.setVisibility(View.GONE);
//    }
//
//    protected void showChangeVolume(int newVolumeProgress) {
//        mChangeVolume.setVisibility(View.VISIBLE);
//        mChangeVolumeProgress.setProgress(newVolumeProgress);
//    }
//
//
//    protected void hideChangeVolume() {
//        mChangeVolume.setVisibility(View.GONE);
//    }
//
//
//    protected void showChangeBrightness(int newBrightnessProgress) {
//        mChangeBrightness.setVisibility(View.VISIBLE);
//        mChangeBrightnessProgress.setProgress(newBrightnessProgress);
//    }
//
//
//    protected void hideChangeBrightness() {
//        mChangeBrightness.setVisibility(View.GONE);
//    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

}
