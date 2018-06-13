package com.sylvain.domisoin.Dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sylvain.domisoin.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ShowWorkingHourFragment extends DialogFragment implements View.OnClickListener {

    private String hour_data = "";
    private JSONObject hour_data_json = null;

    public ShowWorkingHourFragment() {
        // Required empty public constructor
    }



    @SuppressLint("ValidFragment")
    public ShowWorkingHourFragment(String hour) {
        // Required empty public constructor
        this.hour_data = hour;
        try {
            hour_data_json = new JSONObject(this.hour_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ShowWorkHour", this.hour_data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_working_hour, container, false);

        Button close = (Button)view.findViewById(R.id.close_showWD);
        close.setOnClickListener(this);

        try {
            TextView desc = (TextView) view.findViewById(R.id.show_desc_lundi);
            JSONObject day = hour_data_json.getJSONObject("monday");
            JSONObject morning = day.getJSONObject("morning");
            JSONObject afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));
            }

            desc = (TextView) view.findViewById(R.id.show_desc_mardi);
            day = hour_data_json.getJSONObject("tuesday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }

            desc = (TextView) view.findViewById(R.id.show_desc_mercredi);
            day = hour_data_json.getJSONObject("wednesday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }

            desc = (TextView) view.findViewById(R.id.show_desc_jeudi);
            day = hour_data_json.getJSONObject("thursday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }

            desc = (TextView) view.findViewById(R.id.show_desc_vendredi);
            day = hour_data_json.getJSONObject("friday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }

            desc = (TextView) view.findViewById(R.id.show_desc_samedi);
            day = hour_data_json.getJSONObject("saturday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }

            desc = (TextView) view.findViewById(R.id.show_desc_dimanche);
            day = hour_data_json.getJSONObject("sunday");
            morning = day.getJSONObject("morning");
            afternoon = day.getJSONObject("afternoon");
            if (day.getBoolean("is_work")) {
                desc.setText("De " + minToHour(morning.getString("start")) + " à " + minToHour(morning.getString("end")) + " et de " + minToHour(afternoon.getString("start")) + " à " + minToHour(afternoon.getString("end")));            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private String minToHour(String mins) {
        int min = Integer.parseInt(mins);
        int hour = (int)Math.floor(min / 60);
        int minute = min % 60;

        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.50f;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.close_showWD:
                getDialog().dismiss();
                break;
        }

    }
}
