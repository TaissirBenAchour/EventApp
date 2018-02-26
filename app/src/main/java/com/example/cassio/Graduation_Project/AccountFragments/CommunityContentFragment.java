package com.example.cassio.Graduation_Project.AccountFragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cassio.Graduation_Project.R;


public class CommunityContentFragment extends Fragment {
    View mView;


    private ViewPager mViewPager;
    private TabLayout mytabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.community_layout, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getChildFragmentManager();
        MyAdapter adapter = new MyAdapter(fragmentManager);
        mViewPager.setAdapter(adapter);

        mytabLayout = (TabLayout) mView.findViewById(R.id.main_tab_id);
        mytabLayout.setupWithViewPager(mViewPager);

        mytabLayout.getTabAt(0).setText("Freinds list");
        mytabLayout.getTabAt(1).setText("Freinds Request");

        return mView;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
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
                    FreindsListFragment freindsListFragment = new FreindsListFragment();
                    return freindsListFragment;

                case 1:
                    FreindsRequestFragment freindsRequestFragment = new FreindsRequestFragment();
                    return freindsRequestFragment;
                default:
                    return null;
            }

        }


    }
}
