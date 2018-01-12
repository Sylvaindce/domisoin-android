package com.sylvain.domisoin.Fragments.Pro;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sylvain.domisoin.Activities.ConnexionActivity;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Customer.AccountFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.databinding.FragmentProAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class AccountProFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_HOME_PRO_ACTIVITY";

    private FragmentProAccountBinding fragmentProAccountBinding = null;
    private HomeProActivity homeActivity = null;
    private userInfo UserInfo = null;
    private ImageButton editInfo = null;

    private EditText account_jobtitle = null;
    private EditText account_workphone = null;
    private EditText account_email = null;
    private EditText account_address = null;
    private ImageButton supprimer = null;
    private static String DELETE_URL = null;
    private ProgressDialog progress;


    public AccountProFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro_account, container, false);
        homeActivity = (HomeProActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentProAccountBinding.setUser(UserInfo);

        editInfo = (ImageButton) fragmentProAccountBinding.getRoot().findViewById(R.id.account_modify_button_pro);
        editInfo.setOnClickListener(this);

        supprimer = (ImageButton) fragmentProAccountBinding.getRoot().findViewById(R.id.supprimer);
        supprimer.setOnClickListener(this);

        account_jobtitle = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_jobtitle_pro);
        account_workphone = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_workphone_pro);
        account_email = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_email_pro);
        account_address = (EditText) fragmentProAccountBinding.getRoot().findViewById(R.id.account_address_pro);

        //UserInfo.jsonAnswer.set(getArguments().getString("infofrag"));
        DELETE_URL = getString(R.string.api_users_url)+UserInfo.id.get()+"/";

        return fragmentProAccountBinding.getRoot();
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
            case R.id.supprimer:
                Log.d(TAG, "suppresion");
                HTTPDeleteRequest task = new HTTPDeleteRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, DELETE_URL,UserInfo.token.get());
                task.execute();
                progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);
                /*SharedPreferences sharedPref = getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.save_account));
                editor.remove(getString(R.string.save_password));
                editor.apply();
                Intent connIntent = new Intent(getActivity(), ConnexionActivity.class);
                connIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(connIntent);*/
                break;
           /* case R.id.account_modify_button_pro:
                Log.d(TAG, "account modify info");
                if (account_jobtitle.isEnabled()) {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_create_black_24dp));
                    account_jobtitle.setEnabled(false);
                    account_workphone.setEnabled(false);
                    account_email.setEnabled(false);
                    account_address.setEnabled(false);
                    doUpdate();
                } else {
                    editInfo.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
                    account_jobtitle.setEnabled(true);
                    account_workphone.setEnabled(true);
                    account_email.setEnabled(true);
                    account_address.setEnabled(true);
                }
                break;*/
        }

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null)
            {
                Toast.makeText(getActivity(),"khraaa" ,Toast.LENGTH_LONG).show();
                progress.dismiss();
            }

            String response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);
            if(response== null) {
                Toast.makeText(getActivity(),"khraaa2" ,Toast.LENGTH_LONG).show();

            }
            if (response != null) {
                if (response.length() == 3) {
                    Snackbar.make(fragmentProAccountBinding.getRoot(), "Une erreur s'est produite, veuillez verifier vos informations et essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        }

        ;
    };
    private void doUpdate() {
        userInfo userinfo = ((HomeProActivity)getActivity()).getUserInfo();

        try {
            JSONObject newjson = new JSONObject(userinfo.json.get());
            newjson.put("job_title", account_jobtitle.getText());
            newjson.put("workphone", account_workphone.getText());
            newjson.put("email", account_email.getText());
            newjson.put("adresse", account_address.getText());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+newjson.get("id")+"/", newjson, UserInfo.token.get());
            task.execute();
            ((HomeProActivity)getActivity()).progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
