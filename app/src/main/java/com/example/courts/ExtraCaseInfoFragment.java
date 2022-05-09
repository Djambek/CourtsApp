package com.example.courts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ExtraCaseInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_extra_case_info, container, false);
        ArrayList<String> history = getArguments().getStringArrayList("history");
        ArrayList<ArrayList<String>> history_listview = new ArrayList<>();
        for (int i = 0; i < history.size(); i+=3) {
            history_listview.add(new ArrayList<>(Arrays.asList(history.get(i), history.get(i+1), history.get(i+2))));
        }
        ArrayList<String> history_place = getArguments().getStringArrayList("history_place");
        ArrayList<String> documents = getArguments().getStringArrayList("documents");
        ArrayList<String> sessions = getArguments().getStringArrayList("sessions");

        ListView listView_history = view.findViewById(R.id.listview_history);

        return view;
    }
}