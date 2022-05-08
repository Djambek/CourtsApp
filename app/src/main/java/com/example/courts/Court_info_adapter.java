package com.example.courts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    ArrayList<ArrayList<Object>> case_info;
    Court_info_adapter(Context context, ArrayList<ArrayList<Object>> case_info){
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
        ArrayList<Object> i_case = case_info.get(i);
        if (i_case.size() == 4){
            tw.setText(Html.fromHtml("<font><b>" + i_case.get(0) + ": " + "</b></font>" + "<font color='#0066FF'>" + i_case.get(1) + "</font>"));
            tw.setBackground((Drawable) i_case.get(3));
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CourtCase.class);
                    Log.d("SEARCH_IN_ADAPTER", String.valueOf(i_case));
                    Toast.makeText(context, "Загрузка...", Toast.LENGTH_SHORT).show();
                    intent.putExtra("link", String.valueOf(i_case.get(2)));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        }else{
            tw.setText(Html.fromHtml("<font><b>" + i_case.get(0)  + "</b></font>" + i_case.get(1)));
            tw.setBackground((Drawable) i_case.get(2));
        }

        return my_view;
    }
}
