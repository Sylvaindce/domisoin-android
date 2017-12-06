package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sylvain.domisoin.Fragments.Customer.PlanningFragment;
import com.sylvain.domisoin.Fragments.Pro.PlanningProFragment;
import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sylvain on 30/06/2017.
 */

public class AppointmentMore extends DialogFragment implements View.OnClickListener {
    private String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING";
    private static final String TAG = AppointmentMore.class.getName();
    private View dialogFragment = null;
    private AppointmentModel _apt = null;

    public AppointmentMore(){}

    @SuppressLint("ValidFragment")
    public AppointmentMore(String intent){
        ACTION_FOR_INTENT_CALLBACK = intent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogFragment = inflater.inflate(R.layout.appointment_more_dialog, container, false);
        getDialog().setTitle("Plus d'informations");

        try {
            JSONObject pro_name = new JSONObject(_apt.getClient_id());

            TextView author = (TextView) dialogFragment.findViewById(R.id.txt_author);
            author.setText(pro_name.getString("last_name") + " " + pro_name.getString("first_name"));

            pro_name = new JSONObject(_apt.getAuthor_id());
            TextView client = (TextView) dialogFragment.findViewById(R.id.txt_client);
            client.setText(pro_name.getString("last_name") + " " + pro_name.getString("first_name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView desc = (TextView) dialogFragment.findViewById(R.id.txt_desc);
        desc.setText(_apt.getDescription());
        TextView type = (TextView) dialogFragment.findViewById(R.id.txt_type);
        type.setText(_apt.getType());



        TextView txt_date = (TextView) dialogFragment.findViewById(R.id.txt_date);

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf_disp = new SimpleDateFormat("EEE dd MMM yyyy hh:mm aa");
        String date = sdf_disp.format(_apt.getStart_date());
        txt_date.setText(date);
        TextView location = (TextView) dialogFragment.findViewById(R.id.txt_location);
        location.setText(_apt.getLocation());

        Button delete_button = (Button) dialogFragment.findViewById(R.id.delete_appointment_button);
        delete_button.setOnClickListener(this);

        Button close_button = (Button) dialogFragment.findViewById(R.id.close_appointment_more_button);
        close_button.setOnClickListener(this);

        Button validate_button = (Button) dialogFragment.findViewById(R.id.validate_appointment_more_button);
        validate_button.setOnClickListener(this);
        if (ACTION_FOR_INTENT_CALLBACK.equals("THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING_PRO") && !_apt.getIs_validate()) {
            validate_button.setVisibility(View.VISIBLE);
        }

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
    public void onClick(View v) {
        String url = getString(R.string.api_url)+"events/"+_apt.getId()+"/";
        switch (v.getId()) {
            case R.id.close_appointment_more_button:
                getDialog().dismiss();
                break;
            case R.id.delete_appointment_button:
                HTTPDeleteRequest task = new HTTPDeleteRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, url);
                task.execute();

                switch (ACTION_FOR_INTENT_CALLBACK) {
                    case "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING":
                        PlanningFragment.progress = ProgressDialog.show(getActivity(), "Annulation", "Annulation de votre rendez-vous en cours, merci de patienter...", true);
                        break;
                    case "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING_PRO":
                        PlanningProFragment.progress = ProgressDialog.show(getActivity(), "Annulation", "Annulation de votre rendez-vous en cours, merci de patienter...", true);
                        break;
                }
                getDialog().dismiss();
                break;
            case R.id.validate_appointment_more_button:
                Log.d("AppointmentMore", "Validate");
                _apt.setIs_validate(Boolean.TRUE);
                HTTPPutRequest put_task = new HTTPPutRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, url, JsonUtils.AppointmentToJSON(_apt));
                put_task.execute();
                PlanningProFragment.progress = ProgressDialog.show(getActivity(), "Mise à jour", "Mise à jour du votre rendez-vous en cours, merci de patienter...", true);
                getDialog().dismiss();
                break;
        }
    }

    public void set_apt(AppointmentModel apt) {
        this._apt = apt;
    }

}
