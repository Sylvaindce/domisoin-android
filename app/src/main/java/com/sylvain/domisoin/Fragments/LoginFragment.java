package com.sylvain.domisoin.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sylvain.domisoin.Activities.HomeActivity;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private View loginfragment = null;

    private EditText loginEdit = null;
    private EditText passwordEdit = null;
    private Button validate = null;
    private ProgressDialog progress;

    private String login = null;
    private String password = null;

    private Map<String, String> datas = null;


    private static String LOGIN_URL                   = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_LOGIN";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        loginfragment = inflater.inflate(R.layout.fragment_login, container, false);


        LOGIN_URL = getString(R.string.api_url)+"auth/login/";
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_colorp_24dp);
        //upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TextView appbartitle= (TextView) getActivity().findViewById(R.id.appbartitle);
        //appbartitle.setText("Login");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        //InputMethodManager imm = (InputMethodManager) loginfragment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        loginEdit = (EditText)loginfragment.findViewById(R.id.loginEdit);
        loginEdit.requestFocus();

        //loginEdit.clearFocus();

        passwordEdit = (EditText)loginfragment.findViewById(R.id.passwordEdit);
        //passwordEdit.clearFocus();

        validate = (Button)loginfragment.findViewById(R.id.validate_login_button);
        validate.setOnClickListener(this);

        datas = new LinkedHashMap<String, String>();

        //imm.showSoftInput(loginEdit, InputMethodManager.SHOW_IMPLICIT);
        //imm.showSoftInput(passwordEdit, InputMethodManager.SHOW_IMPLICIT);

        return loginfragment;
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
            case R.id.validate_login_button:

                login = String.valueOf(loginEdit.getText());
                password = String.valueOf(passwordEdit.getText());

                if (login != null && !login.equals("") && password != null && !password.equals("")) {
                    Log.v("LoginValidate", login);
                    Log.v("LoginValidate", password);
                    datas.clear();
                    datas.put("email", String.valueOf(login));
                    datas.put("password", String.valueOf(password));

                    /*for(int i = 0; i < datas.size(); ++i) {
                        Log.d("ParentFragm ALL DATA", datas.keySet().toArray()[i] + " " + datas.values().toArray()[i]);
                       }*/

                    HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, LOGIN_URL, datas);
                    task.execute();
                    progress = ProgressDialog.show(getActivity(), "Authentification", "Vérification en cours, merci de patienter...", true);
                }
                else {
                    Snackbar.make(loginfragment.findViewById(R.id.loginfragment_container), "Veuillez renseigner une adresse email et un mot de passe valide.", Snackbar.LENGTH_LONG)
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
                    Snackbar.make(loginfragment.findViewById(R.id.loginfragment_container), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        Log.d(TAG, jsonObj.getString("is_pro"));

                        SharedPreferences sharedPref = getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.save_account), datas.get("email"));
                        editor.putString(getString(R.string.save_password), datas.get("password"));
                        editor.apply();

                        if (jsonObj.getBoolean("is_pro")) {
                            //is pro
                            Intent homeintent = new Intent(getActivity(), HomeActivity.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle userinfo = new Bundle();
                            userinfo.putString("json", response);
                            userinfo.putString("userid", jsonObj.getString("id"));
                            homeintent.putExtras(userinfo);
                            startActivity(homeintent);
                        } else {
                            //isnt pro
                            Intent homeintent = new Intent(getActivity(), HomeActivity.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle userinfo = new Bundle();
                            userinfo.putString("json", response);
                            userinfo.putString("userid", jsonObj.getString("id"));
                            homeintent.putExtras(userinfo);
                            startActivity(homeintent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


}
