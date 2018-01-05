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
public class NameSignInProFragment extends Fragment {

    private View view = null;

    public NameSignInProFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_name_sign_in_pro, container, false);

        EditText lastname = (EditText)view.findViewById(R.id.lastname);
        lastname.requestFocus();

        return view;
    }

}
