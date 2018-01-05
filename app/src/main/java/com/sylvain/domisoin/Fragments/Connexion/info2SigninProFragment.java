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
public class info2SigninProFragment extends Fragment {


    public info2SigninProFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info2_signin2, container, false);
        EditText address = (EditText)view.findViewById(R.id.address);
        address.requestFocus();
        return view;
    }

}
