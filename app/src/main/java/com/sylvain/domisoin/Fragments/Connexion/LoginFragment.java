package com.sylvain.domisoin.Fragments.Connexion;

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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.Dialogs.LostPasswordDialog;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.JsonUtils;
import com.sylvain.domisoin.Utilities.ManageErrorText;

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
    private String token = "";

    private String login = null;
    private String password = null;

    private Map<String, String> datas = null;


    private static String LOGIN_URL                   = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_LOGIN";
    private Switch remindme = null;
    private boolean remindme_value = false;

    private TextView lost_password = null;

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


        LOGIN_URL = getString(R.string.api_login_url);
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
        remindme = (Switch)loginfragment.findViewById(R.id.remindme);
        remindme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               remindme_value = b;
            }
        });

        datas = new LinkedHashMap<String, String>();

        lost_password = (TextView)loginfragment.findViewById(R.id.lost_password);
        lost_password.setOnClickListener(this);

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
                Boolean verif = false;

                if (TextUtils.isEmpty(login) || !Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    loginEdit.setError("Une adresse email valide est requise");
                    verif = true;
                }

                if (TextUtils.isEmpty(password) || password.length() < 4) {
                    passwordEdit.setError("Un mot de passe de plus de 4 caractères est requis");
                    verif = true;
                }

                if (!verif) {
                    Log.v("LoginValidate", login);
                    Log.v("LoginValidate", password);
                    datas.clear();
                    datas.put("email", String.valueOf(login));
                    datas.put("password", String.valueOf(password));

                    /*for(int i = 0; i < datas.size(); ++i) {
                        Log.d("ParentFragm ALL DATA", datas.keySet().toArray()[i] + " " + datas.values().toArray()[i]);
                       }*/

                    HTTPPostRequest task = new HTTPPostRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, LOGIN_URL, JsonUtils.mapToJson(datas), "");
                    task.execute();
                    progress = ProgressDialog.show(getActivity(), "Authentification", "Vérification en cours, merci de patienter...", true);
                }
                /*else {
                    Toast toast = Toast.makeText(getContext(), "Veuillez renseigner une adresse email et un mot de passe valide.", Toast.LENGTH_LONG);
                    toast.show();
                }*/
                break;
            case R.id.lost_password:
                LostPasswordDialog lost_dialog = new LostPasswordDialog();
                lost_dialog.show(getFragmentManager(), "lost_password");
                //Log.d(TAG, "LOST password");
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
            if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            }
            Log.i(TAG, "RESPONSE = " + response);

            if (response != null) {
                String response_code = "400";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }
                if (response.equals("0")) {
                    Toast toast = Toast.makeText(getContext(), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (Integer.decode(response_code) > 226 ) {
                    Toast toast = Toast.makeText(getContext(), "Une erreur s'est produite, veuillez essayer de nouveau. (" + ManageErrorText.manage_my_error(response) + ")", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        //Log.d(TAG, jsonObj.getString("is_pro"));

                        if (remindme_value) {
                            SharedPreferences sharedPref = getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.save_account), datas.get("email"));
                            editor.putString(getString(R.string.save_password), datas.get("password"));
                            editor.apply();
                        }

                        if (jsonObj.has("token")) {
                            token = jsonObj.getString("token");
                            HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_me), token);
                            task.execute();
                            progress = ProgressDialog.show(getActivity(), "Authentification", "Vérification en cours, merci de patienter...", true);
                        }
                        else if (jsonObj.has("is_pro")) {
                            if (jsonObj.getBoolean("is_pro")) {
                                //is pro
                                Intent homeprointent = new Intent(getActivity(), HomeProActivity.class);
                                homeprointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Bundle userinfo = new Bundle();
                                jsonObj.put("token", token);
                                //userinfo.putString("json", response);
                                userinfo.putString("json", jsonObj.toString());
                                userinfo.putString("userid", jsonObj.getString("id"));
                                userinfo.putString("mdp", datas.get("password"));
                                homeprointent.putExtras(userinfo);
                                startActivity(homeprointent);
                            } else {
                                //isnt pro
                                Intent homecustomerintent = new Intent(getActivity(), HomeCustomerActivity.class);
                                homecustomerintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Bundle userinfo = new Bundle();
                                jsonObj.put("token", token);
                                //userinfo.putString("json", response);
                                userinfo.putString("json", jsonObj.toString());
                                userinfo.putString("userid", jsonObj.getString("id"));
                                userinfo.putString("mdp", datas.get("password"));
                                homecustomerintent.putExtras(userinfo);
                                startActivity(homecustomerintent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


}
