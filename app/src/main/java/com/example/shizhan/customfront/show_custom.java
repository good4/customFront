package com.example.shizhan.customfront;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhan.customfront.calendar.CalendarCard;
import com.example.shizhan.customfront.calendar.CalendarViewAdapter;
import com.example.shizhan.customfront.calendar.Custom;
import com.example.shizhan.customfront.calendar.CustomDate;
import com.example.shizhan.customfront.calendar.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;


/**
 * Created by candy on 2016/8/1.
 */
public class show_custom extends AppCompatActivity implements CalendarCard.OnCellClickListener{

    private TextView toolbar_title;
    private ViewPager mViewPager;
    private TextView monthText;
    private int mCurrentIndex = 498;
    private CalendarViewAdapter<CalendarCard> adapter;
    private List<Custom> listDay;
    private CalendarCard[] views;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    private LinearLayout indicatorLayout;
    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE
    }
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.show_custom);

         //获取传来的数据
        Intent intent = getIntent();
        String custom= intent.getStringExtra("show_custom");
        String user_name = intent.getStringExtra("show_user_name");
        toolbar_title =(TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(custom);

        Button button_back = (Button) findViewById(R.id.back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*向活动1传入数据
                Intent intent = new Intent();
                intent.putExtra("data_return", "Hello FirstActivity");
                setResult(RESULT_OK, intent);
                finish();
                */
                finish();
            }
        });
        Button button_clock = (Button) findViewById(R.id.clock);
        button_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(show_custom.this,
                        ThirdActivity.class);
                startActivity(intent);
            }
        });

        Button button_clockin = (Button) findViewById(R.id.clockin);
        button_clockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(show_custom.this,"恭喜你，打卡成功！",Toast.LENGTH_SHORT).show();//通过Toast输出
            }
        });

/*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK, intent);
        finish();
    }
    */


        mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        monthText = (TextView) findViewById(R.id.tvCurrentMonth);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_drop);
        int month = DateUtil.getCurrentMonthNow();
        int year = DateUtil.getCurrentYeatNow();
        CustomDate c = new CustomDate(year, month, 1);
        monthText.setText(showTimeCount(c));
        initData();
    }

    private void initData() {
        listDay = new ArrayList<>();
        for (int i = 1;i<5;i++){
            Custom custom = new Custom(2016,7,i);
            listDay.add(custom);
        }
        for (int i = 1;i<5;i++){
            Custom custom = new Custom(2016,7+i,i);
            listDay.add(custom);
        }
        views = new CalendarCard[6];
        for (int i = 0; i < 6; i++) {
            views[i] = new CalendarCard(this, this, listDay);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();

        CustomDate c = new CustomDate(DateUtil.getCurrentYeatNow(), DateUtil.getCurrentMonthNow(), DateUtil.getCurrentMonthDay());


    }

    @Override
    public void clickDate(CustomDate date) {
        Toast.makeText(this,showTimeCountAll(date),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void changeDate(CustomDate date) {

    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
//        initCategoryBarPoint(indicatorLayout);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
//                setIndicator(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initCategoryBarPoint(LinearLayout indicatorLayout) {

        indicatorLayout.removeAllViews();

        android.widget.LinearLayout.LayoutParams lp;
        for (int i = 0; i < views.length; i++) {
            View v = new View(this);
            v.setBackgroundResource(R.drawable.point_background_calendar);
            v.setEnabled(false);
            lp = new android.widget.LinearLayout.LayoutParams(dip2px(getApplicationContext(), 12), dip2px(getApplicationContext(), 4));
            lp.leftMargin = dip2px(getApplicationContext(), 3);
            lp.rightMargin = dip2px(getApplicationContext(), 3);
            v.setLayoutParams(lp);
            indicatorLayout.addView(v);
        }

        if (indicatorLayout.getChildCount() > 0)
            indicatorLayout.getChildAt(0).setEnabled(true);

    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < views.length; i++) {
            indicatorLayout.getChildAt(i).setEnabled(false);
        }
        if (views.length > selectedPosition)
            indicatorLayout.getChildAt(selectedPosition).setEnabled(true);

    }
    /* 计算方向
   *
    */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }
    // 更新日历视图
    private void updateCalendarView(int arg0) {
        CustomDate customDate = new CustomDate();
        CalendarCard[] mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            customDate = mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            customDate = mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
        if (customDate != null) {
            monthText.setText(showTimeCount(customDate));
            //进行网络请求

        }


    }
    public String showTimeCount(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + "年" + minue + "月";
        return timeCount;
    }

    public String showTimeCountAll(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + minue + sec;
        return timeCount;
    }

    public int dip2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        //dp = px/density
        int px = (int) (dp * density + 0.5f);
        return px;
    }
}

