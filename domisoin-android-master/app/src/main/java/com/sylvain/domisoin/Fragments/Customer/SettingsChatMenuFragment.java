package com.sylvain.domisoin.Fragments.Customer;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsChatMenuFragment extends Fragment implements View.OnClickListener {

    ButtonInterface buttonInterface = null;

    public SettingsChatMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View connexionFragment = inflater.inflate(R.layout.fragment_settings_chat_menu, container, false);

        Button settings_button = (Button)connexionFragment.findViewById(R.id.settings_button);
        settings_button.setOnClickListener(this);
        Button chat_button = (Button)connexionFragment.findViewById(R.id.chat_button);
        chat_button.setOnClickListener(this);

        return connexionFragment;
    }

    @Override
    public void onClick(View v) {
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        buttonInterface.buttonClicked(v);
        switch (v.getId()){
            case R.id.settings_button:
                //ft.replace(R.id.bottom_sheet_chat_settings, new , "LoginFragment");
                //ft.addToBackStack("login");
                //ft.commit();
                break;
            case R.id.chat_button:
                //Log.v("OnClick", "Signin button");
                /*ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
                params.height = mheight.intValue();
                params.width = width;
                bottomSheet.setLayoutParams(params);
                ft.replace(R.id.bottom_sheet_chat_settings, new MainChatFragment(), "MainChatFragment2");
                ft.addToBackStack("MainChatFragment");
                ft.commit();*/
                break;
        }
    }

    public void setInterface(ButtonInterface ourInterface) {
        this.buttonInterface = ourInterface;
    }


}
