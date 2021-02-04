package com.wyp.android.wxvideoplayer.player;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Map;

/**
 * Created by wyp on 2021/2/3.
 */

public class WxVideoPlayer extends FrameLayout implements IWxVideoPlayer {
    private FrameLayout mContainer;
    private Context mContext;
    private WxPlayerController mController;

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

    }

    @Override
    public void start() {

    }

    @Override
    public void start(long position) {

    }

    @Override
    public void restart() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(long pos) {

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
        return false;
    }

    @Override
    public boolean isPreparing() {
        return false;
    }

    @Override
    public boolean isPrepared() {
        return false;
    }

    @Override
    public boolean isBufferingPlaying() {
        return false;
    }

    @Override
    public boolean isBufferingPaused() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public boolean isTinyWindow() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return false;
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
    public long getDuration() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public float getSpeed(float speed) {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }

    @Override
    public void enterFullScreen() {

    }

    @Override
    public boolean exitFullScreen() {
        return false;
    }

    @Override
    public void enterTinyWindow() {

    }

    @Override
    public boolean exitTinyWindow() {
        return false;
    }

    @Override
    public void releasePlayer() {

    }

    @Override
    public void release() {

    }
}
