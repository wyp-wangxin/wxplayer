package com.wyp.wxplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.URLProviderUtil;
import com.wyp.wxplayer.bean.AreaBean;
import com.wyp.wxplayer.http.BaseCallBack;
import com.wyp.wxplayer.http.HttpManager;
import com.wyp.wxplayer.utils.myLog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        //String url = URLProviderUtil.getMainPageUrl(0,10);

//      getMethod(url);
//        getInChildThread(url);//OKhttp 自带现场，可以不用自己new thread。
//        postInChildThread(url);

        HttpManager.getInstance().get(url, new BaseCallBack<List<AreaBean>>() {
            @Override
            public void onFailure(int code, Exception e) {
                myLog.e(TAG,"OkHttpTestActivity.onFailure,e="+e);
            }

            @Override
            public void onSuccess(List<AreaBean> areaBeen) {
                myLog.e(TAG,"OkHttpTestActivity.onSuccess,areaBeen="+areaBeen.size());

            }

        });
    }

    // 在子线程发起 post 请求
    private void postInChildThread(String url) {

        // 创建客户端
        OkHttpClient okHttpClient = new OkHttpClient();

        // 创建表单
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("offset","0");
        bodyBuilder.add("size","10");

        // 创建请求体对象
        RequestBody body = bodyBuilder.build();

        // 创建请求参数
        Request request = new Request.Builder().url(url).post(body).build();

        // 创建请求对象
        Call call = okHttpClient.newCall(request);
        // 发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String result = response.body().string();
                    myLog.e(TAG,"OkHttpTestActivity.postInChildThread,result="+result);
                }
            }
        });

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
