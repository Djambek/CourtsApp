package com.example.courts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CourtCase extends AppCompatActivity {
    JSONObject jsonObject;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_case);

        // Делаем сверху строчку с названием приложения
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);




        Bundle arguments = getIntent().getExtras();
        ArrayList<String> add_case_db_info = new ArrayList<>();
        ArrayList<String> part_db_info = new ArrayList<>();

        String link = "http://ctf.djambek.com:8080/details?link=" + arguments.getString("link");
        Log.d("RESULT_SEARCH_LINK", link);
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document res = Jsoup.connect(link).ignoreContentType(true).get();
                    jsonObject = new JSONObject(res.text());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
            Log.d("RESULT_SEARCH", String.valueOf(jsonObject));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        String[] args = new String[]{"id", "number", "number_input_document", "participants",
//                "register_date", "date_hearing_first_instance",
//                "date_of_appellate_instance", "result_hearing", "number_in_next_instance", "number_in_last_instance", "judge", "category",
//                "status", "article", "resons_solve", "date_of_decision"};
//        String[] names = new String[]{"Уникальный индификатор дела", "Номер дела", "Номер входящего документа",
//                "", "Дата регистрации", "Дата рассмотрения дела в первой инстанции",
//                "Дата поступления дела в апелляционную инстанцию", "Результат рассмотрения", "Номер дела в суде вышестоящей инстанции",
//                "Номер дела в суде нижестоящей инстанции",
//                "Судья", "Категория дела", "Статус",
//                "Статья", "Основание решения суда", "Дата вступления решения в силу"};


        String[] args = new String[]{"id", "number", "number_input_document",
                "register_date", "date_hearing_first_instance",
                "date_of_appellate_instance", "result_hearing", "number_in_next_instance", "number_in_last_instance", "judge", "category",
                "status", "article", "resons_solve", "date_of_decision"};
        String[] names = new String[]{"Уникальный индификатор дела", "Номер дела", "Номер входящего документа",
                "Дата регистрации", "Дата рассмотрения дела в первой инстанции",
                "Дата поступления дела в апелляционную инстанцию", "Результат рассмотрения", "Номер дела в суде вышестоящей инстанции",
                "Номер дела в суде нижестоящей инстанции",
                "Судья", "Категория дела", "Статус",
                "Статья", "Основание решения суда", "Дата вступления решения в силу"};

        ArrayList<String> for_listview = new ArrayList<>(); // сюда добавляем все чисто для спиннера, чтобы внутри него не страдать
        ArrayList<String> for_db = new ArrayList<>(); // сюда добавляем данные, чтобы потом добавить в бд
        ArrayList<ArrayList<String>> part_list = new ArrayList<>(); // для участников

        for(int i=0; i<args.length; i++){
            try {
                if (!jsonObject.getString(args[i]).equals("")){
                    for_listview.add(names[i]+": ");
                    for_listview.add(jsonObject.getString(args[i]));
                    for_db.add(jsonObject.getString(args[i]));
                }else{
                    for_listview.add("");
                    for_listview.add("");
                    for_db.add("null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONArray parts = jsonObject.getJSONArray("participants");
            for (int i = 0; i < parts.length(); i++) {
                String type = parts.getJSONObject(i).getString("type");
                String name = parts.getJSONObject(i).getString("name");
                part_list.add(new ArrayList<>(Arrays.asList(type, name)));
                for_listview.add(6, type + ": ");
                for_listview.add(7, name);
            }
        } catch (JSONException e) {
            part_list.add(new ArrayList<>(Arrays.asList("", "")));
            e.printStackTrace();
        }

        // парсим истоторию состояний
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


        Log.d("Court+", "---------");
        Log.d("Court+", String.valueOf(for_db));
        Log.d("Court+", String.valueOf(for_db.size()));
        Log.d("Court+", "---------");
        Log.d("Court+", String.valueOf(for_listview));
        Log.d("Court+", String.valueOf(part_list));

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpage);

        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter view_adapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        Bundle args_main = new Bundle();
        args_main.putStringArrayList("info", for_listview);
        MainCaseInfoFragment mainCaseInfoFragment = new MainCaseInfoFragment();
        mainCaseInfoFragment.setArguments(args_main);
        view_adapter.addFragment(mainCaseInfoFragment, "Главное");


        ExtraCaseInfoFragment extraCaseInfoFragment = new ExtraCaseInfoFragment();
        Bundle args_extra = new Bundle();
        Log.d("History", String.valueOf(history));
        args_extra.putStringArrayList("history", history);
        args_extra.putStringArrayList("history_place", history_place);
        args_extra.putStringArrayList("sessions", sessions);
        args_extra.putStringArrayList("documents", documents);
        extraCaseInfoFragment.setArguments(args_extra);
        view_adapter.addFragment(extraCaseInfoFragment, "Дополнительное");


        viewPager.setAdapter(view_adapter);

        Button save = findViewById(R.id.button_add_new_case);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase db = new DataBase(getApplicationContext());
                if (!db.exist(for_db.get(0))){
                    db.addNewCase(for_db.get(0), for_db.get(1), for_db.get(2), for_db.get(3), for_db.get(4), for_db.get(5),
                            for_db.get(6), for_db.get(7), for_db.get(8), for_db.get(9),
                            for_db.get(10), for_db.get(11), for_db.get(12), for_db.get(13), for_db.get(14), part_list);
                    db.close();
                    Intent intent = new Intent(CourtCase.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "Это дело уже добавлено.", Toast.LENGTH_SHORT).show();
                }

            }
        });



//        Button save = findViewById(R.id.buttonsaveinfo);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DataBase dataBase = new DataBase(getApplicationContext());
//                if (!dataBase.exist(add_case_db_info.get(0))){
//                    dataBase.addNewCase(add_case_db_info.get(0), add_case_db_info.get(1), add_case_db_info.get(2), part_db_info,
//                            add_case_db_info.get(3), add_case_db_info.get(4), add_case_db_info.get(5), add_case_db_info.get(6),
//                            add_case_db_info.get(7), add_case_db_info.get(8), add_case_db_info.get(9),add_case_db_info.get(10),
//                            add_case_db_info.get(11), add_case_db_info.get(12),add_case_db_info.get(13), add_case_db_info.get(14),
//                            add_case_db_info.get(15), add_case_db_info.get(16));}
//                else{
//                    Log.d("IDS__", "Добавляем уже существуеющий");
//                }
//                Log.d("IDS___", String.valueOf(dataBase.get_all_id()));
//                dataBase.close();
//                Intent intent = new Intent(CourtCase.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//
//            }
//        });

//        ListView listView = findViewById(R.id.list_view_court_info);
//        Log.d("MASSIVE", String.valueOf(case_info));
//        listView.setAdapter(new Court_info_adapter(this, case_info));

    }
}