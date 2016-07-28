package com.example.shizhan.customfront;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.example.shizhan.customfront.adapter.CustomAdapter;
import com.example.shizhan.customfront.model.Custom;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {

    private AtomicInteger count=new AtomicInteger(0);//是否加static？
    private static final int SHOW_LIST=1;
    private static final int SHOW_IMAGE=2;
    private static Context cContext;
    private List<Custom> cData;
    private HashMap<Integer,Bitmap> images;
    private CustomAdapter cAdapter = null;
    private ListView userCustom;
    /////////////////////////////////////
    private String userName="shizhan";
    private static final String baseUrl="http://192.168.1.106:8080/";
    private static String parameter="";

    //OSS init
    private static final String bucketName="shenyang";
    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    static OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("POTVuwgchOwAcIE9", "PEAub5vvc9FLwT1bZpbemIcydeb17R");
    static OSS oss = new OSSClient(cContext, endpoint, credentialProvider);

    //Test data
    private String[] data = { "Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape",
            "Pineapple","Strawberry", "Cherry", "Mango","Lemon","Blueberry","abc","def" };
    private String[] num={"1","2","3","4","5","6","7","8","9","0","1","2","3","4"};

    //处理子线程发来的消息
    private Handler handler=new Handler(){
        public void handleMessage(Message message){
            if(message.what==SHOW_LIST){
                images=new HashMap<>();
                for(int i=0;i<cData.size();i++){
                    String imageUrl=cData.get(i).getImage_url();
                    int index0=endpoint.length()+bucketName.length()+2;
                    String objectKey=imageUrl.substring(index0);
                    Log.d("objectKey",objectKey);
                    getImageWithAliyun(i,objectKey);
                }
            }
            else if(message.what==SHOW_IMAGE){
                //images.add((Bitmap) message.obj);
                Log.d("images",String.valueOf(images.size()));
                cAdapter=new CustomAdapter(cData,images,cContext);
                userCustom.setAdapter(cAdapter);
            }
        }
    };

    //根据登录时的用户名在服务器上查询该用户的习惯并显示在主界面上
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getSupportActionBar().hide();//隐藏标题栏，API版本24
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//API版本21
        setContentView(R.layout.activity_main);

        //init
        cContext=MainActivity.this;
        userCustom = (ListView) findViewById(R.id.list_view);
        parameter="?userName=" + userName;
        /*
        * 发送查询请求
        *
        * */
        HttpUtil.sendRequestWithHttpClient(baseUrl +parameter , new HttpCallbackListener() {
            @Override
            public void onFinish(List<Custom> response) {
                Log.d("user customs ","success!");
                cData=response;
                Message message=new Message();
                message.what=SHOW_LIST;
                message.obj=response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d("user customs","请求服务器失败！");
            }
        });

    }
    public void  getImageWithAliyun(final int i, String objectKey){

        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName,objectKey);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                count.getAndIncrement();
                // 请求成功
                Log.d("Content-Length", "" + result.getContentLength());
                try{
                    InputStream inputStream = result.getObjectContent();
                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                    images.put(Integer.valueOf(i),bitmap);
                    Log.d("第"+i+"个",String.valueOf(i));
                    if(count.intValue()==cData.size()){
                        Message message=new Message();
                        message.what=SHOW_IMAGE;
                        message.obj=images;
                        handler.sendMessage(message);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
// task.cancel(); // 可以取消任务

// task.waitUntilFinished(); // 如果需要等待任务完成
    }
}
