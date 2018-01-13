package com.sylvain.domisoin.Fragments.Connexion;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sylvain.domisoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscriptionChoice extends Fragment implements View.OnClickListener {


    public InscriptionChoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_colorp_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View InscriptionChoice = inflater.inflate(R.layout.fragment_inscription_choice, container, false);

        Button choice_button_pro = (Button)InscriptionChoice.findViewById(R.id.choice_button_pro);
        choice_button_pro.setOnClickListener(this);

        Button choice_button_patient = (Button)InscriptionChoice.findViewById(R.id.choice_button_patient);
        choice_button_patient.setOnClickListener(this);


        return InscriptionChoice ;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.choice_button_pro:
                Log.v("OnClick", "Signin button pro");
                ft.replace(R.id.fragment_container, new ParentFragmentSignInPro(), "SigninFragmentPro()");
                ft.addToBackStack("signin");
                ft.commit();
                break;
            case R.id.choice_button_patient:
                Log.v("OnClick", "Signin button patient");
                ft.replace(R.id.fragment_container, new ParentFragmentSignIn(), "SigninFragmentPro()");
                ft.addToBackStack("signin");
                ft.commit();
                break;
                           }
    }

}
