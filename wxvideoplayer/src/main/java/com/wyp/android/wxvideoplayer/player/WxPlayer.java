package com.wyp.android.wxvideoplayer.player;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.view.Surface;

import com.wyp.android.wxvideoplayer.WlTimeInfoBean;
import com.wyp.android.wxvideoplayer.listener.WxOnCompleteListener;
import com.wyp.android.wxvideoplayer.listener.WxOnErrorListener;
import com.wyp.android.wxvideoplayer.listener.WxOnLoadListener;
import com.wyp.android.wxvideoplayer.listener.WxOnParparedListener;
import com.wyp.android.wxvideoplayer.listener.WxOnPauseResumeListener;
import com.wyp.android.wxvideoplayer.listener.WxOnTimeInfoListener;
import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.opengl.WxGLSurfaceView;
import com.wyp.android.wxvideoplayer.opengl.WxRender;
import com.wyp.android.wxvideoplayer.util.WxVideoSupportUitl;

import java.nio.ByteBuffer;

/**
 * Created by wyp on 2021/2/6.
 */

public class WxPlayer {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avutil-55");
        System.loadLibrary("postproc-54");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
    }

    private static String source;//数据源
    private static WlTimeInfoBean wlTimeInfoBean;
    private static int duration = -1;
    private static boolean playNext = false;

    private WxOnParparedListener mWxOnParparedListener;
    private WxOnLoadListener mWxOnLoadListener;
    private WxOnPauseResumeListener wlOnPauseResumeListener;
    private WxOnTimeInfoListener mWxOnTimeInfoListener;
    private WxOnErrorListener mWxOnErrorListener;
    private WxOnCompleteListener mWxOnCompleteListener;
    private WxGLSurfaceView mWxGLSurfaceView;

    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private Surface surface;
    private MediaCodec.BufferInfo info;
    private WxRender mWxRender;


    public WxPlayer()
    {}

    /**
     * 设置数据源
     * @param source
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    public void setWxGLSurfaceView(WxGLSurfaceView wxGLSurfaceView) {
        this.mWxGLSurfaceView = wxGLSurfaceView;
        mWxRender = wxGLSurfaceView.getWxRender();
        mWxRender.setOnSurfaceCreateListener(new WxRender.OnSurfaceCreateListener() {
            @Override
            public void onSurfaceCreate(Surface s) {
                if(surface == null)
                {
                    surface = s;
                    MyLog.d("onSurfaceCreate 被创建");
                }
            }
        });
    }

    /**
     * 设置准备接口回调
     * @param wxOnParparedListener
     */
    public void setWxOnParparedListener(WxOnParparedListener wxOnParparedListener)
    {
        this.mWxOnParparedListener = wxOnParparedListener;
    }

    public void setWxOnLoadListener(WxOnLoadListener wxOnLoadListener) {
        this.mWxOnLoadListener = wxOnLoadListener;
    }

    public void setWlOnPauseResumeListener(WxOnPauseResumeListener wlOnPauseResumeListener) {
        this.wlOnPauseResumeListener = wlOnPauseResumeListener;
    }

    public void setWxOnTimeInfoListener(WxOnTimeInfoListener wxOnTimeInfoListener) {
        this.mWxOnTimeInfoListener = wxOnTimeInfoListener;
    }

    public void setWxOnErrorListener(WxOnErrorListener wxOnErrorListener) {
        this.mWxOnErrorListener = wxOnErrorListener;
    }

    public void setWxOnCompleteListener(WxOnCompleteListener wxOnCompleteListener) {
        this.mWxOnCompleteListener = wxOnCompleteListener;
    }

    public void parpared()
    {
        if(TextUtils.isEmpty(source))
        {
            MyLog.d("source not be empty");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                n_parpared(source);
            }
        }).start();
    }

    public void start()
    {
        if(TextUtils.isEmpty(source))
        {
            MyLog.d("source is empty");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                n_start();
            }
        }).start();
    }
    public void stop()
    {
        wlTimeInfoBean = null;
        duration = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyLog.d("stop 被调用了");
                n_stop();
                releaseMediacodec();
            }
        }).start();
    }

    public void pause()
    {
        n_pause();
        if(wlOnPauseResumeListener != null)
        {
            wlOnPauseResumeListener.onPause(true);
        }
    }


    public void resume()
    {
        n_resume();
        if(wlOnPauseResumeListener != null)
        {
            wlOnPauseResumeListener.onPause(false);
        }
    }

    public void seek(int secds)
    {
        n_seek(secds);
    }

    public void playNext(String url)
    {
        source = url;
        playNext = true;
        stop();
    }
    public int getDuration()
    {
        if(duration < 0)
        {
            duration = n_duration();
        }
        return duration;
    }

    /**
     * c++回调java的方法
     */
    public void onCallParpared()
    {
        if(mWxOnParparedListener != null)
        {
            mWxOnParparedListener.onParpared();
        }
    }

    public native void n_parpared(String source);
    public native void n_start();
    private native void n_pause();
    private native void n_resume();
    private native void n_stop();
    private native void n_seek(int secds);
    private native int n_duration();

    public WxOnLoadListener getWxOnLoadListener() {
        return mWxOnLoadListener;
    }

    public void onCallLoad(boolean bool){
        if(mWxOnLoadListener !=null){
            mWxOnLoadListener.onLoad(bool);
        }
    }

    public void onCallTimeInfo(int currentTime, int totalTime)
    {
        if(mWxOnTimeInfoListener != null)
        {
            if(wlTimeInfoBean == null)
            {
                wlTimeInfoBean = new WlTimeInfoBean();
            }
            wlTimeInfoBean.setCurrentTime(currentTime);
            wlTimeInfoBean.setTotalTime(totalTime);
            mWxOnTimeInfoListener.onTimeInfo(wlTimeInfoBean);
        }
    }

    public void onCallError(int code, String msg)
    {
        if(mWxOnErrorListener != null)
        {
            stop();
            mWxOnErrorListener.onError(code, msg);
        }
    }


    public void onCallComplete()
    {
        if(mWxOnCompleteListener != null)
        {
            stop();
            mWxOnCompleteListener.onComplete();
        }
    }

    public void onCallNext()
    {
        if(playNext)
        {
            try {
                Thread.sleep(500);//等待前面的資源完全釋放
            }catch (Exception E){

            }
            playNext = false;
            parpared();
        }
    }

    public void onCallRenderYUV(int width, int height, byte[] y, byte[] u, byte[] v)
    {
        MyLog.d("获取到视频的yuv数据");
        if(mWxGLSurfaceView != null)
        {
            mWxGLSurfaceView.getWxRender().setRenderType(WxRender.RENDER_YUV);
            mWxGLSurfaceView.setYUVData(width, height, y, u, v);
        }
    }
    public boolean onCallIsSupportMediaCodec(String ffcodecname)
    {
        return WxVideoSupportUitl.isSupportCodec(ffcodecname);
    }


    /**
     * 初始化MediaCodec
     * @param codecName
     * @param width
     * @param height
     * @param csd_0
     * @param csd_1
     */
    public void initMediaCodec(String codecName, int width, int height, byte[] csd_0, byte[] csd_1)
    {
        if(surface != null)
        {
            try {
                mWxRender.setDataWidthHegit(width,height);
                mWxGLSurfaceView.getWxRender().setRenderType(WxRender.RENDER_MEDIACODEC);
                String mime = WxVideoSupportUitl.findVideoCodecName(codecName);
                mediaFormat = MediaFormat.createVideoFormat(mime, width, height);
                mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, width * height);
                mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(csd_0));
                mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(csd_1));
                MyLog.d(mediaFormat.toString());
                mediaCodec = MediaCodec.createDecoderByType(mime);

                info = new MediaCodec.BufferInfo();
                mediaCodec.configure(mediaFormat, surface, null, 0);
                mediaCodec.start();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if(mWxOnErrorListener != null)
            {
                mWxOnErrorListener.onError(2001, "surface is null");
            }
        }
    }

    public void decodeAVPacket(int datasize, byte[] data)
    {
        if(surface != null && datasize > 0 && data != null && mediaCodec != null)
        {
            MyLog.d("JAVA decodeAVPacket 被调用！");
            try
            {
                int intputBufferIndex = mediaCodec.dequeueInputBuffer(10);
                if(intputBufferIndex >= 0)
                {
                    ByteBuffer byteBuffer = mediaCodec.getInputBuffers()[intputBufferIndex];
                    byteBuffer.clear();
                    byteBuffer.put(data);
                    mediaCodec.queueInputBuffer(intputBufferIndex, 0, datasize, 0, 0);
                }
                int outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 10);
                while(outputBufferIndex >= 0)
                {
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, true);
                    outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 10);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private void releaseMediacodec()
    {
        if(mediaCodec != null)
        {
            MyLog.d("releaseMediacodec 被调用！");
            try
            {
                mediaCodec.flush();
                mediaCodec.stop();
                mediaCodec.release();
            }
            catch(Exception e)
            {
                //e.printStackTrace();
            }
            mediaCodec = null;
            mediaFormat = null;
            info = null;
            //surface = null;
        }

    }




}
