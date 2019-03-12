package com.example.sarav.anew;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFrag extends Fragment {
    TextView help;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.help, container, false);
        help = v.findViewById(R.id.help);
        help.setText("Mail to saravanasan06@gmail.com for any queries related to this application");
        return v;
    }
}
