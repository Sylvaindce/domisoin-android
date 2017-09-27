package com.sylvain.domisoin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sylvain.domisoin.R;

public class Info2SigninFragment extends Fragment {

    public Info2SigninFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info2_signin, container, false);
        EditText address = (EditText)view.findViewById(R.id.address);
        address.requestFocus();

        return view;
    }

}
