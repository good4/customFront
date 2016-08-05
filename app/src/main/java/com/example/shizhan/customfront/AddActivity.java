package com.example.shizhan.customfront;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shizhan.customfront.adapter.AddCustomAdapter;
import com.example.shizhan.customfront.model.AddCustom;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String baseUrl="http://192.168.1.103:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！
    public static final String TAG = "AddActivity";
    private List<AddCustom> addData;
    private AddCustomAdapter addCustomAdapter;
    private Context context;
    private ListView listView;
    private Button create_custom;
    private EditText name;
    private EditText day;
    private ImageView clear_name;
    private ImageView clear_day;
    private Toolbar toolbar;
    private Handler handler;

    private Map<String,String> new_custom;
    private static final int AlarmTimeChoose=0;
    private static final int CategoryChoose=1;
    private static final int SHOW_EXIST=0;
    private static final int CREATE_CUSTOM=1;

    //习惯名
    private String custom_name;
    //目标坚持天数
    private String target_day;
    //提醒时间
    private String alarm_time="12:00";
    //习惯分类
    private String category="效率";
    //返回结果
    private String re="";
    private int pos=0;
    //假设已经将用户名得到
    private String userName="shizhan";
    private String userId;
    //请求参数
    private static String parameter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if(savedInstanceState!=null){
            ((EditText)findViewById(R.id.name)).setText(savedInstanceState.getString("customName"));
            ((EditText)findViewById(R.id.day)).setText(savedInstanceState.getString("insistDay"));
        }
        Log.i(TAG,"onCreate");

        context=AddActivity.this;
        listView=(ListView)findViewById(R.id.add_list_view);
        create_custom=(Button)findViewById(R.id.create_custom);
        name=(EditText)findViewById(R.id.name);
        day=(EditText)findViewById(R.id.day);
        clear_name=(ImageView)findViewById(R.id.clear_name);
        clear_day=(ImageView)findViewById(R.id.clear_day);
        toolbar=(Toolbar)findViewById(R.id.add_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //可以只让
                Intent intent=new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        create_custom.setOnClickListener(this);
        clear_name.setOnClickListener(this);
        clear_day.setOnClickListener(this);
        initAddCustom();
        addCustomAdapter=new AddCustomAdapter(context,addData);
        listView.setAdapter(addCustomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                pos=position;
                switch (position){
                    case 0:
                        Intent intentAlarmTime=new Intent(context, AlarmTimeChooseActivity.class);
                        startActivityForResult(intentAlarmTime,AlarmTimeChoose);
                        break;
                    case 1:
                        Intent intentCategory=new Intent(context,CategoryChooseActivity.class);
                        startActivityForResult(intentCategory,CategoryChoose);
                        break;
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    clear_name.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                }
                else {
                    clear_name.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }
            }
        });
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    clear_day.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                }
                else {
                    clear_day.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }
            }
        });
        //处理子线程发来的消息
        handler=new Handler(){
            public void handleMessage(Message message){
                if(message.what==SHOW_EXIST){
                    Toast.makeText(AddActivity.this,"习惯养成计划中已经有这个习惯了呢，请重新输入吧！",Toast.LENGTH_SHORT).show();
                }
                else if(message.what==CREATE_CUSTOM){
                    Toast.makeText(AddActivity.this,"添加新习惯成功！",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    /*
    * 设置提醒时间和分类的初始化数据
    * */
    private void initAddCustom(){
        addData=new LinkedList<AddCustom>();
        AddCustom addAlarm=new AddCustom(R.mipmap.alarm,"提醒时间","请设置提醒时间",R.mipmap.enter);
        addData.add(addAlarm);
        AddCustom addCategory=new AddCustom(R.mipmap.category2,"分类","效率",R.mipmap.enter);
        addData.add(addCategory);
    }
    /*
    * 得到其它活动的返回值的回调
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case AlarmTimeChoose:
                if(resultCode==RESULT_OK){
                    //处理AlarmTimeChooseActivity中穿过来的数据
                    re=data.getStringExtra("alarmTime");
                    alarm_time=re;
                    Log.d("alarmTime",String.valueOf(pos)+","+re);
                    addCustomAdapter.setText(re,pos);
                    //addCustomAdapter.notifyDataSetChanged();
                }
                break;
            case CategoryChoose:
                if(resultCode==RESULT_OK){
                    //处理CategoryChooseActivity中穿过来的数据
                    re=data.getStringExtra("category");
                    category=re;
                    Log.d("alarmTime",String.valueOf(pos)+","+re);
                    addCustomAdapter.setText(re,pos);
                    //addCustomAdapter.notifyDataSetChanged();
                }
                break;
        }
        addCustomAdapter.notifyDataSetChanged();
    }
    /*
    * 防止输入框中的信息丢失
    * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        custom_name=((EditText)findViewById(R.id.name)).getText().toString();
        target_day=((EditText)findViewById(R.id.day)).getText().toString();
        outState.putString("customName", custom_name);
        outState.putString("insistDay",target_day);
    }
    /*
    * 重写物理返回键
    * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //使用这个方法会让MainActivity再次create
        Intent intent=new Intent(AddActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_custom:
                //将四个值放在一个Map中，
                userId=String.valueOf(MainFragment.userId);
                Log.d("user_id",userId);
                new_custom=new HashMap<String, String>();
                new_custom.put("user_id",userId);
                new_custom.put("custom_name",custom_name);
                new_custom.put("alarm_time",alarm_time);
                new_custom.put("target_day",target_day);
                new_custom.put("category",category);
                //查询当前用户是否已经存在该习惯
                try {
                    parameter="custom?userName=" + URLEncoder.encode(userName,"utf-8")+"&customName="+ URLEncoder.encode(custom_name,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                /*
                * 查询是否已经存在这条习惯
                * */
                HttpUtil.sendRequestWithHttpClient(baseUrl+parameter,new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response){
                        try {
                            Map<String,String> re=HttpUtil.convertToMapWithJSON(response);
                            String result=re.get("isExist");
                            Log.d("result",result);
                            //该习惯已经存在
                            if(result.equals("custom exist")){
                                //Toast
                                Log.d("isExist",re.get("isExist"));
                                Message message=new Message();
                                message.what=SHOW_EXIST;
                                handler.sendMessage(message);
                            }
                            else{
                                HttpUtil.postRequest(baseUrl, new_custom, new HttpCallbackListener() {
                                    @Override
                                    public void onFinish(String response) {
                                        if(response.equals("success"))
                                        {
                                            Log.d("....","success");
                                            Message message=new Message();
                                            message.what=CREATE_CUSTOM;
                                            handler.sendMessage(message);
                                        }
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("AddActivity","请求服务器失败！");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("AddActivity","请求服务器失败！");
                    }
                });
                break;
            case R.id.clear_name:
                name.setText("");
                break;
            case R.id.clear_day:
                day.setText("");
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }
}
