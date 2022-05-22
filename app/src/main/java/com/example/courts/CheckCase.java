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
    static ArrayList<String> participants; // участники из таблицы

    public void checkUpdate(Context context, String id) {
        DataBase db = new DataBase(context);
        String link = db.getLink(id);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Document res = Jsoup.connect("http://ctf.djambek.com:8080/details?link=" + link).maxBodySize(0).ignoreContentType(true).get();
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
        Log.d("EQUALS MAIN INFO", String.valueOf(diffMainInfo(db, id, jsonObject)));
        Log.d("EQUALS PARTICIPANTS", String.valueOf(diffParticipants(participants, jsonObject)));
        Log.d("EQUALS HISTORY", String.valueOf(diffHistory(db, id, jsonObject)));
        Log.d("EQUALS HISTORY PLACE", String.valueOf(diffHistoryPlace(db, id, jsonObject)));
        Log.d("EQUALS SESSIONS", String.valueOf(diffSessions(db, id, jsonObject)));
        Log.d("EQUALS DOCUMENTS", String.valueOf(diffDocuments(db, id, jsonObject)));



    }

    public static boolean diffParticipants(ArrayList<String> participants, JSONObject jsonObject){
        ArrayList<String> part_list = new ArrayList<>();

        try {
            JSONArray parts = jsonObject.getJSONArray("participants");
            for (int i = 0; i < parts.length(); i++) {
                String name = parts.getJSONObject(i).getString("name");
                part_list.add(name);
            }
        } catch (JSONException e) {
            part_list.add("");
            e.printStackTrace();
        }

        Log.d("FROM_DB_PARTS", String.valueOf(participants));
        Log.d("FROM_INT_PATS", String.valueOf(part_list));

        if (participants.equals(part_list)) {
           return true;
        }
        return false;
    }

    public static boolean diffDocuments(DataBase db, String id, JSONObject jsonObject){
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
        if (db.getDocument(id).equals(documents)) {
            return true;
        }
        return false;

    }

    public static boolean diffHistoryPlace(DataBase db, String id, JSONObject jsonObject){
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
        if (db.getPlaceHistory(id).equals(history_place)){
            return true;
        }
        return false;
    }

    public static boolean diffSessions(DataBase db, String id, JSONObject jsonObject){
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
        Log.d("FROM_DB_SESSIONS", String.valueOf(db.getSessions(id)));
        Log.d("FROM_INTERN_SESSIONS", String.valueOf(sessions));

        if (db.getSessions(id).equals(sessions)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean diffHistory(DataBase db, String id, JSONObject jsonObject){
        Log.d("FROM_HISTORY", String.valueOf(db.getHistory(id)));

        ArrayList<String> history = new ArrayList<>();
        try {
            JSONArray history_json = jsonObject.getJSONArray("history");
            for (int i = 0; i < history_json.length(); i++) {
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
        Log.d("FROM_HISTORY_INTER", String.valueOf(history));
        if (db.getHistory(id).equals(history)) {
           return true;
        }
        return false;
    }

    public static boolean diffMainInfo(DataBase db, String id, JSONObject jsonObject) {
        ArrayList<String> from_db_main_info = db.getMainInfo(id);
        Log.d("FROM", String.valueOf(from_db_main_info));
        ArrayList<String> from_db_info = new ArrayList<>();
        for (int j = 1; j < from_db_main_info.size(); j += 2) {
            from_db_info.add(from_db_main_info.get(j));
        }
        Log.d("FROM", String.valueOf(from_db_info));
        participants = new ArrayList<>(Arrays.asList(from_db_info.get(4), from_db_info.get(3)));
        // Удаляем участников
        from_db_info.remove(3);
        from_db_info.remove(3);

        String[] args = new String[]{"id", "number", "number_input_document",
                "register_date", "date_hearing_first_instance",
                "date_of_appellate_instance", "result_hearing", "number_in_next_instance", "number_in_last_instance", "judge", "category",
                "status", "article", "resons_solve", "date_of_decision"};
        ArrayList<String> from_internet = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            try {
                if (!jsonObject.getString(args[i]).equals("")) {
                    from_internet.add(jsonObject.getString(args[i]));
                } else {
                    from_internet.add("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("FROM DB", String.valueOf(from_db_info));
        Log.d("FROM_INTERN", String.valueOf(from_internet));
        if (from_db_info.equals(from_internet)) {
            return true;
        }
        return false;
    }


}
