package com.example.cassio.Graduation_Project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cassio.Graduation_Project._AppealFragments.AppealCommittee;
import com.example.cassio.Graduation_Project._AppealFragments.AppealEvent;

/**
 * Created by cassio on 27/01/18.
 */

public class AppealPagerAdapter extends FragmentStatePagerAdapter {


    public AppealPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                AppealEvent appealEvent =new AppealEvent();
                return appealEvent;


            case 1:
                AppealCommittee appealCommittee = new AppealCommittee();
                return appealCommittee;

            default: return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }}