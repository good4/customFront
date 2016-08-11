package com.example.shizhan.customfront;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shizhan on 16/7/29.
 */
public class MainFragment extends Fragment {

    private AtomicInteger count=new AtomicInteger(0);//是否加static？
    private static final int SHOW_LIST=1;
    private static final int SHOW_IMAGE=2;
    private Context cContext;
    private List<Custom> cData;
    private HashMap<Integer,Bitmap> images;
    private CustomAdapter cAdapter = null;
    private ListView userCustom=null;
    private Handler handler=null;

    //userName应该由Activity传给Fragment
    private String userName="shizhan";
    private static final String baseUrl="http://192.168.1.104:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！
    private static String parameter="";

    //OSS init
    private static final String bucketName="shenyang";
    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    private OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("POTVuwgchOwAcIE9", "PEAub5vvc9FLwT1bZpbemIcydeb17R");
    private OSS oss = new OSSClient(cContext, endpoint, credentialProvider);

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        cContext=activity;
//        parameter="?userName=" + userName;
//    }
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
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_custom,container,false);
        userCustom = (ListView) view.findViewById(R.id.list_view);
        cContext=getActivity();
        parameter="?userName=" + userName;
            //          test Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.custom_toolbar);
        //ToolBar默认显示项目名，这里不显示ToolBar默认的
        toolbar.setTitle("");
//         getActivity().setSupportActionBar(toolbar);

        //处理子线程发来的消息
         handler=new Handler(){
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
                    Log.d("images",String.valueOf(images.size()));
                    Log.d("cData",String.valueOf(cData.size()));
                    cAdapter=new CustomAdapter(cData,images,cContext);
                    //test
//                    if(userCustom==null)
//                        Log.d("userCustom","is null");
                    userCustom.setAdapter(cAdapter);
                }
            }
        };
        /*
        * 向服务器发送查询请求
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
        return view;
    }
}
