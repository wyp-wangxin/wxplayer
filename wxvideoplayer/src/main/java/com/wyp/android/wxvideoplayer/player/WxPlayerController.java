package com.wyp.android.wxvideoplayer.player;


import android.content.Context;
import android.os.CountDownTimer;
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

import com.wyp.android.wxvideoplayer.R;

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

    public WxPlayerController(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tx_video_palyer_controller, WxPlayerController.this, false);

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

    public void setNiceVideoPlayer(WxVideoPlayer niceVideoPlayer) {
        mWxVideoPlayer = niceVideoPlayer;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public ImageView imageView() {
        return mImage;
    }


    public void setImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    public void setLenght(long length) {
        // mLength.setText(NiceUtil.formatTime(length));
    }


//    public void setNiceVideoPlayer(INiceVideoPlayer niceVideoPlayer) {
//        super.setNiceVideoPlayer(niceVideoPlayer);
//        // 给播放器配置视频链接地址
//        if (clarities != null && clarities.size() > 1) {
//            mNiceVideoPlayer.setUp(clarities.get(defaultClarityIndex).videoUrl, null);
//        }
//    }


//    protected void onPlayStateChanged(int playState) {
//        switch (playState) {
//            case NiceVideoPlayer.STATE_IDLE:
//                break;
//            case NiceVideoPlayer.STATE_PREPARING:
//                mImage.setVisibility(View.GONE);
//                mLoading.setVisibility(View.VISIBLE);
//                mLoadText.setText("正在准备...");
//                mError.setVisibility(View.GONE);
//                mCompleted.setVisibility(View.GONE);
//                mTop.setVisibility(View.GONE);
//                mBottom.setVisibility(View.GONE);
//                mCenterStart.setVisibility(View.GONE);
//                mLength.setVisibility(View.GONE);
//                break;
//            case NiceVideoPlayer.STATE_PREPARED:
//                startUpdateProgressTimer();
//                break;
//            case NiceVideoPlayer.STATE_PLAYING:
//                mLoading.setVisibility(View.GONE);
//                mRestartPause.setImageResource(R.drawable.ic_player_pause);
//                startDismissTopBottomTimer();
//                break;
//            case NiceVideoPlayer.STATE_PAUSED:
//                mLoading.setVisibility(View.GONE);
//                mRestartPause.setImageResource(R.drawable.ic_player_start);
//                cancelDismissTopBottomTimer();
//                break;
//            case NiceVideoPlayer.STATE_BUFFERING_PLAYING:
//                mLoading.setVisibility(View.VISIBLE);
//                mRestartPause.setImageResource(R.drawable.ic_player_pause);
//                mLoadText.setText("正在缓冲...");
//                startDismissTopBottomTimer();
//                break;
//            case NiceVideoPlayer.STATE_BUFFERING_PAUSED:
//                mLoading.setVisibility(View.VISIBLE);
//                mRestartPause.setImageResource(R.drawable.ic_player_start);
//                mLoadText.setText("正在缓冲...");
//                cancelDismissTopBottomTimer();
//                break;
//            case NiceVideoPlayer.STATE_ERROR:
//                cancelUpdateProgressTimer();
//                setTopBottomVisible(false);
//                mTop.setVisibility(View.VISIBLE);
//                mError.setVisibility(View.VISIBLE);
//                break;
//            case NiceVideoPlayer.STATE_COMPLETED:
//                cancelUpdateProgressTimer();
//                setTopBottomVisible(false);
//                mImage.setVisibility(View.VISIBLE);
//                mCompleted.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    protected void onPlayModeChanged(int playMode) {
//        switch (playMode) {
//            case NiceVideoPlayer.MODE_NORMAL:
//                mBack.setVisibility(View.GONE);
//                mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
//                mFullScreen.setVisibility(View.VISIBLE);
//                mClarity.setVisibility(View.GONE);
//                mBatteryTime.setVisibility(View.GONE);
//                if (hasRegisterBatteryReceiver) {
//                    mContext.unregisterReceiver(mBatterReceiver);
//                    hasRegisterBatteryReceiver = false;
//                }
//                break;
//            case NiceVideoPlayer.MODE_FULL_SCREEN:
//                mBack.setVisibility(View.VISIBLE);
//                mFullScreen.setVisibility(View.GONE);
//                mFullScreen.setImageResource(R.drawable.ic_player_shrink);
//                if (clarities != null && clarities.size() > 1) {
//                    mClarity.setVisibility(View.VISIBLE);
//                }
//                mBatteryTime.setVisibility(View.VISIBLE);
//                if (!hasRegisterBatteryReceiver) {
//                    mContext.registerReceiver(mBatterReceiver,
//                            new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//                    hasRegisterBatteryReceiver = true;
//                }
//                break;
//            case NiceVideoPlayer.MODE_TINY_WINDOW:
//                mBack.setVisibility(View.VISIBLE);
//                mClarity.setVisibility(View.GONE);
//                break;
//        }
//    }
//
    protected void reset() {
        topBottomVisible = false;
        cancelUpdateProgressTimer();
        cancelDismissTopBottomTimer();
        mSeek.setProgress(0);
        mSeek.setSecondaryProgress(0);

        mCenterStart.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);

        mBottom.setVisibility(View.GONE);
        mFullScreen.setImageResource(R.drawable.ic_player_enlarge);

        mLength.setVisibility(View.VISIBLE);

        mTop.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.GONE);

        mLoading.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mCompleted.setVisibility(View.GONE);
    }
//
//    /**
//     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
//     * UI相关的逻辑都尽量到{@link #onPlayStateChanged}和{@link #onPlayModeChanged}中处理.
//     */
//    @Override
//    public void onClick(View v) {
//        if (v == mCenterStart) {
//            if (mNiceVideoPlayer.isIdle()) {
//                mNiceVideoPlayer.start();
//            }
//        } else if (v == mBack) {
//            if (mNiceVideoPlayer.isFullScreen()) {
//                mNiceVideoPlayer.exitFullScreen();
//            } else if (mNiceVideoPlayer.isTinyWindow()) {
//                mNiceVideoPlayer.exitTinyWindow();
//            }
//        } else if (v == mRestartPause) {
//            if (mNiceVideoPlayer.isPlaying() || mNiceVideoPlayer.isBufferingPlaying()) {
//                mNiceVideoPlayer.pause();
//            } else if (mNiceVideoPlayer.isPaused() || mNiceVideoPlayer.isBufferingPaused()) {
//                mNiceVideoPlayer.restart();
//            }
//        } else if (v == mFullScreen) {
//            if (mNiceVideoPlayer.isNormal() || mNiceVideoPlayer.isTinyWindow()) {
//                mNiceVideoPlayer.enterFullScreen();
//            } else if (mNiceVideoPlayer.isFullScreen()) {
//                mNiceVideoPlayer.exitFullScreen();
//            }
//        } else if (v == mClarity) {
//            setTopBottomVisible(false); // 隐藏top、bottom
//            mClarityDialog.show();     // 显示清晰度对话框
//        } else if (v == mRetry) {
//            mNiceVideoPlayer.restart();
//        } else if (v == mReplay) {
//            mRetry.performClick();
//        } else if (v == mShare) {
//            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
//        } else if (v == this) {
//            if (mNiceVideoPlayer.isPlaying()
//                    || mNiceVideoPlayer.isPaused()
//                    || mNiceVideoPlayer.isBufferingPlaying()
//                    || mNiceVideoPlayer.isBufferingPaused()) {
//                setTopBottomVisible(!topBottomVisible);
//            }
//        }
//    }
//

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


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onClick(View view) {

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
}