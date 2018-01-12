package com.sylvain.domisoin.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.sylvain.domisoin.R;

/**
 * Created by sylvain on 12/01/18.
 */

public class LostPasswordDialog extends DialogFragment implements View.OnClickListener {

    private View dialogFragment = null;
    private Button valider = null;
    private EditText email = null;

    public LostPasswordDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogFragment = inflater.inflate(R.layout.lost_password_dialog, container, false);
        getDialog().setTitle("Récupération de mot de passe");

        valider = (Button) dialogFragment.findViewById(R.id.validate_recover_password);
        valider.setOnClickListener(this);

        email = (EditText)dialogFragment.findViewById(R.id.recover_email);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.validate_recover_password:
                getDialog().dismiss();
                break;
        }

    }
}
