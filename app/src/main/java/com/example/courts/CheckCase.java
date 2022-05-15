package com.example.courts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckCase {
    JSONObject jsonObject = null;
    public void checkUpdate(Context context, String id){
        DataBase db = new DataBase(context);
        String link = db.getLink(id);

        ArrayList<String> from_db_main_info = db.getMainInfo(id);
        ArrayList<String> from_db_info = new ArrayList<>();
        Integer j=0;
        while( j < from_db_main_info.size()){
            if (!from_db_main_info.get(j).contains(":")){
                Log.d("FR", from_db_main_info.get(j));
                from_db_info.add(from_db_main_info.get(j));
            }
            j++;

        }
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Document res = Jsoup.connect("http://ctf.djambek.com:8080/details?link="+link).maxBodySize(0).ignoreContentType(true).get();
                    jsonObject = new JSONObject(res.text());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] args = new String[]{"id", "number", "number_input_document",
                "register_date", "date_hearing_first_instance",
                "date_of_appellate_instance", "result_hearing", "number_in_next_instance", "number_in_last_instance", "judge", "category",
                "status", "article", "resons_solve", "date_of_decision"};
        ArrayList<String> for_db = new ArrayList<>();
        for(int i=0; i<args.length; i++){
            try {
                if (!jsonObject.getString(args[i]).equals("")){
                    for_db.add(jsonObject.getString(args[i]));
                }else{
                    for_db.add("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("FROM DB", String.valueOf(from_db_info));
        Log.d("FROM_INTERN", String.valueOf(for_db));
        ArrayList<ArrayList<String>> part_list = new ArrayList<>();
        try {
            JSONArray parts = jsonObject.getJSONArray("participants");
            for (int i = 0; i < parts.length(); i++) {
                String type = parts.getJSONObject(i).getString("type");
                String name = parts.getJSONObject(i).getString("name");
                part_list.add(new ArrayList<>(Arrays.asList(type, name)));
            }
        } catch (JSONException e) {
            part_list.add(new ArrayList<>(Arrays.asList("", "")));
            e.printStackTrace();
        }

        ArrayList<String> history = new ArrayList<>();
        try {
            JSONArray history_json = jsonObject.getJSONArray("history");
            for(int i=0; i<history_json.length(); i++){
                String date = history_json.getJSONObject(i).getString("date");
                history.add(date);
                String status = history_json.getJSONObject(i).getString("status");
                history.add(status);
                String document = history_json.getJSONObject(i).getString("document");
                history.add(document);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // парсим историю местонахождений
        ArrayList<String> history_place = new ArrayList<>();
        try {
            JSONArray history_place_json = jsonObject.getJSONArray("places_history");
            for (int i = 0; i < history_place_json.length(); i++) {
                history_place.add(history_place_json.getJSONObject(i).getString("date"));
                history_place.add(history_place_json.getJSONObject(i).getString("place"));
                history_place.add(history_place_json.getJSONObject(i).getString("comment"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // парсим судебные заседания и и беседы
        ArrayList<String> sessions = new ArrayList<>();
        try {
            JSONArray sessions_json = jsonObject.getJSONArray("sessions");
            for (int i = 0; i < sessions_json.length(); i++) {
                sessions.add(sessions_json.getJSONObject(i).getString("date"));
                sessions.add(sessions_json.getJSONObject(i).getString("hall"));
                sessions.add(sessions_json.getJSONObject(i).getString("stage"));
                sessions.add(sessions_json.getJSONObject(i).getString("result"));
                sessions.add(sessions_json.getJSONObject(i).getString("reson"));
                sessions.add(sessions_json.getJSONObject(i).getString("video"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // парсим судебные акты
        ArrayList<String> documents = new ArrayList<>();
        try {
            JSONArray documents_json = jsonObject.getJSONArray("documents");
            for (int i = 0; i < documents_json.length(); i++) {
                documents.add(documents_json.getJSONObject(i).getString("date"));
                documents.add(documents_json.getJSONObject(i).getString("kind_document"));
                documents.add(documents_json.getJSONObject(i).getString("document_text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
