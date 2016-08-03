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
                String custom = "学习";
                String user_name = "CANDY";

                Intent intent = new Intent();
                intent.putExtra("show_custom", custom);
                intent.putExtra("show_user_name", user_name);
                intent.setClass(MainActivity.this, show_custom.class);
                startActivity(intent);

                /*从活动2中获取数据
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivityForResult(intent, 1);
                */
            }
        });
    }
}