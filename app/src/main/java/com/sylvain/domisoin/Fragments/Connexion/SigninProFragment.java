package com.sylvain.domisoin.Fragments.Connexion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.sylvain.domisoin.R;


public class SigninProFragment extends Fragment {

    public SigninProFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signinpro, container, false);


        com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar rangeSeekbar = (com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar);
        rangeSeekbar.setCornerRadius(10);
        rangeSeekbar.setMinValue(0);
        rangeSeekbar.setMaxValue(1439);
        rangeSeekbar.setGap(1);
        // get min and max text view
        final TextView begin_hour = (TextView) view.findViewById(R.id.begin_hour_pro);
        final TextView end_hour = (TextView) view.findViewById(R.id.end_hour_pro);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                int minv = minValue.intValue() % 15;
                int min_result = minValue.intValue();
                if(minv != 0) {
                    int res = 15 - minv;
                    min_result = minValue.intValue() + res;
                }
                int min_hours = min_result / 60;
                int min_minutes = (int) min_result % 60;
                String min_minutes_str = String.valueOf(min_minutes);
                if (min_minutes <= 0)
                    min_minutes_str = "0"+String.valueOf(min_minutes);


                int maxv = maxValue.intValue() % 15;
                int max_result = maxValue.intValue();
                if(maxv != 0) {
                    int res2 = 15 - maxv;
                    max_result = maxValue.intValue() + res2;
                }
                int max_hours = max_result / 60;
                int max_minutes = (int) max_result % 60;
                String max_minutes_str = String.valueOf(max_minutes);
                if (max_minutes <= 0)
                    max_minutes_str = "0"+String.valueOf(max_minutes);


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

        final TextView rdv_dur_txt = (TextView) view.findViewById(R.id.rdv_dur);
        SeekBar rdv_dur_sb = (SeekBar) view.findViewById(R.id.rdv_dur_sb);

        rdv_dur_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                int min = 15;
                int ourv = value%15;
                if (value<15)
                    value = 15;
                else if (ourv != 0) {
                    int res = min - ourv;
                    value = value+res;
                }
                Log.d("SeekBar", String.valueOf(value));
                rdv_dur_txt.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }
}
