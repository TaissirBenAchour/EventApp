package com.example.cassio.Graduation_Project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.cassio.Graduation_Project._profileFragments.MainProfileContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.MessagesListContentFragment;

/**
 * Created by cassio on 14/02/18.
 */

public class tabfreindsAdapter extends FragmentPagerAdapter {


        public tabfreindsAdapter(FragmentManager fm) {
            super(fm);



        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MainProfileContentFragment mainProfileContentFragment = new MainProfileContentFragment();
                    return mainProfileContentFragment;

                case 1:
                    MessagesListContentFragment messagesListContentFragment =new MessagesListContentFragment();
                    return messagesListContentFragment;
                default: return null;}

        }

    }

