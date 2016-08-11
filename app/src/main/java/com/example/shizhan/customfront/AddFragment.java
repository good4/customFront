package com.example.shizhan.customfront;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.shizhan.customfront.adapter.AddCustomAdapter;
import com.example.shizhan.customfront.model.AddCustom;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shizhan on 16/7/30.
 */
public class AddFragment extends Fragment{

    private List<AddCustom> addData;
    private AddCustomAdapter addCustomAdapter;
    private Context context;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_add,container,false);
        Toolbar toolbar=(Toolbar)view.findViewById(R.id.add_toolbar);
        //设置返回键
        toolbar.setNavigationIcon(R.mipmap.back);

        listView=(ListView) view.findViewById(R.id.add_list_view);
        context=getActivity();
        initAddCustom();
        //test
//        if(addData.size()!=0)
//            Log.d("addData",String.valueOf(addData.size()));

        addCustomAdapter=new AddCustomAdapter(context,addData);
        listView.setAdapter(addCustomAdapter);
        return view;
    }
    private void initAddCustom(){
        addData=new LinkedList<AddCustom>();
        AddCustom addAlarm=new AddCustom(R.mipmap.alarm,"提醒时间","请设置提醒时间",R.mipmap.enter);
        addData.add(addAlarm);
        AddCustom addCategory=new AddCustom(R.mipmap.category2,"分类","生活",R.mipmap.enter);
        addData.add(addCategory);
    }
}
