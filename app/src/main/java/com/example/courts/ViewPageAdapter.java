package com.example.courts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments_list = new ArrayList<>();
    private final ArrayList<String> fragments_title = new ArrayList<>();

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments_list.get(position);
    }

    @Override
    public int getCount() {
        return fragments_list.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragments_list.add(fragment);
        fragments_title.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments_title.get(position);
    }
}
