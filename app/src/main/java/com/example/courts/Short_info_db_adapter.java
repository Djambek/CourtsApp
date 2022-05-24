package com.example.courts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Short_info_db_adapter extends BaseAdapter {
    ArrayList<Boolean> colors;
    Context context;
    ArrayList<String> ids;
    DataBase db;

    Short_info_db_adapter(Context context, ArrayList<String> ids, ArrayList<Boolean> colors){
        this.ids = ids;
        this.context = context;
        this.colors = colors;
        db = new DataBase(context);
    }
    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int i) {
        return ids.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View my_view = LayoutInflater.from(context).inflate(R.layout.short_info_db_adapterview, null);
        ArrayList<Object> info = db.getShortInfo(ids.get(i));

        TextView number = my_view.findViewById(R.id.text_spinner_number_case);
        number.setText((String) info.get(0));

        ArrayList<String> plain = (ArrayList<String>) info.get(1);
        ArrayList<String> def = (ArrayList<String>) info.get(2);

        TextView text_plain = my_view.findViewById(R.id.text_spinner_plaintiff);
        text_plain.setText(plain.get(0) + plain.get(1));

        TextView text_def = my_view.findViewById(R.id.text_spinner_defendant);
        text_def.setText(def.get(0)+ def.get(1));

        if (colors.get(i)){
            number.setBackgroundColor(Color.GREEN);
            text_def.setBackgroundColor(Color.GREEN);
            text_plain.setBackgroundColor(Color.GREEN);
            my_view.setBackgroundColor(Color.GREEN);
        }

        return my_view;
    }
}
