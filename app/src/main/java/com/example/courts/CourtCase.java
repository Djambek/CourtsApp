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
                if (!jsonObject.getString(args[i]).equals("")){ for_listview.add(names[i]+": "+jsonObject.getString(args[i])); }
                else{ for_listview.add(""); }
                for_db.add(jsonObject.getString(args[i]));
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
                for_listview.add(3, type+name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            for_listview.add(3, "");
            part_list.add(new ArrayList<>(Arrays.asList("", "")));
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
        view_adapter.addFragment(extraCaseInfoFragment, "Дополнительное");

        viewPager.setAdapter(view_adapter);


//        ArrayList<ArrayList<Object>> case_info = new ArrayList<>();
//
//        for (int i = 0; i < args.length; i++) {
//            try {
//                Log.d("ARGS", args[i]);
//                ArrayList<Object> tmp = new ArrayList<>();
//                if (args[i].equals("participants")) {
//
//                    JSONArray parts = jsonObject.getJSONArray("participants");
//                    Log.d("RESULT", String.valueOf(parts));
//                    ArrayList<String> my_tmp = new ArrayList<>(); // надо, чтобы были в одном списке ответчик и истец
//                    for (int j = 0; j < parts.length(); j++) {
//                        Log.d("RESULT", "Зашли в цикл участников");
//                        tmp.add(parts.getJSONObject(j).getString("type"));
//                        part_db_info.add(parts.getJSONObject(j).getString("type"));
//                        tmp.add(parts.getJSONObject(j).getString("name"));
//                        part_db_info.add(parts.getJSONObject(j).getString("name"));
//                        tmp.add(null);
//                        case_info.add(tmp);
//                        tmp = new ArrayList<>();
//                    }
//                    if (parts.length() == 0){
//                        part_db_info.add(""); // чисто само потом поменяется
//                    }
//
//                } else {
//                    if (args[i].equals("number_in_last_instance")) {
//                        tmp.add(names[i] + ": ");
//                        if (!jsonObject.getString(args[i]).equals("")){
//                            add_case_db_info.add(jsonObject.getString(args[i]));
//                        }else{
//                            add_case_db_info.add("null");
//                        }
//                        tmp.add(jsonObject.getString(args[i]));
//
//                        if (!jsonObject.getString("url_number_in_last_instance").equals("")) {
//                            tmp.add(jsonObject.getString("url_number_in_last_instance"));
//                            add_case_db_info.add(jsonObject.getString("url_number_in_last_instance"));
//                        }else{
//                            add_case_db_info.add("null");
//                        }
//                        tmp.add(null); //Цвет
//                    } else {
//                        if (args[i].equals("number_in_next_instance")) {
//                            tmp.add(names[i] + ": ");
//                            if (!jsonObject.getString(args[i]).equals("")) {
//                                add_case_db_info.add(jsonObject.getString(args[i]));
//                            }
//                            else{
//                                add_case_db_info.add("null");
//                            }
//                            tmp.add(jsonObject.getString(args[i]));
//                            if (!jsonObject.getString("url_number_in_next_instance").equals("")) {
//                                add_case_db_info.add(jsonObject.getString("url_number_in_next_instance"));
//                                tmp.add(jsonObject.getString("url_number_in_next_instance"));
//                            }else{
//                                add_case_db_info.add("null");
//                            }
//                            tmp.add(null); //Цвет
//                        } else {
//                            tmp.add(names[i] + ": ");
//                            tmp.add(jsonObject.getString(args[i]));
//                            if (jsonObject.getString(args[i]).equals("")){
//                                add_case_db_info.add("null");
//                            }else {
//                                add_case_db_info.add(jsonObject.getString(args[i]));
//                            }
//                            tmp.add(null); // Цвет, тут он должен быть прозрачным
//
//                        }
//                    }
//                }
//                if(tmp.size() > 2 && !tmp.get(0).equals("") && !tmp.get(1).equals("")) {
//                    case_info.add(tmp);
//                }
//                tmp = new ArrayList<>();
//
//            } catch (JSONException e) {
//                Log.e("JSON ERROR", e.getMessage());
//            }
//        }
//        Log.d("RESULT", String.valueOf(add_case_db_info));
//        Log.d("RESULT", String.valueOf(add_case_db_info.size()));
//        Log.d("PARTS", String.valueOf(part_db_info));



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