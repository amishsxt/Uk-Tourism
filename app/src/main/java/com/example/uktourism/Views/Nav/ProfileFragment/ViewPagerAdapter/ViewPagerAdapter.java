package com.example.uktourism.Views.Nav.ProfileFragment.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.uktourism.Views.Nav.ProfileFragment.Tab.FavTabFragment;
import com.example.uktourism.Views.Nav.ProfileFragment.Tab.PostsTabFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2;


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new PostsTabFragment();
        }
        else {
            return new FavTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
