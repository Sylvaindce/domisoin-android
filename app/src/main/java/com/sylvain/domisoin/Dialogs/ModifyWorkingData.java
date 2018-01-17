package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
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
 * Created by sylvain on 17/01/18.
 */

public class ModifyWorkingData extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = ModifyWorkingData.class.getSimpleName();

    private userInfo _user = null;
    private View view = null;

    public ModifyWorkingData() {}

    @SuppressLint("ValidFragment")
    public ModifyWorkingData(userInfo user) {
        _user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_modify_working_data, container, false);

        Button close = (Button)view.findViewById(R.id.close_button_modify_working_data);
        close.setOnClickListener(this);

        Switch indisponible = (Switch)view.findViewById(R.id.indisponible);
        indisponible.setOnCheckedChangeListener(this);

        Button validate = (Button)view.findViewById(R.id.validate_wd);
        validate.setOnClickListener(this);


        com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar rangeSeekbar = (com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar_wd);
        rangeSeekbar.setCornerRadius(10);
        rangeSeekbar.setMinValue(0);
        rangeSeekbar.setMaxValue(1439);
        rangeSeekbar.setGap(1);
        // get min and max text view
        final TextView begin_hour = (TextView) view.findViewById(R.id.begin_hour_pro_wd);
        final TextView end_hour = (TextView) view.findViewById(R.id.end_hour_pro_wd);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                final Integer pas_rdv = Integer.decode(String.valueOf(_user.duration.get()));

                int minv = minValue.intValue() % pas_rdv;
                int min_result = minValue.intValue();
                if(minValue.intValue()>= 1350 && minValue.intValue() <= 1425) {
                    switch (pas_rdv) {
                        case 30:
                            if (minValue.intValue() >= 1410) {
                                min_result = minValue.intValue() - 15;
                                minv = min_result % pas_rdv;
                            }
                            break;
                        case 45:
                            if (minValue.intValue() >= 1395) {
                                min_result = minValue.intValue() - 30;
                                minv = min_result % pas_rdv;
                            }
                            break;
                        case 60:
                            if (minValue.intValue() >= 1380) {
                                min_result = minValue.intValue() - 45;
                                minv = min_result % pas_rdv;
                            }
                            break;
                    }
                }

                Log.d("Pa sgn pro", String.valueOf(pas_rdv));
                if(minv != 0) {
                    int res = pas_rdv - minv;
                    min_result = minValue.intValue() + res;
                }
                int min_hours = min_result / 60;
                int min_minutes = (int) min_result % 60;
                String min_minutes_str = String.valueOf(min_minutes);
                if (min_minutes <= 0)
                    min_minutes_str = "0"+String.valueOf(min_minutes);


                int maxv = maxValue.intValue() % pas_rdv;
                int max_result = maxValue.intValue();
                if(maxv != 0) {
                    int res2 = pas_rdv - maxv;
                    max_result = maxValue.intValue() + res2;
                }
                int max_hours = max_result / 60;
                int max_minutes = (int) max_result % 60;
                String max_minutes_str = String.valueOf(max_minutes);
                if (max_minutes <= 0)
                    max_minutes_str = "0"+String.valueOf(max_minutes);

                Log.d("Calcul", String.valueOf(minValue.intValue()%60));
                Log.d("Calcul", String.valueOf(minValue.intValue()/60));
                Log.d("Values ", minValue.toString() +" " + maxValue.toString());


                /*if (minValue.intValue() == 1425) {
                    Log.d("ICI", "equal");
                    minv = (minValue.intValue()-pas_rdv) % pas_rdv;
                    min_result = minValue.intValue()-pas_rdv;
                    if(minv != 0) {
                        int res = pas_rdv - minv;
                        min_result = minValue.intValue() + res;
                    }
                    min_hours = min_result / 60;
                    min_minutes = (int) min_result % 60;
                    min_minutes_str = String.valueOf(min_minutes);
                    if (min_minutes <= 0)
                        min_minutes_str = "0"+String.valueOf(min_minutes);
                }*/

                begin_hour.setText(String.valueOf(min_hours)+":"+min_minutes_str);
                end_hour.setText(String.valueOf(max_hours)+":"+max_minutes_str);
            }
        });


        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

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
            case R.id.close_button_modify_working_data:
                dismiss();
                break;
            case R.id.validate_wd:
                Log.d("TOTO", "button");
            break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            //shadow.setVisibility(View.VISIBLE);
        } else {
            //shadow.setVisibility(View.GONE);
        }
    }
}
