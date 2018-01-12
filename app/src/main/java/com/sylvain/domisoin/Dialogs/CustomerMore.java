package com.sylvain.domisoin.Dialogs;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;

import java.util.LinkedHashMap;

/**
 * Created by Sylvain on 14/10/2017.
 */

public class CustomerMore extends DialogFragment implements View.OnClickListener {

    private static final String TAG = CustomerMore.class.getName();
    private View dialogFragment = null;
    private UserModel _user = null;
    private Button closeButton = null;

    public CustomerMore(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogFragment = inflater.inflate(R.layout.customer_form, container, false);
        getDialog().setTitle("Plus d'informations");
        getDialog().setCancelable(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView name = (TextView) dialogFragment.findViewById(R.id.customer_more_name);
        name.setText(_user.getFirst_name() + " " + _user.getLast_name());
        TextView profession = (TextView) dialogFragment.findViewById(R.id.customer_more_job);
        profession.setText(_user.getJob_title());

        TextView address = (TextView) dialogFragment.findViewById(R.id.customer_more_address);
        address.setText(_user.getAddress());

        TextView phone = (TextView) dialogFragment.findViewById(R.id.customer_more_workphone);
        phone.setText(_user.getWorkphone());

        TextView email = (TextView) dialogFragment.findViewById(R.id.customer_more_email);
        email.setText(_user.getEmail());

        closeButton = (Button) dialogFragment.findViewById(R.id.customer_more_close_button);
        closeButton.setOnClickListener(this);


        return dialogFragment;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.85);
        int screenHeight = (int) (metrics.heightPixels * 0.75);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);
    }

    public void set_user(UserModel apt) {
        this._user = apt;
    }

    public UserModel get_user() {
        return _user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_more_close_button:
                dismiss();
                break;
        }
    }
}
