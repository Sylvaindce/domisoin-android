package com.sylvain.domisoin.Fragments.Customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sylvain.domisoin.Dialogs.ProMore;
import com.sylvain.domisoin.Interfaces.ButtonInterface;
import com.sylvain.domisoin.Models.TitleCalendarValueModel;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

//implements CalendarView.OnDateChangeListener

public class MoreProDetailsFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    private static final String TAG = MoreProDetailsFragment.class.getName();
    private CalendarView calendarView = null;
    private UserModel user = null;
    //public String ourdate = "";
    //private ProMore parent = null;


    private CustomExpandableListAdapter listAdapterExp;
    private ExpandableListView expListView;
    private List<TitleCalendarValueModel> listDataHeader;
    private HashMap<String, List<String>> listDataChild;


    private Calendar currentCalendarView = null;
    private SimpleDateFormat sdf_disp = null;
    private SimpleDateFormat sdf_api = null;

    private ButtonInterface buttonInterface = null;

    public MoreProDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pro_details, container, false);
        user = ((ProMore)getParentFragment()).get_user();

        //parent = (ProMore)getParentFragment();

        //calendarView = (CalendarView)view.findViewById(R.id.pro_more_calendar);
        currentCalendarView = Calendar.getInstance();
        //Calendar calendr = currentCalendarView;
        //sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf_disp = new SimpleDateFormat("EEE dd MMM yyyy");
        sdf_api = new SimpleDateFormat("yyyy-MM-dd");
        /*calendarView.setMinDate(calendr.getTimeInMillis());


        ourdate = calendr.get(Calendar.YEAR) + "-";
        if (calendr.get(Calendar.MONTH) < 10) {
            ourdate = ourdate+"0"+String.valueOf(calendr.get(Calendar.MONTH));
        } else {
            ourdate = ourdate + String.valueOf(calendr.get(Calendar.MONTH));
        }
        ourdate = ourdate+"-";
        if (calendr.get(Calendar.DAY_OF_MONTH) < 10) {
            ourdate = ourdate+"0"+String.valueOf(calendr.get(Calendar.DAY_OF_MONTH));
        } else {
            ourdate = ourdate + String.valueOf(calendr.get(Calendar.DAY_OF_MONTH));
        }


        Log.d(TAG, ourdate);*/
        //ourdate = sdf_disp.format(currentCalendarView.getTimeInMillis());
        //parent.setOurDate(ourdate);
        //Log.d(TAG, ourdate);
        /*calendr.set(Calendar.DAY_OF_MONTH, currentCalendarView.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarView.setMaxDate(calendr.getTimeInMillis());
        calendarView.setOnDateChangeListener(this);*/

        TextView address = (TextView)view.findViewById(R.id.pro_more_address);
        address.setText(user.getAddress());

        TextView phone = (TextView) view.findViewById(R.id.pro_more_phone);
        phone.setText(user.getWorkphone());


        Log.d("TEST", user.getEvents());


        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.expList2);

        // preparing list data
        prepareListData();

        listAdapterExp = new CustomExpandableListAdapter(view.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapterExp);

        expListView.setOnChildClickListener(this);

        return view;
    }

    /*@Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        ourdate = String.valueOf(year) + "-";
        if (month+1 < 10) {
            ourdate = ourdate+"0"+String.valueOf(month+1);
        } else {
            ourdate = ourdate + String.valueOf(month+1);
        }
        ourdate = ourdate+"-";
        if (dayOfMonth < 10) {
            ourdate = ourdate+"0"+String.valueOf(dayOfMonth);
        } else {
            ourdate = ourdate + String.valueOf(dayOfMonth);
        }
        Log.d(TAG, ourdate);
        parent.ourDate = ourdate;
    }*/

    private void prepareListData() {
        listDataHeader = new LinkedList<TitleCalendarValueModel>();
        listDataChild = new HashMap<String, List<String>>();
        LinkedList<String> listpro_events = new LinkedList<String>();

        //init header semaine
        Calendar tmp = currentCalendarView;
        for (int i = 0; i < 7; ++i) {
            tmp.add(Calendar.DAY_OF_MONTH, 1);
            TitleCalendarValueModel titleCalendarValue = new TitleCalendarValueModel(sdf_disp.format(tmp.getTime()), tmp.getTime());
            listDataHeader.add(titleCalendarValue);
        }

        // Adding child data
        List<String> horaires = new ArrayList<String>();
        horaires.add("08h00 - 09h00");
        horaires.add("09h00 - 10h00");
        horaires.add("10h00 - 11h00");
        horaires.add("11h00 - 12h00");
        horaires.add("13h00 - 14h00");
        horaires.add("14h00 - 15h00");
        horaires.add("15h00 - 16h00");
        horaires.add("16h00 - 17h00");
        horaires.add("17h00 - 18h00");
        horaires.add("18h00 - 19h00");

        //Check pro events
        try {
            JSONArray pro_events = new JSONArray(user.getEvents());

            for (int i = 0; i < pro_events.length(); ++i) {
                JSONObject one_event = pro_events.getJSONObject(i);
                listpro_events.add(one_event.getString("start_date"));
                Log.d("Test Smart Calendar", one_event.getString("start_date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int flag = 0;
        String day_api = "";
        String hour_api = "";
        String dayhour_api = "";

        for (int i = 0; i < listDataHeader.size(); ++i) {
            day_api = sdf_api.format(listDataHeader.get(i).get_time())+"T";
            List<String> tmp_horaires = new ArrayList<String>();
            for (int j = 0; j < horaires.size(); ++j) {
                hour_api = horaires.get(j).substring(0, 5);
                hour_api = hour_api.replace('h', ':');
                hour_api = hour_api+":00Z";
                dayhour_api = day_api+hour_api;
                flag = 0;
                for (int k = 0; k < listpro_events.size(); ++k) {
                    //Log.d("Test Smart Cal today", dayhour_api);
                    //Log.d("Test Smart Cal pro", listpro_events.get(k));
                    if (dayhour_api.equals(String.valueOf(listpro_events.get(k)))) {
                        Log.d("Smart Cal Equal", dayhour_api);
                        flag = -1;
                    }
                }
                if(flag == 0) {
                    tmp_horaires.add(horaires.get(j));
                }
            }
            listDataChild.put(listDataHeader.get(i).get_title(), tmp_horaires);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        Toast.makeText(getContext(), listDataHeader.get(i).get_title() + listDataChild.get(listDataHeader.get(i).get_title()).get(i1), Toast.LENGTH_LONG).show();
        //parent.ourDate = sdf_api.format(listDataHeader.get(i).get_time());
        //parent.setOurDate(sdf_api.format(listDataHeader.get(i).get_time()));
        //Log.d(TAG, parent.getOurDate());

        String Bhour = listDataChild.get(listDataHeader.get(i).get_title()).get(i1).split("-")[0].replaceAll(" ", "");
        Bhour = Bhour.replaceAll("h", ":");
        Bhour = Bhour + ":00Z";

        String Ehour = listDataChild.get(listDataHeader.get(i).get_title()).get(i1).split("-")[1].replaceAll(" ", "");
        Ehour = Ehour.replaceAll("h", ":");
        Ehour = Ehour + ":00Z";

        //Log.d("OURHOUR", Bhour);

        String Bdate = sdf_api.format(listDataHeader.get(i).get_time()) + "T" + Bhour;
        String Edate = sdf_api.format(listDataHeader.get(i).get_time()) + "T" + Ehour;
        //buttonInterface.onBookClick(sdf_api.format(listDataHeader.get(i).get_time()));
        buttonInterface.onBookClick(Bdate, Edate);

        return false;
    }

    public void setInterface(ButtonInterface ourInterface) {
        this.buttonInterface = ourInterface;
    }

}
