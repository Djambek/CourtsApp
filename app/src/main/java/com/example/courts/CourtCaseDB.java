package com.example.courts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class CourtCaseDB extends AppCompatActivity {


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_courtcasedb_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_case_db);


        Intent intent = getIntent();
        ArrayList<String> info = intent.getStringArrayListExtra("main_info");
        CourtCaseDBFragment fragment = new CourtCaseDBFragment(info,
                intent.getStringArrayListExtra("history"),
                intent.getStringArrayListExtra("history_place"),
                intent.getStringArrayListExtra("sessions"),
                intent.getStringArrayListExtra("documents"));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Делаем сверху строчку с названием приложения
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    DataBase db = new DataBase(getApplicationContext());
                    Log.d("INFO", info.get(1));
                    db.delete(info.get(1));
                    finish();
                }
                return true;
            }
        });
    }
}