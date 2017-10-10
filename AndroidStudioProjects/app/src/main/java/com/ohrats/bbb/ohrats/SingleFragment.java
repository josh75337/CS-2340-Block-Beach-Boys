package com.ohrats.bbb.ohrats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment that handles uploading csvs
 * Created by Matt on 10/6/2017.
 */

public class SingleFragment extends Fragment {
    private static final String TAG = "SingleFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single, container, false);

        return view;
    }
}
