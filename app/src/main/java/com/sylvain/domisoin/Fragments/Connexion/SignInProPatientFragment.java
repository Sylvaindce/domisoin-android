package com.sylvain.domisoin.Fragments.Connexion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

public class SignInProPatientFragment extends Fragment implements View.OnClickListener {

    private Button pro = null;
    private Button patient = null;

    public SignInProPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_pro_patient, container, false);

        // Inflate the layout for this fragment
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_colorp_24dp);
        //upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TextView appbartitle= (TextView) getActivity().findViewById(R.id.appbartitle);
        //appbartitle.setText("Signin");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        pro = (Button)view.findViewById(R.id.signin_pro);
        patient = (Button)view.findViewById(R.id.signin_cust);

        pro.setOnClickListener(this);
        patient.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Boolean pro_part = false;
        switch (view.getId()) {
            case R.id.signin_pro:
                pro_part = false;
                break;
            case R.id.signin_cust:
                pro_part = true;
                break;
        }
        ParentFragmentSignIn signin = new ParentFragmentSignIn(pro_part);
        Log.d("Patient ou Pro", String.valueOf(pro_part));
        //signin.setPro_Patient(pro_part);
        ft.replace(R.id.fragment_container, new ParentFragmentSignIn(), "SigninFragment()");
        ft.addToBackStack("signin");
        ft.commit();
    }
}
