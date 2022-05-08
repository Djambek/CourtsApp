package com.example.courts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DataBase db = new DataBase(getContext());
        ArrayList<String> ids = db.get_all_id();
        Log.d("__Info", "---------------");
        for (String id : ids){
            Log.d("__Info", String.valueOf(db.getShortInfo(id)));
        }

        ListView listview = view.findViewById(R.id.listview);
        listview.setAdapter(new Short_info_db_adapter(getContext(), ids));

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
