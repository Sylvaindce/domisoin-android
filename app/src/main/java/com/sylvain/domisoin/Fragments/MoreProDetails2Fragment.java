package com.sylvain.domisoin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sylvain.domisoin.Dialogs.ProMore;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MoreProDetails2Fragment extends Fragment {
    private static final String TAG = MoreProDetails2Fragment.class.getName();
    private SeekBar time_bar= null;
    private TextView time_value = null;
    public String begin_hours = "T08:00:00Z";
    public String end_hours = "T09:00:00Z";
    private ProMore parent = null;
    private TextView resume = null;

    /* TEST EXPANDABLE */
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    public MoreProDetails2Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pro_details2, container, false);
        parent = (ProMore)getParentFragment();

        /* TEST EXPANDABLE */
        //expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListViewHours);
        //expandableListDetail = ExpandableListDataPump.getData();
        //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        //expandableListView.setAdapter(expandableListAdapter);


        time_value = (TextView)view.findViewById(R.id.seekBar_Value);

        time_bar = (SeekBar)view.findViewById(R.id.seekBar_Time);
        time_bar.setMax(9);
        time_bar.setProgress(0);

        resume = (TextView)view.findViewById(R.id.pro_more_resume);
        resume.setText(parent.ourDate + " de " + begin_hours + " a " + end_hours);

        parent.ourBeginHours = begin_hours;
        parent.ourEndHours = end_hours;

        Log.d("TESTMORE2", parent.get_user().getEvents());

        time_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                switch (progress) {
                    case 0:
                        time_value.setText("08h00 - 09h00");
                        begin_hours = "T08:00:00Z";
                        end_hours = "T09:00:00Z";
                        break;
                    case 1:
                        time_value.setText("09h00 - 10h00");
                        begin_hours = "T09:00:00Z";
                        end_hours = "T10:00:00Z";
                        break;
                    case 2:
                        time_value.setText("10h00 - 11h00");
                        begin_hours = "T10:00:00Z";
                        end_hours = "T11:00:00Z";
                        break;
                    case 3:
                        time_value.setText("11h00 - 12h00");
                        begin_hours = "T11:00:00Z";
                        end_hours = "T12:00:00Z";
                        break;
                    case 4:
                        time_value.setText("13h00 - 14h00");
                        begin_hours = "T13:00:00Z";
                        end_hours = "T14:00:00Z";
                        break;
                    case 5:
                        time_value.setText("14h00 - 15h00");
                        begin_hours = "T14:00:00Z";
                        end_hours = "T15:00:00Z";
                        break;
                    case 6:
                        time_value.setText("15h00 - 16h00");
                        begin_hours = "T15:00:00Z";
                        end_hours = "T16:00:00Z";
                        break;
                    case 7:
                        time_value.setText("16h00 - 17h00");
                        begin_hours = "T16:00:00Z";
                        end_hours = "T17:00:00Z";
                        break;
                    case 8:
                        time_value.setText("17h00 - 18h00");
                        begin_hours = "T17:00:00Z";
                        end_hours = "T18:00:00Z";
                        break;
                    case 9:
                        time_value.setText("18h00 - 19h00");
                        begin_hours = "T18:00:00Z";
                        end_hours = "T19:00:00Z";
                        break;
                }
                parent.ourBeginHours = begin_hours;
                parent.ourEndHours = end_hours;
                resume.setText(parent.ourDate + " de " + begin_hours + " a " + end_hours);
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