package com.sylvain.domisoin.Fragments.Pro.Workday;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.sylvain.domisoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkHourFragment extends Fragment {

    private String day_name = "";
    private TextView begin_matin = null;
    private TextView end_matin = null;

    private TextView begin_midi = null;
    private TextView end_midi = null;

    private final Integer pas_rdv = 15;

    public WorkHourFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public WorkHourFragment(String day) {
        this.day_name = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_hour, container, false);

        TextView text = (TextView) view.findViewById(R.id.workhour_day);

        begin_matin = (TextView) view.findViewById(R.id.begin_hour_matin);
        end_matin = (TextView) view.findViewById(R.id.end_hour_matin);
        begin_midi = (TextView) view.findViewById(R.id.begin_hour_midi);
        end_midi = (TextView) view.findViewById(R.id.end_hour_midi);

        com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar seek_matin = (com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar) view.findViewById(R.id.seekbar_matin);
        seek_matin.setCornerRadius(10);
        seek_matin.setMinValue(0);
        seek_matin.setMaxValue(1439);
        seek_matin.setGap(1);
        seek_matin.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String min = doCalcHour(minValue);
                begin_matin.setText(min);
                String max = doCalcHour(maxValue);
                end_matin.setText(max);
            }
        });

        com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar seek_midi = (com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar) view.findViewById(R.id.seekbar_midi);
        seek_midi.setCornerRadius(10);
        seek_midi.setMinValue(0);
        seek_midi.setMaxValue(1439);
        seek_midi.setGap(1);
        seek_midi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String min = doCalcHour(minValue);
                begin_midi.setText(min);
                String max = doCalcHour(maxValue);
                end_midi.setText(max);
            }
        });

        switch (this.day_name) {
            case "Lundi":
                Log.d("Toto", "changeID");
                text.setId(R.id.workhour_title_lundi);
                seek_matin.setId(R.id.workhour_seekbar_begin_lundi);
                seek_midi.setId(R.id.workhour_seekbar_end_lundi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_lundi);
                end_matin.setId(R.id.workhour_hour_matin_end_lundi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_lundi);
                end_midi.setId(R.id.workhour_hour_midi_end_lundi);
                break;
            case "Mardi":
                text.setId(R.id.workhour_title_mardi);
                seek_matin.setId(R.id.workhour_seekbar_begin_mardi);
                seek_midi.setId(R.id.workhour_seekbar_end_mardi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_mardi);
                end_matin.setId(R.id.workhour_hour_matin_end_mardi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_mardi);
                end_midi.setId(R.id.workhour_hour_midi_end_mardi);
                break;
            case "Mercredi":
                text.setId(R.id.workhour_title_mercredi);
                seek_matin.setId(R.id.workhour_seekbar_begin_mercredi);
                seek_midi.setId(R.id.workhour_seekbar_end_mercredi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_mercredi);
                end_matin.setId(R.id.workhour_hour_matin_end_mercredi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_mercredi);
                end_midi.setId(R.id.workhour_hour_midi_end_mercredi);
                break;
            case "Jeudi":
                text.setId(R.id.workhour_title_jeudi);
                seek_matin.setId(R.id.workhour_seekbar_begin_jeudi);
                seek_midi.setId(R.id.workhour_seekbar_end_jeudi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_jeudi);
                end_matin.setId(R.id.workhour_hour_matin_end_jeudi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_jeudi);
                end_midi.setId(R.id.workhour_hour_midi_end_jeudi);
                break;
            case "Vendredi":
                text.setId(R.id.workhour_title_vendredi);
                seek_matin.setId(R.id.workhour_seekbar_begin_vendredi);
                seek_midi.setId(R.id.workhour_seekbar_end_vendredi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_vendredi);
                end_matin.setId(R.id.workhour_hour_matin_end_vendredi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_vendredi);
                end_midi.setId(R.id.workhour_hour_midi_end_vendredi);
                break;
            case "Samedi":
                text.setId(R.id.workhour_title_samedi);
                seek_matin.setId(R.id.workhour_seekbar_begin_samedi);
                seek_midi.setId(R.id.workhour_seekbar_end_samedi);
                begin_matin.setId(R.id.workhour_hour_matin_begin_samedi);
                end_matin.setId(R.id.workhour_hour_matin_end_samedi);
                begin_midi.setId(R.id.workhour_hour_midi_begin_samedi);
                end_midi.setId(R.id.workhour_hour_midi_end_samedi);
                break;
            case "Dimanche":
                text.setId(R.id.workhour_title_dimanche);
                seek_matin.setId(R.id.workhour_seekbar_begin_dimanche);
                seek_midi.setId(R.id.workhour_seekbar_end_dimanche);
                begin_matin.setId(R.id.workhour_hour_matin_begin_dimanche);
                end_matin.setId(R.id.workhour_hour_matin_end_dimanche);
                begin_midi.setId(R.id.workhour_hour_midi_begin_dimanche);
                end_midi.setId(R.id.workhour_hour_midi_end_dimanche);
                break;


        }
        text.setText(this.day_name);

        return view;
    }

    private String doCalcHour(Number minValue) {
        int minv = minValue.intValue() % pas_rdv;
        int min_result = minValue.intValue();

        if (minv != 0) {
            int res = pas_rdv - minv;
            min_result = minValue.intValue() + res;
        }
        int min_hours = min_result / 60;
        int min_minutes = min_result % 60;
        String min_minutes_str = String.valueOf(min_minutes);
        if (min_minutes <= 0)
            min_minutes_str = "0" + String.valueOf(min_minutes);
        return String.valueOf(min_hours) + ":" + min_minutes_str;
    }

}

