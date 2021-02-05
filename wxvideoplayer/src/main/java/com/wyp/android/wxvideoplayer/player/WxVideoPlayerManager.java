package com.wyp.android.wxvideoplayer.player;

/**
 * Created by XiaoJianjun on 2017/5/5.
 * 视频播放器管理器.
 */
public class WxVideoPlayerManager {

    private WxVideoPlayer mVideoPlayer;

    private WxVideoPlayerManager() {
    }

    private static WxVideoPlayerManager sInstance;

    public static synchronized WxVideoPlayerManager instance() {
        if (sInstance == null) {
            sInstance = new WxVideoPlayerManager();
        }
        return sInstance;
    }

    public void setCurrentNiceVideoPlayer(WxVideoPlayer videoPlayer) {
        mVideoPlayer = videoPlayer;
    }

    public void releaseNiceVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    public boolean onBackPressd() {
        if (mVideoPlayer != null) {
            if (mVideoPlayer.isFullScreen()) {
                return mVideoPlayer.exitFullScreen();
            } else if (mVideoPlayer.isTinyWindow()) {
                return mVideoPlayer.exitTinyWindow();
            } else {
                mVideoPlayer.release();
                return false;
            }
        }
        return false;
    }
}
