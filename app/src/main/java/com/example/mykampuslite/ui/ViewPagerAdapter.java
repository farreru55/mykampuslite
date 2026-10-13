package com.example.mykampuslite.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// Atur isi 2 tab ViewPager
public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MahasiswaFragment();
            case 1:
                return new DosenFragment();
            default:
                return new MahasiswaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
