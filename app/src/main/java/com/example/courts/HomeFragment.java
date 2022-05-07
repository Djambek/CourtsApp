package com.example.courts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listview = view.findViewById(R.id.listview);
        Integer[] i = new Integer[100];
        for (int j = 0; j < 100; j++) {
            i[j] = j;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1, i);
        listview.setAdapter(adapter);

        FloatingActionButton b = view.findViewById(R.id.button_add_new_case);
        b.bringToFront();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
