package com.example.sarav.anew;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFrag extends Fragment {
    TextView about;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about, container, false);
        about = v.findViewById(R.id.about);
        about.setText("This application helps to manage the bunked classes in a user-friendly manner");
        return v;
    }
}
