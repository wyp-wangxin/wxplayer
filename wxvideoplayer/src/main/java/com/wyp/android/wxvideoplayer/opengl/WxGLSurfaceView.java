package com.wyp.android.wxvideoplayer.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.wyp.android.wxvideoplayer.log.MyLog;

public class WxGLSurfaceView extends GLSurfaceView{

    private WxRender mWxRender;

    public WxGLSurfaceView(Context context) {
        this(context, null);
    }

    public WxGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mWxRender = new WxRender(context);
        setRenderer(mWxRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mWxRender.setOnRenderListener(new WxRender.OnRenderListener() {
            @Override
            public void onRender() {
                //MyLog.d("wwxx onRender setOnRenderListener");
                requestRender();
            }
        });
    }

    public void setYUVData(int width, int height, byte[] y, byte[] u, byte[] v)
    {
        if(mWxRender != null)
        {
            mWxRender.setYUVRenderData(width, height, y, u, v);
            requestRender();
        }
    }

    public WxRender getWxRender() {
        return mWxRender;
    }
    public void relese(){
        mWxRender.destroy_glyuv();
        mWxRender.destroy_glmediacodec();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
