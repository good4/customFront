package com.example.shizhan.customfront;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    public static final String TAG = "MainActivity";

    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private MainFragment main_fg;
    //private AddFragment add_fg;
    //定义其它碎片，如添加和个人中心
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	//this.getSupportActionBar().hide();//隐藏标题栏，API版本24
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//API版本21
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");
        fManager = getFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_custom);
        rb_channel.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_custom:
                if(main_fg == null){
                    main_fg = new MainFragment();
                    fTransaction.add(R.id.ly_content,main_fg);
                }else{
                    fTransaction.show(main_fg);
                }
                break;
            case R.id.rb_add:
//                开启新的活动
                Log.d("in add","in add");
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;
            case R.id.rb_my:
//                if(fg3 == null){
//                    fg3 = new MyFragment("第三个Fragment");
//                    fTransaction.add(R.id.ly_content,fg3);
//                }else{
//                    fTransaction.show(fg3);
//                }
//                break;
        }
//        if(checkedId!=R.id.rb_add)
//            fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    //隐藏所有Fragment
    protected void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(main_fg != null){
            Log.d("main_fg 存在","隐藏");
            fragmentTransaction.hide(main_fg);
        }
//        if(add_fg != null){
//            Log.d("add_fg 存在","隐藏");
//            fragmentTransaction.hide(add_fg);
//        }
//        if(fg3 != null)fragmentTransaction.hide(fg3);
//        if(fg4 != null)fragmentTransaction.hide(fg4);
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
/*=======
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 向后一活动传入数据
                String custom_id = "1";
                String custom_name = "学习";
                String current_insist_day = "1";//当前连续天数
                String max_insist_day = "6";//最大连续天数
                String  insist_day = "8";//已坚持天数
                String target_day = "21";//目标天数

                // 今天是否完成

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, show_custom.class);

                intent.putExtra("show_custom_id", custom_id);
                intent.putExtra("show_custom_name", custom_name);
                intent.putExtra("show_current_insist_day", current_insist_day);
                intent.putExtra("show_max_insist_day", max_insist_day);
                intent.putExtra("show_insist_day", insist_day);
                intent.putExtra("show_target_day", target_day);

                startActivity(intent);

                    //将bundle传入intent中。
                /*从活动2中获取数据
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivityForResult(intent, 1);
                */
            }
        });
    }
}
>>>>>>> zd*/
