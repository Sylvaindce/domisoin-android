package com.sylvain.domisoin.Fragments.Connexion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import android.content.BroadcastReceiver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sylvain.domisoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ForgetFragment.class.getSimpleName();
    private View ForgetFragment = null;
    private EditText loginEdit1 = null;
    private Button forgot_login_button = null;
    private ProgressDialog progress;

    private Map<String, String> datas = null;

    private String login = null;

    private static String FORGET_URL = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_LOGIN";


    public ForgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ForgetFragment = inflater.inflate(R.layout.fragment_forget, container, false);

        loginEdit1 = (EditText) ForgetFragment.findViewById(R.id.loginEdit1);

        FORGET_URL = getString(R.string.api_url)+"reset-password/";
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_colorp_24dp);
       ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        loginEdit1 = (EditText) ForgetFragment.findViewById(R.id.loginEdit1);
        loginEdit1.requestFocus();

        forgot_login_button = (Button) ForgetFragment.findViewById(R.id.forgot_login_button);
        forgot_login_button.setOnClickListener(this);


        datas = new LinkedHashMap<String, String>();


        return ForgetFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_login_button:
                login = String.valueOf(loginEdit1.getText());

                if (login != null && !login.equals("")) {
                    Log.v("EmailValidate", login);
                    datas.clear();
                    datas.put("Forget_Email", String.valueOf(login));
                    HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, FORGET_URL, datas);
                    task.execute();
                    progress = ProgressDialog.show(getActivity(), "Verification", "Vérification du mail en cour..", true);

                } else {
                    Snackbar.make(ForgetFragment.findViewById(R.id.loginfragment_container), "Veuillez renseigner une adresse email et un mot de passe valide.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                break;
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);

            if (response != null) {
                if (response.length() == 3) {
                    Snackbar.make(ForgetFragment.findViewById(R.id.loginfragment_container), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        }

        ;
    };
}