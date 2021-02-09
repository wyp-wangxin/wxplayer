package com.wyp.android.wxvideoplayer.opengl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.wyp.android.wxvideoplayer.R;
import com.wyp.android.wxvideoplayer.log.MyLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class WxRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{


    public static final int RENDER_YUV = 1;
    public static final int RENDER_MEDIACODEC = 2;

    private Context context;

    private final float[] vertexData ={

            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f

    };

    private final float[] textureData ={
            0f,1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer matrixBuffer;
    private int renderType = RENDER_YUV;

    //yuv
    private int program_yuv;
    private int avPosition_yuv;
    private int afPosition_yuv;
    private int textureid;

    private int sampler_y;
    private int sampler_u;
    private int sampler_v;
    private int[] textureId_yuv;
    private int[] textureids;

    private int width_yuv;
    private int height_yuv;
    private ByteBuffer y;
    private ByteBuffer u;
    private ByteBuffer v;

    //mediacodec
    private int program_mediacodec;
    private int avPosition_mediacodec;
    private int afPosition_mediacodec;
    private int samplerOES_mediacodec;
    private int textureId_mediacodec;
    private int u_matrix;
    float[] matrix = new float[16];
   // private int yuv_wdith =1920;
   // private int yuv_height=1080;

    private int yuv_wdith =0;
    private int yuv_height=0;
    private int screen_width = 0;
    private int screen_height =0;
    private SurfaceTexture surfaceTexture;
    private Surface surface;

    private OnSurfaceCreateListener onSurfaceCreateListener;
    private OnRenderListener onRenderListener;
    private boolean INIT_YUV =false;
    private boolean INT_MEIDIA =false;
    public WxRender(Context context)
    {
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
        MyLog.d(textureBuffer.toString());
        //MyLog.d(""+textureBuffer.array());
        matrixBuffer = ByteBuffer.allocateDirect(matrix.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(matrix);
        matrixBuffer.position(0);

    }

    public void setRenderType(int renderType) {
        MyLog.d("setRenderType : "+renderType);
        this.renderType = renderType;
    }

    public void setOnSurfaceCreateListener(OnSurfaceCreateListener onSurfaceCreateListener) {
        this.onSurfaceCreateListener = onSurfaceCreateListener;
    }

    public void setOnRenderListener(OnRenderListener onRenderListener) {
        this.onRenderListener = onRenderListener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        MyLog.d("onSurfaceCreated 被调用 ！");

        initRenderYUV();
        initRenderMediacodec();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        MyLog.d("onSurfaceChanged width:"+width+" height:"+height);
        screen_width = width;
        screen_height =height;
        setMatrix(screen_width, screen_height);
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        //MyLog.d("onDrawFrame ");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        setMatrix(screen_width,screen_height);
        if(renderType == RENDER_YUV)
        {
            renderYUV();
        }
        else if(renderType == RENDER_MEDIACODEC)
        {
            renderMediacodec();
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if(onRenderListener != null)
        {
            //MyLog.d(" onFrameAvailable ");
            onRenderListener.onRender();
        }
    }

    private void initRenderYUV()
    {
        String vertexSource = WxShaderUtil.readRawTxt(context, R.raw.vertex_shader);
        String fragmentSource = WxShaderUtil.readRawTxt(context, R.raw.fragment_yuv);
        program_yuv = WxShaderUtil.createProgram(vertexSource, fragmentSource);

        avPosition_yuv = GLES20.glGetAttribLocation(program_yuv, "av_Position");
        afPosition_yuv = GLES20.glGetAttribLocation(program_yuv, "af_Position");

        sampler_y = GLES20.glGetUniformLocation(program_yuv, "sampler_y");
        sampler_u = GLES20.glGetUniformLocation(program_yuv, "sampler_u");
        sampler_v = GLES20.glGetUniformLocation(program_yuv, "sampler_v");

        textureId_yuv = new int[3];
        GLES20.glGenTextures(3, textureId_yuv, 0);

        for(int i = 0; i < 3; i++)
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[i]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        }

        INIT_YUV = true;
    }

    public void destroy_glyuv(){
        IntBuffer ib = ByteBuffer.allocateDirect(textureId_yuv.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer().put(textureId_yuv);
        ib.position(0);
        GLES20.glDeleteTextures(3, ib);
        GLES20.glDetachShader(program_yuv, avPosition_yuv);
        GLES20.glDetachShader(program_yuv, afPosition_yuv);
        GLES20.glDeleteShader(avPosition_yuv);
        GLES20.glDeleteShader(afPosition_yuv);
        GLES20.glDeleteProgram(program_yuv);
    }

    public void setYUVRenderData(int width, int height, byte[] y, byte[] u, byte[] v)
    {
        this.width_yuv = width;
        this.height_yuv = height;
        this.y = ByteBuffer.wrap(y);
        this.u = ByteBuffer.wrap(u);
        this.v = ByteBuffer.wrap(v);
    }

    private void renderYUV()
    {
        if(width_yuv > 0 && height_yuv > 0 && y != null && u != null && v != null)
        {
            MyLog.d("renderYUV!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            GLES20.glUseProgram(program_yuv);

            GLES20.glEnableVertexAttribArray(avPosition_yuv);
            GLES20.glVertexAttribPointer(avPosition_yuv, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);

            GLES20.glEnableVertexAttribArray(afPosition_yuv);
            GLES20.glVertexAttribPointer(afPosition_yuv, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[0]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv, height_yuv, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, y);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[1]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv / 2, height_yuv / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, u);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[2]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv / 2, height_yuv / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, v);

            GLES20.glUniform1i(sampler_y, 0);
            GLES20.glUniform1i(sampler_u, 1);
            GLES20.glUniform1i(sampler_v, 2);

            y.clear();
            u.clear();
            v.clear();
            y = null;
            u = null;
            v = null;
        }
    }


    private void initRenderMediacodec()
    {
        String vertexSource = WxShaderUtil.readRawTxt(context, R.raw.vertex_shader);
        String fragmentSource = WxShaderUtil.readRawTxt(context, R.raw.fragment_mediacodec);
        program_mediacodec = WxShaderUtil.createProgram(vertexSource, fragmentSource);

        avPosition_mediacodec = GLES20.glGetAttribLocation(program_mediacodec, "av_Position");
        afPosition_mediacodec = GLES20.glGetAttribLocation(program_mediacodec, "af_Position");
        samplerOES_mediacodec = GLES20.glGetUniformLocation(program_mediacodec, "sTexture");
        u_matrix = GLES20.glGetUniformLocation(program_mediacodec, "u_Matrix");

         textureids = new int[1];
        GLES20.glGenTextures(1, textureids, 0);
        textureId_mediacodec = textureids[0];

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        surfaceTexture = new SurfaceTexture(textureId_mediacodec);
        surface = new Surface(surfaceTexture);
        surfaceTexture.setOnFrameAvailableListener(this);

        if(onSurfaceCreateListener != null)
        {
            onSurfaceCreateListener.onSurfaceCreate(surface);
        }
    }

    public void destroy_glmediacodec(){
        IntBuffer ib = ByteBuffer.allocateDirect(textureids.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer().put(textureids);
        ib.position(0);
        GLES20.glDeleteTextures(1, ib);
        GLES20.glDetachShader(program_mediacodec, avPosition_mediacodec);
        GLES20.glDetachShader(program_mediacodec, afPosition_mediacodec);
        GLES20.glDetachShader(program_mediacodec, samplerOES_mediacodec);
        GLES20.glDetachShader(program_mediacodec, u_matrix);
        GLES20.glDeleteShader(avPosition_mediacodec);
        GLES20.glDeleteShader(afPosition_mediacodec);
        GLES20.glDeleteShader(samplerOES_mediacodec);
        GLES20.glDeleteProgram(program_mediacodec);
        GLES20.glDeleteProgram(u_matrix);
    }

    private void renderMediacodec()
    {
        MyLog.d("renderMediacodec !!!!!!!!!!!!!!!!!!!!!DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        surfaceTexture.updateTexImage();
        GLES20.glUseProgram(program_mediacodec);


        //MyLog.d(matrixBuffer.toString());
        //MyLog.d(" dd "+matrixBuffer.get(0));
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, matrixBuffer);

        GLES20.glEnableVertexAttribArray(avPosition_mediacodec);
        GLES20.glVertexAttribPointer(avPosition_mediacodec, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);

        GLES20.glEnableVertexAttribArray(afPosition_mediacodec);
        GLES20.glVertexAttribPointer(afPosition_mediacodec, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId_mediacodec);
        GLES20.glUniform1i(samplerOES_mediacodec, 0);
        INT_MEIDIA=true;
    }


    public interface OnSurfaceCreateListener
    {
        void onSurfaceCreate(Surface surface);
    }

    public interface OnRenderListener{
        void onRender();
    }

    public void setMatrix(int width, int height) {

        WxShaderUtil.initMatrix(matrix);
        if(yuv_wdith > 0 && yuv_height > 0)
        {
            float screen_r =(float) 1.0 * width / height;
            float picture_r = (float)1.0 * yuv_wdith / yuv_height;

            if(screen_r > picture_r) //图片宽度缩放
            {
                float r =(float) (width / (1.0 * height / yuv_height * yuv_wdith));
                WxShaderUtil.orthoM(-r, r, -1, 1, matrix);
                matrixBuffer.clear();
                matrixBuffer.put(matrix,0,matrix.length);
                matrixBuffer.position(0);
            } else{//图片高度缩放
                float r =(float)( height / (1.0 * width / yuv_wdith * yuv_height));
                WxShaderUtil.orthoM(-1, 1, -r, r, matrix);
                matrixBuffer.clear();
                matrixBuffer.put(matrix,0,matrix.length);
                matrixBuffer.position(0);
            }
        }
    }

    public void setDataWidthHegit(int width, int height){
        yuv_wdith = width;
        yuv_height = height;
    }

}
