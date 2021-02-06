package com.wyp.android.wxvideoplayer.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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
}
