package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sylvain on 14/01/18.
 */

public class ModifyPwdDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = ModifyPwdDialog.class.getSimpleName();

    private View view = null;

    private EditText act_pwd = null;
    private EditText new_pwd = null;
    private EditText ver_new_pwd = null;
    private String saved_pwd = null;
    private userInfo ourdata = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_MODIFY_PWD_DIALOG";
    private ProgressDialog progress = null;


    public ModifyPwdDialog() {}

    @SuppressLint("ValidFragment")
    public ModifyPwdDialog(userInfo _infos) {
        this.ourdata = _infos;
        saved_pwd = ourdata.mdp.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_pwd_dialog, container, false);

        Button validate = (Button)view.findViewById(R.id.validate_modify_pwd);
        validate.setOnClickListener(this);

        act_pwd = (EditText)view.findViewById(R.id.act_pwd_txt);
        new_pwd = (EditText)view.findViewById(R.id.new_pwd_txt);
        ver_new_pwd = (EditText)view.findViewById(R.id.ver_new_pwd_txt);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.50f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.validate_modify_pwd:
                if (!verify_pwd()) {
                    do_http();
                }
                break;
        }
    }

    private Boolean verify_pwd() {
        Boolean error = false;
        if (!saved_pwd.equals(String.valueOf(act_pwd.getText())) || act_pwd.getText().toString().length() == 0) {
            act_pwd.setError("Mauvais mot de passe");
            error = true;
        }
        if (new_pwd.getText().toString().length() < 4) {
            new_pwd.setError("veuillez entrer un mot de passe valide (min 4 caractères)");
            error = true;
        }

        if (!String.valueOf(new_pwd.getText()).equals(String.valueOf(ver_new_pwd.getText()))) {
            ver_new_pwd.setError("Les mots de passe ne sont pas identiques");

            error = true;
        }
        return error;
    }

    private void do_http() {

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            JSONObject newjson = new JSONObject(ourdata.json.get());
            newjson.put("password", new_pwd.getText());
            newjson.put("adresse", ourdata.address.get());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+newjson.get("id")+"/", newjson, ourdata.token.get());
            task.execute();
            progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            }

            if (response != null) {
                Log.d(TAG, response);
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
                }
                else if (Integer.decode(response_code) == 200) {
                    Toast toast = Toast.makeText(getContext(), "Mise à jour reussie", Toast.LENGTH_LONG);
                    toast.show();
                    ourdata.mdp.set(String.valueOf(new_pwd.getText()));
                    dismiss();
                }
            }

        }

    };

}
