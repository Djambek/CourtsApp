package com.example.courts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<String> ids;
    DataBase db;
    ListView listview;


    @Override
    public void onResume() {
        ids = db.get_all_id();
        Log.d("__Info", "---------------");
        for (String id : ids){
            Log.d("__Info", String.valueOf(db.getShortInfo(id)));
        }

        Log.d("IDS", String.valueOf(ids));
        listview.setAdapter(new Short_info_db_adapter(getContext(), ids));

        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DataBase(getContext());
        ids = db.get_all_id();
        CheckCase checkCase = new CheckCase();
        checkCase.checkUpdate(getContext(), ids.get(0));

        listview = view.findViewById(R.id.listview);
        Log.d("_ID", String.valueOf(ids));
        listview.setAdapter(new Short_info_db_adapter(getContext(), ids));
        Log.d("S__", "help");
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("S__", String.valueOf(adapterView.getItemAtPosition(i)));

                String id = (String) adapterView.getItemAtPosition(i);
                Log.d("History_", String.valueOf(db.getHistory(id)));
                Log.d("Place History_", String.valueOf(db.getPlaceHistory(id)));
                Log.d("Sessions_", String.valueOf(db.getSessions(id)));
                Log.d("Documents_", String.valueOf(db.getDocument(id)));
                Log.d("ASDF", "Создался новый фрагмент");
                //CourtCaseDBFragment fragment = new CourtCaseDBFragment(db.getMainInfo(id), db.getHistory(id), db.getPlaceHistory(id), db.getSessions(id), db.getDocument(id));
                Log.d("Main Info_", String.valueOf(db.getMainInfo(id)));

                Intent intent = new Intent(getContext(), CourtCaseDB.class);
                intent.putStringArrayListExtra("main_info", db.getMainInfo(id));
                intent.putStringArrayListExtra("history", db.getHistory(id));
                intent.putStringArrayListExtra("history_place", db.getPlaceHistory(id));
                intent.putStringArrayListExtra("sessions", db.getSessions(id));
                intent.putStringArrayListExtra("documents", db.getDocument(id));
                startActivity(intent);
                getActivity().getFragmentManager().popBackStackImmediate();
                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                // попробовать сделать через активность!
//                Log.d("Count", getActivity().getSupportFragmentManager().getBackStackEntryCount()+"");
//                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0){
//                    Log.d("Imortant","Удаляем фрагмент");
//                    getActivity().getSupportFragmentManager().popBackStack();}
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, fragment)
//                            .addToBackStack(null)
//                            .commit();
//
//                Log.d("Count", getActivity().getSupportFragmentManager().getBackStackEntryCount()+"");



                //Intent intent = new Intent(getContext(), CourtCaseDB.class);
                //intent.putStringArrayListExtra();
            }
        });
//        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("S__", String.valueOf(adapterView.getItemAtPosition(i)));
//                Log.d("S__", "a");
//                Log.d("S__", i+"");
//                Toast.makeText(getContext(), "ASDF", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

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
