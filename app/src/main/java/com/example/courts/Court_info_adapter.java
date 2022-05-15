package com.example.courts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Court_info_adapter extends BaseAdapter {
    Context context;
    ArrayList<ArrayList<String>> case_info;
    Court_info_adapter(Context context, ArrayList<ArrayList<String>> case_info){
        this.context = context;
        this.case_info = case_info;
    }
    @Override
    public int getCount() {
        return case_info.size();
    }

    @Override
    public Object getItem(int i) {
        return case_info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View my_view = LayoutInflater.from(context).inflate(R.layout.court_info_adapter_view, null);
        TextView tw = my_view.findViewById(R.id.textview_adapet_case_info);
        ArrayList<String> i_case = case_info.get(i);
        tw.setText(Html.fromHtml("<font><b>" + i_case.get(0)  + "</b></font>" + i_case.get(1)));
        if(i_case.size() > 2){tw.setBackground(null);}

        return my_view;
    }
}
