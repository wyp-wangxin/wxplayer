package com.wyp.wxplayer.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpManager {
    private static final String TAG = "HttpManager";

    private static HttpManager httpManager;
    private OkHttpClient okHttpClient;
    private Handler mHandler;

    private HttpManager() {
        this.okHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取 HttpManager 的单例对象
     * @return
     */
    public static HttpManager getInstance(){
        if (httpManager ==null){
            httpManager = new HttpManager();
        }

        return httpManager;
    }

    /**
     * 发起 get 请求
     * @param url
     */
    public void get(String url,BaseCallBack baseCallBack){
        // 创建请求参数
        Request request = new Request.Builder().url(url).build();

        // 发起异步的请求
        doRequest(request,baseCallBack);
    }

    /**
     * 发起 post 请求
     */
    public void post(String url, Map<String,String> params,BaseCallBack baseCallBack) {

        // 创建表单
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String,String> entry:params.entrySet()){
            bodyBuilder.add(entry.getKey(),entry.getValue());
        }

        // 创建请求体对象
        RequestBody body = bodyBuilder.build();

        // 创建请求参数
        Request request = new Request.Builder().url(url).post(body).build();

        doRequest(request,baseCallBack);
    }

    private void doRequest(Request request, final BaseCallBack baseCallBack) {
        // 创建请求对象
        Call call = okHttpClient.newCall(request);
        // 发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseCallBack.onFailure(-1, e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                myLog.e(TAG,"HttpManager.onResponse,thread="+Thread.currentThread());
                // 在子线程读取服务器数据
                final String result = response.body().string();

                // 在主线程更新界面
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()){

                            // 根据 baseCallBack.type 类型的不同，做不同的数据解析
                            if (baseCallBack.type == String.class){
                                // 直接返回String类型的数据
                                baseCallBack.onSuccess(result);
                            }else{
                                try {
                                    // 指定了一个 Bean，直接进行 json 转换
                                    Object obj = new Gson().fromJson(result, baseCallBack.type);
                                    baseCallBack.onSuccess(obj);
                                } catch (Exception e) {
                                    baseCallBack.onFailure(-1,new RuntimeException("JSON解析出错："+result));
                                    e.printStackTrace();
                                }
                            }
//                          myLog.e(TAG,"OkHttpTestActivity.postInChildThread,result="+result);
                        }else{
                            baseCallBack.onFailure(response.code(),new RuntimeException("获取到服务器的错误状态"));
                        }
                    }
                });
            }
        });
    }
}
