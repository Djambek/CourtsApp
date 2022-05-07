package com.example.courts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // тут происходить создание таблицы
        DataBase db = new DataBase(this);
        db.close();

        Paper.init(this);
        Log.d("PAPER", Paper.book().read("city")+"a");
        if (Paper.book().read("city") == null){
            Intent intent = new Intent(MainActivity.this, FirstRun.class);
            startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            nav_view.setCheckedItem(R.id.nav_home);
        }

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;
                    case R.id.nav_settings:
                        Log.d("EE", "Выбрал настройки");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                        break;
                    case R.id.nav_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}