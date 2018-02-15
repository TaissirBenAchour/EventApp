package com.example.cassio.Graduation_Project;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cassio.Graduation_Project._profileFragments.CommunityContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.HomeContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.MainProfileContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.MessagesListContentFragment;

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
                CommunityContentFragment communityContentFragment = new CommunityContentFragment();
                return communityContentFragment;


            case 3:
                MessagesListContentFragment messagesListContentFragment =new MessagesListContentFragment();
                return messagesListContentFragment;



            default: return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
