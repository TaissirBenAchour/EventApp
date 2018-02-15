package com.example.cassio.Graduation_Project._profileFragments;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContentFragment extends Fragment {
    View mView;


    private ViewPager mViewPager;
    private TabLayout mytabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_home_content, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager_id);

        FragmentManager fragmentManager = getChildFragmentManager();
        MyAdapter adapter = new MyAdapter(fragmentManager);
        mViewPager.setAdapter(adapter);

//        mytabLayout = (TabLayout) mView.findViewById(R.id.main_tab_id);
//        mytabLayout.setupWithViewPager(mViewPager);


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
            switch (position) {
                case 0:
                    HomeContentFragment freindsListFragment = new HomeContentFragment();
                    return freindsListFragment;

                case 1:
                    FreindsRequestFragment freindsRequestFragment =new FreindsRequestFragment();
                    return freindsRequestFragment;
                case 2:
                    FreindsRequestFragment freindsRequestFragment1 =new FreindsRequestFragment();
                    return freindsRequestFragment1;
                case 3:
                    FreindsRequestFragment freindsRequestFragment2 =new FreindsRequestFragment();
                    return freindsRequestFragment2;
                case 4:
                    FreindsRequestFragment freindsRequestFragment3 =new FreindsRequestFragment();
                    return freindsRequestFragment3;

                default: return null;}

        }

//        public CharSequence getPageTitle(int position) {
//            switch (position){
//                case 0 : return "1" ;
//                case 1 : return "2" ;
//                case 2 :return "2"  ;
//                case 3: return "2" ;
//                case  4: return "2";
//                default: return null;
//            }
//        }
    }

}
