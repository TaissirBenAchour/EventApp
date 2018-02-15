package com.example.cassio.Graduation_Project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cassio.Graduation_Project.profileFragment.infosFragment;
import com.example.cassio.Graduation_Project.profileFragment.profileFragment;


/**
 * Created by cassio on 11/02/18.
 */

class profiletabAdapter extends FragmentStatePagerAdapter {


    public profiletabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                profileFragment profileFragment =new profileFragment();
                return profileFragment;


            case 1:
                infosFragment infoFragment = new infosFragment();
                return infoFragment;

            default: return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}