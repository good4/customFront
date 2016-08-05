package com.example.shizhan.customfront.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shizhan.customfront.MainActivity;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.Custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shizhan on 16/7/25.
 */
public class CustomAdapter extends BaseAdapter {
    private Context cContext;
    private List<Custom> cData;
    private HashMap<Integer,Bitmap> images;

    public CustomAdapter(List<Custom> cData, HashMap<Integer,Bitmap> images,Context cContext) {
        this.cData = cData;
        this.images=images;
        this.cContext = cContext;
    }

    @Override
    public int getCount() {
        return cData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("in the AddCustomAdapter","in getView");
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(cContext).inflate(R.layout.list_item_card,parent,false);
            holder = new ViewHolder();
            holder.cIcon = (ImageView) convertView.findViewById(R.id.custom_icon);
            holder.cName = (TextView) convertView.findViewById(R.id.custom_name);
            holder.cInsist_day=(TextView)convertView.findViewById(R.id.insist_day);
            holder.alarm_time=(TextView)convertView.findViewById(R.id.alarm_time);
            holder.recorded=(ImageView)convertView.findViewById(R.id.recorded);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();//将Holder存储到convertView中,不用每次调findViewById
        }
        holder.cIcon.setImageBitmap(images.get(position));
        holder.cName.setText(cData.get(position).getCustom_name());
        holder.cInsist_day.setText("已坚持"+cData.get(position).getInsist_day()+"天");
        //比较过时间后判断是否打卡
        holder.recorded.setImageResource(R.mipmap.check1);
        Log.d("position"+position,cData.get(position).getAlarm_time());
        if(cData.get(position).getAlarm_time().equals("不提醒")) {
            Log.d("tixing",cData.get(position).getAlarm_time());
            holder.alarm_time.setText("");
        }
        else{
            holder.alarm_time.setText(" 提醒 "+cData.get(position).getAlarm_time());
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView cIcon;
        TextView cName;
        TextView cInsist_day;
        TextView alarm_time;
        ImageView recorded;
    }
}

