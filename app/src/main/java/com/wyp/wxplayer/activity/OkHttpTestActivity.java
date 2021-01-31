package com.wyp.wxplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.utils.myLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTestActivity extends AppCompatActivity {
    private static String TAG = "OkHttpTestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);
        loadData();
    }

    private void loadData() {
        String url = "http://192.168.78.21:8080/1";

//      getMethod(url);
        getInChildThread(url);//OKhttp 自带现场，可以不用自己new thread。
    }

    // 在子线程发起网络请求
    private void getInChildThread(String url) {
        // 创建请求客户端
        OkHttpClient okHttpClient = new OkHttpClient();

        // 创建请求参数
        Request request = new Request.Builder().url(url).build();

        // 创建请求对象
        Call call = okHttpClient.newCall(request);

        // 发起异步的请求
        call.enqueue(new Callback() {
            @Override
            // 请求发生异常
            public void onFailure(Call call, IOException e) {

            }

            @Override
            // 获取到服务器数据。注意：即使是 404 等错误状态也是获取到服务器数据
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    String result = response.body().string();
                    myLog.e(TAG,"OkHttpTestActivity.getInChildThread,result="+result);
                }
            }
        });
    }


    private void getMethod(final String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    // 创建请求客户端
                    OkHttpClient okHttpClient = new OkHttpClient();

                    // 创建请求参数
                    Request request = new Request.Builder().url(url).build();

                    // 创建请求对象
                    Call call = okHttpClient.newCall(request);
                    // 执行请求，获取服务器响应
                    Response response = call.execute();

                    // 获取服务器数据
                    if (response.isSuccessful()){
                        String result = response.body().string();
                        myLog.e(TAG,"OkHttpTestActivity.getMethod,result="+result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
