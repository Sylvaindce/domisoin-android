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

        /*final TextView rdv_dur_txt = (TextView) view.findViewById(R.id.rdv_dur);
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
                final Integer pas_rdv = Integer.decode(String.valueOf(rdv_dur_txt.getText()));

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
                Log.d("Values ", minValue.toString() +" " + maxValue.toString());*/


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
                }

                begin_hour.setText(String.valueOf(min_hours)+":"+min_minutes_str);
                end_hour.setText(String.valueOf(max_hours)+":"+max_minutes_str);
            }
        });


        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });*/


        return view;
    }
}
