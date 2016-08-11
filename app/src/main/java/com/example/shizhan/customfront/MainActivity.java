package com.example.shizhan.customfront;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private MainFragment main_fg;
    private AddFragment add_fg;
    //定义其它碎片，如添加和个人中心
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getSupportActionBar().hide();//隐藏标题栏，API版本24
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//API版本21
        setContentView(R.layout.activity_main);

        fManager = getFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_add);
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
                if(add_fg == null){
                    add_fg = new AddFragment();
                    fTransaction.add(R.id.ly_content,add_fg);
                }else{
                    fTransaction.show(add_fg);
                }
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
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(main_fg != null){
            Log.d("main_fg 存在","隐藏");
            fragmentTransaction.hide(main_fg);
        }
        if(add_fg != null){
            Log.d("add_fg 存在","隐藏");
            fragmentTransaction.hide(add_fg);
        }
//        if(fg3 != null)fragmentTransaction.hide(fg3);
//        if(fg4 != null)fragmentTransaction.hide(fg4);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
