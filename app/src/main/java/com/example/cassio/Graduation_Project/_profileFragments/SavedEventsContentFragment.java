package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cassio.Graduation_Project.R;

/**
 * Provides UI for the view with List.
 */
public class  SavedEventsContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_layout, null);
    }
}