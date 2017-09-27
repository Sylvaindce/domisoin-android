package com.sylvain.domisoin.Fragments;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sylvain.domisoin.Activities.HomeActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.databinding.FragmentAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_ACCOUNT_ACTIVITY";

    private String url = "";

    private FragmentAccountBinding fragmentAccountBinding = null;
    private HomeActivity homeActivity = null;
    private userInfo UserInfo = null;
    private ImageButton editInfo = null;

    private EditText account_jobtitle = null;
    private EditText account_workphone = null;
    private EditText account_email = null;
    private EditText account_address = null;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        homeActivity = (HomeActivity) getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentAccountBinding.setUser(UserInfo);

        editInfo = (ImageButton) fragmentAccountBinding.getRoot().findViewById(R.id.account_modify_button);
        editInfo.setOnClickListener(this);

        account_jobtitle = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_jobtitle);
        account_workphone = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_workphone);
        account_email = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_email);
        account_address = (EditText) fragmentAccountBinding.getRoot().findViewById(R.id.account_address);

        //UserInfo.jsonAnswer.set(getArguments().getString("infofrag"));

        url = getString(R.string.api_url)+"users/";

        return fragmentAccountBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.account_modify_button:
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
                break;
        }

    }

    private void doUpdate() {
        userInfo userinfo = ((HomeActivity)getActivity()).getUserInfo();

        try {
            JSONObject newjson = new JSONObject(userinfo.json.get());
            newjson.put("job_title", account_jobtitle.getText());
            newjson.put("workphone", account_workphone.getText());
            newjson.put("email", account_email.getText());
            newjson.put("adresse", account_address.getText());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, url+newjson.get("id")+"/", newjson);
            task.execute();
            ((HomeActivity)getActivity()).progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

