package com.sylvain.domisoin.Fragments.Connexion;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sylvain.domisoin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConnexionFragment extends Fragment implements View.OnClickListener {

    public ConnexionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View connexionFragment = inflater.inflate(R.layout.fragment_connexion, container, false);



        //ImageView rounded_pic = (ImageView)connexionFragment.findViewById(R.id.rounded_pic);

        //RoundedPic rounder = new RoundedPic(getResources());
        //rounded_pic.setImageDrawable(rounder.createRoundedBitmapDrawableWithBorder(BitmapFactory.decodeResource(getResources(),R.drawable.background_login)));

        Button login_button = (Button)connexionFragment.findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        Button signup_button = (Button)connexionFragment.findViewById(R.id.signup_button);
        signup_button.setOnClickListener(this);

        return connexionFragment;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.login_button:
                ft.replace(R.id.fragment_container, new LoginFragment(), "LoginFragment()");
                ft.addToBackStack("login");
                ft.commit();
                //Login test = new Login(this.getContext());
                //test.execute();
                break;
            case R.id.signup_button:
                Log.v("OnClick", "Signin button");
                ft.replace(R.id.fragment_container, new SignInProPatientFragment(), "SignInProPatientFragment()");
                ft.addToBackStack("signin");
                ft.commit();
                /*ft.replace(R.id.fragment_container, new ParentFragmentSignIn(), "SigninFragment()");
                ft.addToBackStack("signin");
                ft.commit();*/
                break;
        }
    }
}
