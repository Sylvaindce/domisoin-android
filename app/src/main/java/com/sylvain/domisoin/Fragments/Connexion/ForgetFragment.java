package com.sylvain.domisoin.Fragments.Connexion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sylvain.domisoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetFragment extends Fragment {

    private View ForgetFragment = null;
    private EditText loginEdit1 = null;
    public ForgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ForgetFragment = inflater.inflate(R.layout.fragment_forget, container, false);
        loginEdit1 = (EditText)ForgetFragment.findViewById(R.id.loginEdit1);

        return ForgetFragment;
    }



}
