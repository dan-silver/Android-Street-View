package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dan-silver on 12/14/14.
 */
public class DummyFragment extends Fragment {
    public DummyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dummy_frag, container, false);
    }
}