package com.sylvain.domisoin.Fragments.Connexion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sylvain.domisoin.R;

public class Info1SigninFragment extends Fragment {

    public Info1SigninFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info1_signin, container, false);
        EditText email = (EditText)view.findViewById(R.id.email);
        email.requestFocus();

        return view;
    }

}
