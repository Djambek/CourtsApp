package com.example.courts;


import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBase {
    /** Поле контекста */
    Context context;
    /** Поле название базы данных */
    final String DB_NAME = "all_info.db";
    /** Поле базыданных */
    final SQLiteDatabase db;
    private static DataBase dbManager;

    public static DataBase getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DataBase(context);
        }
        return dbManager;
    }
    public DataBase(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        createTable();
    }

    void addNewCase(String id, String number, String number_input_document,
                    String register_date, String date_hearing_first_instance, String date_of_appellate_instance,
                    String result_hearing, String number_in_next_instance,  String number_in_last_instance,
                    String judge, String category, String status, String article,
                    String resons_solve, String date_of_decision, ArrayList<ArrayList<String>> participants) {
        db.execSQL("INSERT INTO 'case' VALUES ('"+id+"', '"+number+"', '"+number_input_document+"', '"+register_date+"', '"+date_hearing_first_instance+"', '"+date_of_appellate_instance+"', '"+result_hearing+"', '"+number_in_next_instance+"', '"+number_in_last_instance+"', '"+judge+"', '"+category+"', '"+status+"', '"+article+"', '"+resons_solve+"', '"+date_of_decision+"');");
        for(int i=0; i< participants.size(); i++) {
            Log.d("D__", "внутри цикла участников");
            db.execSQL("INSERT INTO 'participants'  VALUES ('"+ id +"', '"+ participants.get(i).get(0)+"', '"+participants.get(i).get(1)+"'); ");
        }
        Log.d("D__", "Ну вроде как добавили");

    }

    void addHistory(String id, ArrayList<String> args){
        for (int i = 0; i < args.size() / 3; i++) {
            //Log.d("info", String.valueOf(new ArrayList<String>(Arrays.asList(id, args.get(i*3), args.get(i*3+1), args.get(i*3+2)))));
            for (int j = 0; j < 3; j++) {
                if (args.get(i*3+j).equals("")){
                    args.set(i*3+j, null);
                }
            }
            String query = String.format("INSERT INTO 'history' VALUES ('%s', '%s', '%s', '%s')", id, args.get(i*3), args.get(i*3+1),
                    args.get(i*3+2));
            db.execSQL(query);
        }

    }
    void addPlaceHistory(String id, ArrayList<String> args){
        for (int i = 0; i < args.size() / 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (args.get(i*3+j).equals("")){
                    args.set(i*3+j, null);
                }
            }
            String query = String.format("INSERT INTO 'places_history' VALUES ('%s', '%s', '%s', '%s')", id, args.get(i*3), args.get(i*3+1),
                    args.get(i*3+2));
            db.execSQL(query);
        }
    }
    void addSessions(String id, ArrayList<String> args){
        for (int i = 0; i < args.size() / 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (args.get(i*3+j).equals("")){
                    args.set(i*3+j, null);
                }
            }
            String query = String.format("INSERT INTO 'sessions' VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", id, args.get(i*3), args.get(i*3+1),
                    args.get(i*3+2), args.get(i*3+3), args.get(i*3+4), args.get(i*3+5));
            db.execSQL(query);
        }
    }
    void addDocuments(String id, ArrayList<String> args){
        for (int i = 0; i < args.size() / 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (args.get(i*3+j).equals("")){
                    args.set(i*3+j, null);
                }
            }
            String query = String.format("INSERT INTO 'documents' VALUES ('%s', '%s', '%s', '%s')", id, args.get(i*3), args.get(i*3+1),
                    args.get(i*3+2));
            db.execSQL(query);
        }
    }


    public boolean exist(String id){
        Cursor cursor = db.rawQuery("SELECT id FROM 'case' WHERE id='"+id+"';", null);
        return cursor.moveToFirst();

    }
    public ArrayList<String> get_all_id(){
        Cursor cursor = db.rawQuery("SELECT id FROM 'case'", null);
        boolean hasMoreData = cursor.moveToFirst();
        ArrayList<String> ids = new ArrayList<>();
        while (hasMoreData){
            ids.add(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            hasMoreData = cursor.moveToNext();

        }
        return ids;
    }

    public ArrayList<Object> getShortInfo(String id) {
        ArrayList<Object> info = new ArrayList<>();
        String  query = "SELECT number, status FROM 'case' WHERE id='"+id+"'"+" LIMIT 1;";
        Log.d("QUERY", query);
        Cursor cursor = db.rawQuery(query, null);
        Log.d("D__number_number_curson", String.valueOf(cursor.getColumnIndex("number")));
        cursor.moveToFirst();
        Log.d("D__", cursor.getString(cursor.getColumnIndexOrThrow("number")));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));

        cursor = db.rawQuery("SELECT type, name FROM 'participants' WHERE id='" + id + "';", null);
        boolean hasMoreData = cursor.moveToFirst();
        info.add(number);
        boolean has_parts = false;
        while (hasMoreData) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"))+": ";
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            info.add(new ArrayList<String>(Arrays.asList(type, name)));
            hasMoreData = cursor.moveToNext();
            has_parts = true;
        }
        if (!has_parts){
            info.add(new ArrayList<String>(Arrays.asList("", "")));
            info.add(new ArrayList<String>(Arrays.asList("", "")));
        }
        if (info.size() < 3){info.add(new ArrayList<String>(Arrays.asList("", "")));}
        Log.d("info", String.valueOf(info));
        return info;
    }
    public void close(){
        db.close();
    }

    public void dropTable(){
        db.execSQL("DROP TABLE 'case'; ");
    }

    public void createTable() {
        Log.d("D__", "CREATED");
        db.execSQL("CREATE TABLE if not exists 'case' ('id' text, 'number' text, 'number_input_document' text, 'register_date' text, 'date_hearing_first_instance' text, 'date_of_appellate_instance' text, 'result_hearing' text, 'number_in_next_instance' text, 'number_in_last_instance' text,  'judge' text, 'category' text, 'status' text, 'article' text, 'resons_solve' text, 'date_of_decision' text);");
        Log.d("D__", "SDFSDF");

        db.execSQL("CREATE TABLE if not exists 'participants' ('id' text, 'type' text, 'name' text);");
        db.execSQL("CREATE TABLE if not exists 'places_history' ('id' text, 'date' text, 'place' text, 'comment' text);");
        db.execSQL("CREATE TABLE if not exists 'sessions' ('id' text, 'date' text, 'hall' text, 'stage' text, 'result' text, 'reson' text, 'video' text);");
        db.execSQL("CREATE TABLE if not exists 'history' ('id' text, 'date' text, 'status' text, 'document' text);");
        db.execSQL("CREATE TABLE if not exists 'documents' ('id' text, 'date' text, 'kind_document' text, 'document_text' text);");
    }

}
