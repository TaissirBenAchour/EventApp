package com.example.cassio.Graduation_Project._profileFragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cassio.Graduation_Project.AllAppUsersActivity;
import com.example.cassio.Graduation_Project.AppealsList;
import com.example.cassio.Graduation_Project.AvailableEventActivity;
import com.example.cassio.Graduation_Project.HomeFragment;
import com.example.cassio.Graduation_Project.LogisticsActivity;
import com.example.cassio.Graduation_Project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContentFragment extends Fragment {
    View mView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_home_content, container, false);
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.pager_id);
        /** Important: Must use the child FragmentManager or you will see side effects. */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        return mView;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    HomeFragment mainProfileContentFragment = new HomeFragment();
                    return mainProfileContentFragment;
                case 1 :
                    AvailableEventActivity mainProfileContentFragment1 = new AvailableEventActivity();
                    return mainProfileContentFragment1;
                case 2 :
                    LogisticsActivity mainProfileContentFragment2 = new LogisticsActivity();
                    return mainProfileContentFragment2;
                case 3 :
                    AppealsList mainProfileContentFragment3 = new AppealsList();
                    return mainProfileContentFragment3;
                case 4 :
                    AllAppUsersActivity mainProfileContentFragment4 = new AllAppUsersActivity();
                    return mainProfileContentFragment4;
                    default:return null;

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:return  "home";
                case 1: return "Available Events";
                case 2: return "home";
                case 3: return "Appeals";
                case 4 : return "users";
                default:return null;
            }
        }

    }

    }


