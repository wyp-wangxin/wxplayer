package com.wyp.wxplayer.player;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.wyp.wxplayer.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2021/2/3.
 */

public class WxPlayerController extends FrameLayout implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.load_text)
    TextView mLoadText;
    @Bind(R.id.loading)
    LinearLayout mLoading;
    @Bind(R.id.change_position_current)
    TextView mChangePositionCurrent;
    @Bind(R.id.change_position_progress)
    ProgressBar mChangePositionProgress;
    @Bind(R.id.change_position)
    LinearLayout mChangePosition;
    @Bind(R.id.change_brightness_progress)
    ProgressBar mChangeBrightnessProgress;
    @Bind(R.id.change_brightness)
    LinearLayout mChangeBrightness;
    @Bind(R.id.change_volume_progress)
    ProgressBar mChangeVolumeProgress;
    @Bind(R.id.change_volume)
    LinearLayout mChangeVolume;
    @Bind(R.id.replay)
    TextView mReplay;
    @Bind(R.id.share)
    TextView mShare;
    @Bind(R.id.completed)
    LinearLayout mCompleted;
    @Bind(R.id.retry)
    TextView mRetry;
    @Bind(R.id.error)
    LinearLayout mError;
    @Bind(R.id.back)
    ImageView mBack;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.battery)
    ImageView mBattery;
    @Bind(R.id.time)
    TextView mTime;
    @Bind(R.id.battery_time)
    LinearLayout mBatteryTime;
    @Bind(R.id.top)
    LinearLayout mTop;
    @Bind(R.id.restart_or_pause)
    ImageView mRestartOrPause;
    @Bind(R.id.position)
    TextView mPosition;
    @Bind(R.id.duration)
    TextView mDuration;
    @Bind(R.id.seek)
    SeekBar mSeek;
    @Bind(R.id.clarity)
    TextView mClarity;
    @Bind(R.id.full_screen)
    ImageView mFullScreen;
    @Bind(R.id.bottom)
    LinearLayout mBottom;
    @Bind(R.id.length)
    TextView mLength;
    @Bind(R.id.center_start)
    ImageView mCenterStart;


    private Context mContext;
    private CountDownTimer mDismissTopBottomCountDownTimer;


    public WxPlayerController(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.tx_video_palyer_controller, WxPlayerController.this, false);

        mCenterStart.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRestartOrPause.setOnClickListener(this);
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
//    protected void reset() {
//        topBottomVisible = false;
//        cancelUpdateProgressTimer();
//        cancelDismissTopBottomTimer();
//        mSeek.setProgress(0);
//        mSeek.setSecondaryProgress(0);
//
//        mCenterStart.setVisibility(View.VISIBLE);
//        mImage.setVisibility(View.VISIBLE);
//
//        mBottom.setVisibility(View.GONE);
//        mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
//
//        mLength.setVisibility(View.VISIBLE);
//
//        mTop.setVisibility(View.VISIBLE);
//        mBack.setVisibility(View.GONE);
//
//        mLoading.setVisibility(View.GONE);
//        mError.setVisibility(View.GONE);
//        mCompleted.setVisibility(View.GONE);
//    }
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
//
//    /**
//     * 设置top、bottom的显示和隐藏
//     *
//     * @param visible true显示，false隐藏.
//     */
//    private void setTopBottomVisible(boolean visible) {
//        mTop.setVisibility(visible ? View.VISIBLE : View.GONE);
//        mBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
//        topBottomVisible = visible;
//        if (visible) {
//            if (!mNiceVideoPlayer.isPaused() && !mNiceVideoPlayer.isBufferingPaused()) {
//                startDismissTopBottomTimer();
//            }
//        } else {
//            cancelDismissTopBottomTimer();
//        }
//    }
//
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
}
