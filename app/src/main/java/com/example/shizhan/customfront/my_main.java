package com.example.shizhan.customfront;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by candy on 2016/8/5.
 */


public class my_main extends AppCompatActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymainlayout);

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 向后一活动传入数据
                String custom_id = "1";
                String custom_name = "学习";
                String current_insist_day = "1";//当前连续天数
                String max_insist_day = "6";//最大连续天数
                String insist_day = "21";//已坚持天数
                String target_day = "21";//目标天数
//                String clock_in = "clockin";
                String clock_in = "";

                Intent intent = new Intent();
                intent.setClass(my_main.this, show_custom.class);

                intent.putExtra("show_custom_id", custom_id);
                intent.putExtra("show_custom_name", custom_name);
                intent.putExtra("show_current_insist_day", current_insist_day);
                intent.putExtra("show_max_insist_day", max_insist_day);
                intent.putExtra("show_insist_day", insist_day);
                intent.putExtra("show_target_day", target_day);
                intent.putExtra("show_clock_in", clock_in);
                //返回时从活动show_custom中获取数据
                startActivityForResult(intent, 1);
            }
        });
    }
    //对从活动show_custom中返回的数据进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("my_main", returnedData);
                }
                break;
            default:
        }
    }
}
