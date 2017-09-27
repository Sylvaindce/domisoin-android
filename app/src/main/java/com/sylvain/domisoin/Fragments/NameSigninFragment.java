package com.sylvain.domisoin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sylvain.domisoin.R;

public class NameSigninFragment extends Fragment {

    private View view = null;

    public NameSigninFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_name_signin, container, false);

        EditText lastname = (EditText)view.findViewById(R.id.lastname);
        lastname.requestFocus();

        return view;
    }
}
