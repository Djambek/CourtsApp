package com.example.courts;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;


import java.io.IOException;

public class SearchResult extends AppCompatActivity {
    Integer page_now;
    String content;
    Integer max_page = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // Делаем сверху строчку с названием приложения
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);



        Bundle arguments = getIntent().getExtras();
        String link = arguments.getString("link");

        Button last = findViewById(R.id.button_last);
        Button next = findViewById(R.id.button_next);
        last.setVisibility(View.INVISIBLE);



        page_now = 1;

        content = get_content(link, page_now).text();

        try {
            max_page = new JSONObject(content).getInt("pages");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (max_page == 1){next.setVisibility(View.INVISIBLE);}


        Log.d("TEXT", content);

        SearchResultFragment sr_framgent = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("json", content);
        sr_framgent.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, sr_framgent).commit();

        TextView textView = findViewById(R.id.text_pages);
        textView.setText(page_now+" из "+ max_page);


        Integer finalMax_page = max_page;
        last.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
                        SearchResultFragment sr_fragment = new SearchResultFragment();
                        Bundle args = new Bundle();
                        page_now -= 1;
                        content = get_content(link, page_now).text();
                        args.putString("json", content);
                        sr_fragment.setArguments(args);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, sr_fragment).commit();
                        textView.setText(page_now+" из "+ max_page);
                        if (page_now > 1){last.setVisibility(View.VISIBLE);}
                        else{last.setVisibility(View.INVISIBLE);}
                        if (page_now != max_page){next.setVisibility(View.VISIBLE);}
                        else{next.setVisibility(View.INVISIBLE);}

                    }
                }
        );


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
                SearchResultFragment sr_fragment = new SearchResultFragment();
                Bundle args = new Bundle();
                page_now += 1;
                content = get_content(link, page_now).text();
                Log.d("CONTENT_", content);
                args.putString("json", content);
                sr_fragment.setArguments(args);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, sr_fragment).commit();
                textView.setText(page_now+" из "+ max_page);
                if (page_now > 1){last.setVisibility(View.VISIBLE);}
                else{last.setVisibility(View.INVISIBLE);}
                if (page_now != max_page){next.setVisibility(View.VISIBLE);}
                else{next.setVisibility(View.INVISIBLE);}
            }
        });

        mActionBarToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_renew) {
                    Toast.makeText(getApplicationContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
                    SearchResultFragment sr_fragment = new SearchResultFragment();
                    Bundle args = new Bundle();
                    content = get_content(link, page_now).text();
                    args.putString("json", content);
                    sr_fragment.setArguments(args);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, sr_fragment).commit();
                    try {
                        max_page = new JSONObject(content).getInt("pages");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (max_page == 1){next.setVisibility(View.INVISIBLE);}
                    textView.setText(page_now+" из "+ max_page);

                }
                return true;
            }
        });

    }
    public static Document get_content(String url, Integer page) {
        String link = "http://ctf.djambek.com:8080/search_case?link="+url+"&page="+page;
        Document[] res = new Document[1];
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    res[0] = Jsoup.connect(link).maxBodySize(50000000)
                            .ignoreContentType(true).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR", e.toString());
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
        return res[0];
    }



}