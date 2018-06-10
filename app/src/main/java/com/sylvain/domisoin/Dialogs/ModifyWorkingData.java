package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Fragments.Connexion.LoginFragment;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by sylvain on 17/01/18.
 */

public class ModifyWorkingData extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = ModifyWorkingData.class.getSimpleName();

    private userInfo _user = null;
    private View view = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_MODIFY_WD";
    //private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_OPTIONSFRAG";
    private ProgressDialog progress = null;

    private CheckBox wd0 = null;
    private CheckBox wd1 = null;
    private CheckBox wd2 = null;
    private CheckBox wd3 = null;
    private CheckBox wd4 = null;
    private CheckBox wd5 = null;
    private CheckBox wd6 = null;

    private String begin_hour_str = "";
    private String end_hour_str = "";
    private JSONArray day_ar = new JSONArray();

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
        if (_user.day_offs.get().length() == 6) {
            indisponible.setChecked(true);
        }

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

                begin_hour_str = String.valueOf(min_hours)+":"+min_minutes_str;
                end_hour_str = String.valueOf(max_hours)+":"+max_minutes_str;

                begin_hour.setText(String.valueOf(min_hours)+":"+min_minutes_str);
                end_hour.setText(String.valueOf(max_hours)+":"+max_minutes_str);
            }
        });

        begin_hour.setText(_user.begin_working_hour.get() + ":" + _user.begin_working_minutes.get());
        end_hour.setText(_user.end_working_hour.get() + ":"+_user.end_working_minutes.get());

        //WORKING DAY BEGIN
        wd0 = (CheckBox)view.findViewById(R.id.wd_0);
        wd0.setChecked(true);
        wd1 = (CheckBox)view.findViewById(R.id.wd_1);
        wd1.setChecked(true);
        wd2 = (CheckBox)view.findViewById(R.id.wd_2);
        wd2.setChecked(true);
        wd3 = (CheckBox)view.findViewById(R.id.wd_3);
        wd3.setChecked(true);
        wd4 = (CheckBox)view.findViewById(R.id.wd_4);
        wd4.setChecked(true);
        wd5 = (CheckBox)view.findViewById(R.id.wd_5);
        wd5.setChecked(true);
        wd6 = (CheckBox)view.findViewById(R.id.wd_6);
        wd6.setChecked(true);

        JSONArray days = _user.day_offs.get();
        for (int i = 0; i < days.length(); ++i) {
            try {
                Log.d("JOUR OFF", String.valueOf(days.get(i)));
                switch (Integer.decode(String.valueOf(days.get(i)))) {
                    case 1:
                        wd0.setChecked(false);
                        break;
                    case 2:
                        wd1.setChecked(false);
                        break;
                    case 3:
                        wd2.setChecked(false);
                        break;
                    case 4:
                        wd3.setChecked(false);
                        break;
                    case 5:
                        wd4.setChecked(false);
                        break;
                    case 6:
                        wd5.setChecked(false);
                        break;
                    case 7:
                        wd6.setChecked(false);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
                validate_data();
            break;

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            //shadow.setVisibility(View.VISIBLE);
            day_ar = new JSONArray();
            day_ar.put(1);
            day_ar.put(2);
            day_ar.put(3);
            day_ar.put(4);
            day_ar.put(5);
            day_ar.put(6);
            day_ar.put(7);
            try {
                JSONObject newjson = new JSONObject();
                newjson.put("day_offs", day_ar);
                newjson.put("adresse", _user.address.get());

                HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+_user.id.get()+"/", newjson, _user.token.get());
                task.execute();
                progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void validate_data() {
        day_ar = new JSONArray();
        if (!wd0.isChecked()) {
            //day_offs+="\"Lundi\",";
            day_ar.put(1);
        }
        if (!wd1.isChecked()) {
            //day_offs += "\"Mardi\",";
            day_ar.put(2);
        }
        if (!wd2.isChecked()) {
            //day_offs += "\"Mercredi\",";
            day_ar.put(3);
        }
        if (!wd3.isChecked()) {
            //day_offs += "\"Jeudi\",";
            day_ar.put(4);
        }
        if (!wd4.isChecked()) {
            //day_offs += "\"Vendredi\",";
            day_ar.put(5);
        }
        if (!wd5.isChecked()) {
            //day_offs += "\"Samedi\",";
            day_ar.put(6);
        }
        if (!wd6.isChecked()) {
            //day_offs += "\"Dimanche\"";
            day_ar.put(7);
        }

        try {
            JSONObject newjson = new JSONObject();
            newjson.put("day_offs", day_ar);
            newjson.put("start_working_hour", begin_hour_str.split(":")[0]);
            newjson.put("start_working_minutes", begin_hour_str.split(":")[1]);
            newjson.put("end_working_hour", end_hour_str.split(":")[0]);
            newjson.put("end_working_minutes", end_hour_str.split(":")[1]);
            newjson.put("adresse", _user.address.get());

            HTTPPutRequest task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+_user.id.get()+"/", newjson, _user.token.get());
            task.execute();
            progress = ProgressDialog.show(getActivity(), "Validation", "Mise à jour en cours, merci de patienter...", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
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
                } else if (Integer.decode(response_code) == 200) {
                    _user.day_offs.set(day_ar);
                    _user.begin_working_hour.set(begin_hour_str.split(":")[0]);
                    _user.end_working_hour.set(end_hour_str.split(":")[0]);
                    Log.d("TAST", _user.begin_working_hour.get());
                }
                else {
                   Log.d(TAG, response);
                }
            }
        }
    };

}
