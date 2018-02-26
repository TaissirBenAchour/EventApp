package com.example.cassio.Graduation_Project;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cassio.Graduation_Project.AccountFragments.CommunityContentFragment;
import com.example.cassio.Graduation_Project.AccountFragments.HomeContentFragment;
import com.example.cassio.Graduation_Project.AccountFragments.MainProfileContentFragment;
import com.example.cassio.Graduation_Project.AccountFragments.MessagesListContentFragment;

/**
 * Created by cassio on 28/12/17.
 */

class TabPagerAdapter extends FragmentStatePagerAdapter {


    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

            HomeContentFragment homeContentFragment = new HomeContentFragment();
            return homeContentFragment;

            case 1:
                MainProfileContentFragment mainProfileContentFragment = new MainProfileContentFragment();
                return mainProfileContentFragment;



            case 2:

            MessagesListContentFragment messagesListContentFragment =new MessagesListContentFragment();
            return messagesListContentFragment;

            case 3:
                CommunityContentFragment communityContentFragment = new CommunityContentFragment();
                return communityContentFragment;



            default: return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
