package com.wyp.android.wxvideoplayer.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WxShaderUtil {


    public static String readRawTxt(Context context, int rawId) {
        InputStream inputStream = context.getResources().openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        try
        {
            while((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int loadShader(int shaderType, String source)
    {
        int shader = GLES20.glCreateShader(shaderType);
        if(shader != 0)
        {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compile = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compile, 0);
            if(compile[0] != GLES20.GL_TRUE)
            {
                Log.d("ywl5320", "shader compile error");
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource)
    {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if(vertexShader == 0)
        {
            return 0;
        }
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if(fragmentShader == 0)
        {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if(program != 0)
        {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);
            int[] linsStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linsStatus, 0);
            if(linsStatus[0] != GLES20.GL_TRUE)
            {
                Log.d("ywl5320", "link program error");
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return  program;

    }

    static void initMatrix(float[] matrix)
    {
        for(int i = 0; i < 16; i++)
        {
            if(i % 5 == 0)
            {
                matrix[i] = 1;
            } else{
                matrix[i] = 0;
            }
        }
    }
    static void orthoM(float left, float right, float bottom, float top, float[] matrix) //正交投影
    {
        matrix[0] = 2 / (right - left);
        matrix[3] = (right + left)/(right - left) * -1;
        matrix[5] = 2 / (top - bottom);
        matrix[7] = (top + bottom) / (top - bottom) * -1;
        matrix[10] = 1;
        matrix[11] = 1;
       /* matrix.position(0);
        matrix.put(0,2 / (right - left));
        matrix.put(3,(right + left)/(right - left) * -1);
        matrix.put(5, 2/(top - bottom));
        matrix.put(7,(top + bottom) / (top - bottom) * -1);
        matrix.put(10, 1);
        matrix.put(11,1);*/
    }
}
