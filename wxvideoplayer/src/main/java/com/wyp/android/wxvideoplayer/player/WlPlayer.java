package com.wyp.android.wxvideoplayer.player;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.view.Surface;

import com.wyp.android.wxvideoplayer.WlTimeInfoBean;
import com.wyp.android.wxvideoplayer.listener.WlOnCompleteListener;
import com.wyp.android.wxvideoplayer.listener.WlOnErrorListener;
import com.wyp.android.wxvideoplayer.listener.WlOnLoadListener;
import com.wyp.android.wxvideoplayer.listener.WlOnParparedListener;
import com.wyp.android.wxvideoplayer.listener.WlOnPauseResumeListener;
import com.wyp.android.wxvideoplayer.listener.WlOnTimeInfoListener;
import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.opengl.WlGLSurfaceView;
import com.wyp.android.wxvideoplayer.opengl.WlRender;
import com.wyp.android.wxvideoplayer.util.WlVideoSupportUitl;

import java.nio.ByteBuffer;

/**
 * Created by yangw on 2018-2-28.
 */

public class WlPlayer {

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
	
    private WlOnParparedListener wlOnParparedListener;
    private WlOnLoadListener wlOnLoadListener;
    private WlOnPauseResumeListener wlOnPauseResumeListener;
    private WlOnTimeInfoListener wlOnTimeInfoListener;
    private WlOnErrorListener wlOnErrorListener;
    private WlOnCompleteListener wlOnCompleteListener;
    private WlGLSurfaceView wlGLSurfaceView;

    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private Surface surface;
    private MediaCodec.BufferInfo info;
    private WlRender mWlRender;


    public WlPlayer()
    {}

    /**
     * 设置数据源
     * @param source
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    public void setWlGLSurfaceView(WlGLSurfaceView wlGLSurfaceView) {
        this.wlGLSurfaceView = wlGLSurfaceView;
        mWlRender = wlGLSurfaceView.getWlRender();
        mWlRender.setOnSurfaceCreateListener(new WlRender.OnSurfaceCreateListener() {
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
     * @param wlOnParparedListener
     */
    public void setWlOnParparedListener(WlOnParparedListener wlOnParparedListener)
    {
        this.wlOnParparedListener = wlOnParparedListener;
    }

    public void setWlOnLoadListener(WlOnLoadListener wlOnLoadListener) {
        this.wlOnLoadListener = wlOnLoadListener;
    }

    public void setWlOnPauseResumeListener(WlOnPauseResumeListener wlOnPauseResumeListener) {
        this.wlOnPauseResumeListener = wlOnPauseResumeListener;
    }

    public void setWlOnTimeInfoListener(WlOnTimeInfoListener wlOnTimeInfoListener) {
        this.wlOnTimeInfoListener = wlOnTimeInfoListener;
    }

    public void setWlOnErrorListener(WlOnErrorListener wlOnErrorListener) {
        this.wlOnErrorListener = wlOnErrorListener;
    }

    public void setWlOnCompleteListener(WlOnCompleteListener wlOnCompleteListener) {
        this.wlOnCompleteListener = wlOnCompleteListener;
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
        if(wlOnParparedListener != null)
        {
            wlOnParparedListener.onParpared();
        }
    }

    public native void n_parpared(String source);
    public native void n_start();
    private native void n_pause();
    private native void n_resume();
    private native void n_stop();
    private native void n_seek(int secds);
    private native int n_duration();

    public WlOnLoadListener getWlOnLoadListener() {
        return wlOnLoadListener;
    }

    public void onCallLoad(boolean bool){
        if(wlOnLoadListener!=null){
            wlOnLoadListener.onLoad(bool);
        }
    }

    public void onCallTimeInfo(int currentTime, int totalTime)
    {
        if(wlOnTimeInfoListener != null)
        {
            if(wlTimeInfoBean == null)
            {
                wlTimeInfoBean = new WlTimeInfoBean();
            }
            wlTimeInfoBean.setCurrentTime(currentTime);
            wlTimeInfoBean.setTotalTime(totalTime);
            wlOnTimeInfoListener.onTimeInfo(wlTimeInfoBean);
        }
    }

    public void onCallError(int code, String msg)
    {
        if(wlOnErrorListener != null)
        {
            stop();
            wlOnErrorListener.onError(code, msg);
        }
    }


    public void onCallComplete()
    {
        if(wlOnCompleteListener != null)
        {
            stop();
            wlOnCompleteListener.onComplete();
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
        if(wlGLSurfaceView != null)
        {
            wlGLSurfaceView.getWlRender().setRenderType(WlRender.RENDER_YUV);
            wlGLSurfaceView.setYUVData(width, height, y, u, v);
        }
    }
	public boolean onCallIsSupportMediaCodec(String ffcodecname)
    {
        return WlVideoSupportUitl.isSupportCodec(ffcodecname);
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
                mWlRender.setDataWidthHegit(width,height);
                wlGLSurfaceView.getWlRender().setRenderType(WlRender.RENDER_MEDIACODEC);
                String mime = WlVideoSupportUitl.findVideoCodecName(codecName);
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
            if(wlOnErrorListener != null)
            {
                wlOnErrorListener.onError(2001, "surface is null");
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
