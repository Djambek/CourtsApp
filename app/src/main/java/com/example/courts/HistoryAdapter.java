package com.example.courts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    private final ArrayList<ArrayList<String>> history;
    private final Context context;

    HistoryAdapter(Context context, ArrayList<ArrayList<String>> history){
        this.history = history;
        this.context = context;
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int i) {
        return history.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View my_view = LayoutInflater.from(context).inflate(R.layout.history_adapter, null);

        TextView date = my_view.findViewById(R.id.text_date);
        date.setText("Дата: "+history.get(i).get(0));

        TextView status = my_view.findViewById(R.id.text_status);
        status.setText("Статус: "+history.get(i).get(1));

        TextView document = my_view.findViewById(R.id.text_document);
        document.setText("Документ: "+history.get(i).get(2));

        return my_view;
    }
}
