package com.example.courts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;



    @RequiresApi(api = Build.VERSION_CODES.M)
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").commit();
            nav_view.setCheckedItem(R.id.nav_home);
        }

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").commit();
                        break;
                    case R.id.nav_settings:
                        Log.d("EE", "Выбрал настройки");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment(), "settings").commit();
                        break;
                    case R.id.nav_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment(), "about").commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


//        Intent intent = new Intent();
//        String packageName = this.getPackageName();
//        PowerManager pm = (PowerManager)
//                this.getSystemService(Context.POWER_SERVICE);
//        if (pm.isIgnoringBatteryOptimizations(packageName))
//            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        else {
//            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//            intent.setData(Uri.parse("package:" + packageName));
//        }
//        this.startActivity(intent);

//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent_ =  new Intent(MainActivity.this, CheckCase.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent_, 0);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR, 18);
//        calendar.set(Calendar.MINUTE, 11);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

//        Intent intent = new Intent(MainActivity.this, CheckCase.class);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR, 19);
//        calendar.set(Calendar.MINUTE, 5);
//        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        ((AlarmManager) getSystemService(ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

//        Calendar updateTime = Calendar.getInstance();
//        updateTime.set(Calendar.SECOND, 5);
//        Intent alarmIntent = new Intent(this, CheckCase.class);
//        PendingIntent recurringDownload = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarms.cancel(recurringDownload);
//        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 1000 * 5, recurringDownload); //will run it after every 5 seconds.


//        Intent intent_alarm = new Intent(this, CheckCase.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent_alarm, 0);
//
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
//                100, alarmIntent);
//        Intent intent = new Intent(this, CheckCase.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 2, intent, 0);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        long l = new Date().getTime();
//        if (l < new Date().getTime()) {
//            l += 1000*5;
//        }
//        Log.d("STARTED", "NOW ALARM WORK");
//        am.setRepeating(AlarmManager.RTC_WAKEUP, l, 5000, sender); // 86400000
//        Timer timer = new Timer();
//        Date executionDate = new Date();
//        long period = 10 * 1000;
//        timer.scheduleAtFixedRate(new CheckCase(), executionDate, period);
//        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
//        ses.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("SHEDULE", "STARTED");
//                Toast.makeText(MainActivity.this, "STARTED", Toast.LENGTH_SHORT).show();
//            }
        
//        }, 0, 1, TimeUnit.SECONDS);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 18);
        calendar.set(Calendar.MINUTE, 44);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        PeriodicWorkRequest per_work = new PeriodicWorkRequest.Builder(CheckCase.class, 15,
                        TimeUnit.MINUTES)
                .addTag("TEST")
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "TEST",
                ExistingPeriodicWorkPolicy.KEEP,
                per_work
        );
//        Long timeDiff = calendar.getTimeInMillis() - System.currentTimeMillis();
//        WorkRequest uploadWorkRequest =
//                new OneTimeWorkRequest.Builder(TestWorker.class)
//                        .setInitialDelay(timeDiff, TimeUnit.MICROSECONDS)
//                        .build();
//        WorkManager
//                .getInstance(this)
//                .enqueue(uploadWorkRequest);


        //create runnable for delay
//        mToastRunnable = new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "This is a delayed toast", Toast.LENGTH_SHORT).show();
//                Log.d("RUN", "ran");
//                mHandler.postDelayed(this, 2000);
//            }
//        };
//        mToastRunnable.run();


//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 38);



        }

//    @Override
//    protected void onRestart() {
//        Log.d("RESTART", "рестартнули маин");
//        // обновили фрагмент, где отображаются дела после рестарта активности
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        Fragment fr = getFragmentManager().findFragmentByTag("home");
//        ft.detach(fr);
//        ft.attach(fr);
//        ft.commit();
//        super.onRestart();
//    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}