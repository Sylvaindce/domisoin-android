package com.sylvain.domisoin.Dialogs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.R;

/**
 * Created by sylvain on 26/04/18.
 */

public class RangeDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogFragment.class.getName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_RANGE_DIALOG";

    private View dialogFragment = null;

    private ProgressDialog progress = null;
    private Spinner range_spinner = null;
    private Button validate = null;
    private ButtonInterface listener = null;

    public RangeDialog() {}

    public RangeDialog(ButtonInterface _listener){
        this.listener = _listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogFragment = inflater.inflate(R.layout.search_range_dialog, container, false);
        getDialog().setTitle("Modification du rayon");

        validate = (Button)dialogFragment.findViewById(R.id.rayon_validate);
        validate.setOnClickListener(this);

        range_spinner = (Spinner)dialogFragment.findViewById(R.id.range_spinner);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.range_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        range_spinner.setAdapter(dataAdapter);

        return dialogFragment;
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
            case R.id.rayon_validate:
                Log.d("Selected Spinner Item", String.valueOf(range_spinner.getSelectedItem()));
                listener.onBookClick(String.valueOf(range_spinner.getSelectedItem()), "");
                getDialog().dismiss();
                break;
            default:
                break;
        }

    }
}
