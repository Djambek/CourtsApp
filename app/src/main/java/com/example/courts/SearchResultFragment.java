package com.example.courts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SearchResultFragment extends Fragment {
    Integer pages;
    JSONArray result_array;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult, container, false);
        Log.d("__FRAGMENT_", "мы в начале фрагмента");
        try {
            Log.d("CONTENT_IN_FRAGMENT", getArguments().getString("json"));
            result_array = new JSONObject(getArguments().getString("json")).getJSONArray("cases");
            pages = new JSONObject(getArguments().getString("json")).getInt("pages");
            Log.d("FRAMGENT", "мы в фрагменте");
            ListView list = view.findViewById(R.id.listview);
            list.setAdapter(new ShortInfoAdapterJson(getContext(), result_array));
            Log.d("SELECTED", "now we start to choice item");

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), CourtCase.class);
                    try {
                       intent.putExtra("link", result_array.getJSONObject(i).getString("url"));
                    } catch (JSONException e) {
                        Log.e("ERROR", e.toString());
                    }
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
            if (e.toString().contains("Unterminated string")){
                TextView error = view.findViewById(R.id.text_error);
                error.setText("Произошла ошибка.Поробуйте обновить.");
                Log.d("TEXT", "Error");
            }
        }


        return view;
    }
}
