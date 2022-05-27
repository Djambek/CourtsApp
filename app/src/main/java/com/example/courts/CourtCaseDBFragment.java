package com.example.courts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CourtCaseDBFragment extends Fragment {

    private final ArrayList<String> history;
    private final ArrayList<String> history_place;
    private final ArrayList<String> sessions;
    private final ArrayList<String> documents;
    private final ArrayList<String> main_info;
    TabLayout tabLayout;
    ViewPager viewPager;

    CourtCaseDBFragment(ArrayList<String> main_info, ArrayList<String> history, ArrayList<String> history_place, ArrayList<String> sessions, ArrayList<String> documents){
        this.history  = history;
        this.history_place = history_place;
        this.sessions = sessions;
        this.documents = documents;
        this.main_info = main_info;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_court_case_d_b, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewpager);



        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter adapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Bundle args_main = new Bundle();
        MainCaseInfoFragment fragment = new MainCaseInfoFragment();
        args_main.putStringArrayList("info", main_info);
        fragment.setArguments(args_main);
        adapter.addFragment(fragment, "Главное");

        Bundle args_extra = new Bundle();
        ExtraCaseInfoFragment fragment_extra = new ExtraCaseInfoFragment();
        args_extra.putStringArrayList("history", history);
        args_extra.putStringArrayList("history_place", history_place);
        args_extra.putStringArrayList("documents", documents);
        args_extra.putStringArrayList("sessions", sessions);
        fragment_extra.setArguments(args_extra);
        adapter.addFragment(fragment_extra, "Дополнительное");
        viewPager.setAdapter(adapter);

        Log.d("STARTED", "Стартанули получение из дб");
        return view;
    }
}