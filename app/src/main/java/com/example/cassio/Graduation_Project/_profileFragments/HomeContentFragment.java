package com.example.cassio.Graduation_Project._profileFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



        return mView;
    }

    }


