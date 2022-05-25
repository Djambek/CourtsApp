package com.example.courts;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;


import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class CheckCase extends Worker{
    static JSONObject jsonObject = null;
    public CheckCase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    static public void checkUpdate(DataBase db, String id, Context context) {
        String link = db.getLink(id);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Document res = Jsoup.connect("http://ctf.djambek.com:8080/details?link=" + link).maxBodySize(0).ignoreContentType(true).get();
                    jsonObject = new JSONObject(res.text());
                } catch (IOException | JSONException e) {
                    Log.d("JSON", "WAS ERROR F");
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
        Log.d("JSON", String.valueOf(jsonObject));


        Log.d("EQUALS MAIN INFO", String.valueOf(equalMainInfo(db, id, jsonObject)));
        Log.d("EQUALS PARTICIPANTS", String.valueOf(equalParticipants(db, id, jsonObject)));
        Log.d("EQUALS HISTORY", String.valueOf(equalHistory(db, id, jsonObject)));
        Log.d("EQUALS HISTORY PLACE", String.valueOf(equalHistoryPlace(db, id, jsonObject)));
        Log.d("EQUALS SESSIONS", String.valueOf(equalSessions(db, id, jsonObject)));
        Log.d("EQUALS DOCUMENTS", String.valueOf(equalDocuments(db, id, jsonObject)));
        updateCase(db, id, jsonObject, context);

    }
    public static boolean updateCase(DataBase db, String id, JSONObject jsonObject, Context context){
        ArrayList<String> info = equalMainInfo(db, id, jsonObject);
        ArrayList<String> parts = equalParticipants(db, id, jsonObject);
        ArrayList<String> history = equalHistory(db, id, jsonObject);
        ArrayList<String> history_place = equalHistoryPlace(db, id, jsonObject);
        ArrayList<String> sessions = equalSessions(db, id, jsonObject);
        ArrayList<String> documents = equalDocuments(db, id, jsonObject);
        String notification = null;
        String number = db.getNumberCase(id);
        boolean needUpdate = false;



//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");


        if(!info.get(0).equals("true")){
            notification = "Изменилась главная информация по делу" ;
            needUpdate = true;
        }else{
            info = db.getOnlyInfo(id);
        }
        if(!parts.get(0).equals("true")){
            notification = "Изменились участники";
            needUpdate = true;
        }else{
            parts = db.getParticipants(id);
        }
        if(!history.get(0).equals("true")){
            notification = "Изменилась история состояний";
            needUpdate = true;
        }else{
            history = db.getHistory(id);
        }
        if(!history_place.get(0).equals("true")){
            notification = "Изменилась история местонахождения";
            needUpdate = true;
        }else{
            history_place = db.getPlaceHistory(id);
        }
        if(!sessions.get(0).equals("true")){
            notification = "Изменились судебные заседания";
            needUpdate = true;
        }else{
            sessions = db.getSessions(id);
        }
        if(!documents.get(0).equals("true")){
            notification = "Изменились судебные акты";
            needUpdate = true;
        }else{
            documents = db.getDocument(id);
        }

        if(needUpdate){
            String link = db.getLink(id);
            db.delete(id);
            ArrayList<ArrayList<String>> part_for_db = new ArrayList<>();
            Log.d("UPDATE PARTS", String.valueOf(parts));
            for (int i = 0; i < parts.size(); i+=2) {
                part_for_db.add(new ArrayList<>(Arrays.asList(parts.get(i), parts.get(i+1))));
            }
            db.addNewCase(info.get(0), info.get(1), info.get(2),
                    info.get(3), info.get(4), info.get(5),
                    info.get(6), info.get(7), info.get(8),
                    info.get(9), info.get(10), info.get(11),
                    info.get(12), info.get(13), info.get(14),
                    link, part_for_db);
            db.addHistory(id, history);
            db.addPlaceHistory(id, history_place);
            db.addDocuments(id, documents);
            db.addSessions(id, sessions);
            db.addColor(id);
            db.close();
            // notification = "Типа уведомление";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My notification");
            builder.setContentTitle(number);
            builder.setContentText(notification);
            builder.setSmallIcon(R.drawable.ic_court);
            builder.setAutoCancel(true);

            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            builder.setContentIntent(resultPendingIntent);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }
        db.close();
        return needUpdate;
    }

    public static ArrayList<String> equalParticipants(DataBase db, String id, JSONObject jsonObject){
        ArrayList<String> part_list = new ArrayList<>();

        try {
            JSONArray parts = jsonObject.getJSONArray("participants");
            for (int i = 0; i < parts.length(); i++) {
                String type = parts.getJSONObject(i).getString("type");
                String name = parts.getJSONObject(i).getString("name");
                part_list.add(type);
                part_list.add(name);
            }
        } catch (JSONException e) {
            part_list.add("");
            e.printStackTrace();
        }

        Log.d("FROM_DB_PARTS", String.valueOf(db.getParticipants(id)));
        Log.d("FROM_DB_PARTS_SIZE", String.valueOf(db.getParticipants(id).size()));
        Log.d("FROM_INT_PARTS", String.valueOf(part_list));

        if (db.getParticipants(id).equals(part_list)) {
           return new ArrayList<String>(Arrays.asList("true"));
        }
        return part_list;
    }

    public static ArrayList<String> equalDocuments(DataBase db, String id, JSONObject jsonObject){
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
            return new ArrayList<>(Arrays.asList("true"));
        }
        return documents;

    }

    public static ArrayList<String> equalHistoryPlace(DataBase db, String id, JSONObject jsonObject){
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
            return new ArrayList<>(Arrays.asList("true"));
        }
        return history_place;
    }

    public static ArrayList<String> equalSessions(DataBase db, String id, JSONObject jsonObject){
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
            return new ArrayList<>(Arrays.asList("true"));
        } else {
            return sessions;
        }
    }

    public static ArrayList<String> equalHistory(DataBase db, String id, JSONObject jsonObject){
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
           return new ArrayList<>(Arrays.asList("true"));
        }
        return history;
    }

    public static ArrayList<String> equalMainInfo(DataBase db, String id, JSONObject jsonObject) {
        ArrayList<String> from_db_info = db.getOnlyInfo(id);
        ArrayList<String> from_internet = new ArrayList<>();
        String[] args = new String[]{"id", "number", "number_input_document",
                "register_date", "date_hearing_first_instance",
                "date_of_appellate_instance", "result_hearing", "number_in_next_instance", "number_in_last_instance", "judge", "category",
                "status", "article", "resons_solve", "date_of_decision"};

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
            return new ArrayList<>(Arrays.asList("true"));
        }
        return from_internet;
    }


    @NonNull
    @Override
    public Result doWork() {
        DataBase db = new DataBase(getApplicationContext());

        for(String id: db.get_all_id()){
            checkUpdate(db, id, getApplicationContext());
        }
        return Result.success();
    }
}
