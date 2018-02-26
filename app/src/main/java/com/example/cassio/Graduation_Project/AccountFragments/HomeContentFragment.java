package com.example.cassio.Graduation_Project.AccountFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cassio.Graduation_Project.AppealFragments.AppealsList;
import com.example.cassio.Graduation_Project.InsightsActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SponsorListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContentFragment extends Fragment {
    View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_content, container, false);
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.pager_id);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        return mView;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;
                case 1:
                    AvailableEventFragment availableEventActivity = new AvailableEventFragment();
                    return availableEventActivity;
                case 2:
                    SponsorListActivity logisticsActivity = new SponsorListActivity();
                    return logisticsActivity;
                case 3:
                    AppealsList appealsList = new AppealsList();
                    return appealsList;
                case 4:
                    AllAppUsersFragment allAppUsersActivity = new AllAppUsersFragment();
                    return allAppUsersActivity;
                case 5:
                    InsightsActivity insightactivity = new InsightsActivity();
                    return insightactivity;
                default:
                    return null;

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "home";
                case 1:
                    return "Available Events";
                case 2:
                    return "Sponsors";
                case 3:
                    return "Appeals";
                case 4:
                    return "users";
                case 5:
                    return "insights";
                default:
                    return null;
            }
        }

    }

}


