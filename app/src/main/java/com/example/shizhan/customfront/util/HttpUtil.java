package com.example.shizhan.customfront.util;

import android.os.Message;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.example.shizhan.customfront.model.Custom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by shizhan on 16/7/25.
 */
public class HttpUtil {

    public static void sendRequestWithHttpClient(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 请求和响应都成功
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");
                        //Gson解析服务器发过来的数据
                        Log.d("data from the server：",response);

                        //Log.d("show response",response);
//                        Message message = new Message();
//                        message.what = SHOW_RESPONSE;
//                        // 将服务器返回的结果存放到Message中
//                        message.obj = response.toString();
//                        handler.sendMessage(message);
                        if (listener != null) {
                            //回调onFinish()方法
                            listener.onFinish(parseJSONwithGson(response));
                        }
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        Log.d("HttpClient","in the catch");
                        listener.onError(e);
                    }
                    else {
                        Log.d("HttpClient","listener null");
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static List<Custom>  parseJSONwithGson(String response){
        Gson gson=new Gson();
        List<Custom> customList = gson.fromJson(response, new TypeToken<List<Custom>>() {}.getType());
        for(Custom custom:customList){
            Log.d("custom_name",custom.getCustom_name());
            Log.d("alarm_time",custom.getAlarm_time());
            Log.d("insist_day",custom.getInsist_day());
            Log.d("image_url",custom.getImage_url());
        }
        return customList;
    }

}

