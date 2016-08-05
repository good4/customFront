package com.example.shizhan.customfront;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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