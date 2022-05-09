package com.example.courts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class MainCaseInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_case_info, container, false);
        ArrayList<String> info = getArguments().getStringArrayList("info");
        ArrayList<ArrayList<String>> info_listview = new ArrayList<>();
        for (int i = 0; i < info.size(); i+=2) {
            if (!info.get(i).equals("") && !info.get(i+1).equals("")) {
                info_listview.add(new ArrayList<>(Arrays.asList(info.get(i), info.get(i + 1))));
            }
        }



        ListView listView = view.findViewById(R.id.listview);
        listView.setAdapter(new Court_info_adapter(getContext(), info_listview));
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, info_listview);
        //listView.setAdapter(adapter);


        return view;
    }
}