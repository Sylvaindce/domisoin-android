package com.sylvain.domisoin.Fragments.Customer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sylvain.domisoin.Dialogs.AppointmentMore;
import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.Utilities.CustomPlanningListView;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.databinding.FragmentPlanningBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanningFragment extends Fragment {
    private static final String TAG = PlanningFragment.class.getSimpleName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING";

    private FragmentPlanningBinding fragmentPlanningBinding = null;
    private HomeCustomerActivity homeActivity = null;
    private userInfo UserInfo = null;

    public  static ProgressDialog progress;

    private CustomPlanningListView mAdapter = null;
    private ListView planning = null;

    private List<AppointmentModel> appointments = null;

    private int oldclickposition = -1;

    private JSONArray jsonevents = null;

    private TextView nordv = null;


    public PlanningFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentPlanningBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_planning, container, false);
        homeActivity = (HomeCustomerActivity)getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentPlanningBinding.setUser(UserInfo);

        planning = (ListView)fragmentPlanningBinding.getRoot().findViewById(R.id.planning);
        nordv = (TextView)fragmentPlanningBinding.getRoot().findViewById(R.id.nordv);
        appointments = new ArrayList<AppointmentModel>();
        mAdapter = new CustomPlanningListView(getContext());

        try {
            jsonevents = new JSONArray(UserInfo.events.get());
            for(int i = 0; i < jsonevents.length(); ++i) {
                AppointmentModel apm = new AppointmentModel();
                JSONObject obj_tmp = jsonevents.getJSONObject(i);
                apm.setId(obj_tmp.getString("id"));
                apm.setDescription(obj_tmp.getString("description"));
                apm.setAuthor_id(obj_tmp.getString("author"));
                apm.setClient_id(obj_tmp.getString("client"));
                apm.setType(obj_tmp.getString("type"));
                apm.setStart_date(obj_tmp.getString("start_date"));
                apm.setEnd_date(obj_tmp.getString("end_date"));
                apm.setLocation(obj_tmp.getString("location"));
                apm.setDuration(obj_tmp.getString("duration"));
                apm.setLink(obj_tmp.getString("link"));
                apm.setIs_validate(obj_tmp.getBoolean("is_validate"));
                appointments.add(apm);
            }
            Log.d(TAG, jsonevents.length() + " " + jsonevents.toString());
            setAppointment();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fragmentPlanningBinding.getRoot();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("setUserVisibleHint", "is visible Planning");

            if (UserInfo != null) {
                mAdapter.clear();
                appointments.clear();
                try {
                    jsonevents = new JSONArray(UserInfo.events.get());
                    for(int i = 0; i < jsonevents.length(); ++i) {
                        AppointmentModel apm = new AppointmentModel();
                        JSONObject obj_tmp = jsonevents.getJSONObject(i);
                        apm.setId(obj_tmp.getString("id"));
                        apm.setDescription(obj_tmp.getString("description"));
                        apm.setAuthor_id(obj_tmp.getString("author"));
                        apm.setClient_id(obj_tmp.getString("client"));
                        apm.setType(obj_tmp.getString("type"));
                        apm.setStart_date(obj_tmp.getString("start_date"));
                        apm.setEnd_date(obj_tmp.getString("end_date"));
                        apm.setLocation(obj_tmp.getString("location"));
                        apm.setDuration(obj_tmp.getString("duration"));
                        apm.setLink(obj_tmp.getString("link"));
                        apm.setIs_validate(obj_tmp.getBoolean("is_validate"));
                        appointments.add(apm);
                    }
                    Log.d(TAG, jsonevents.length() + " " + jsonevents.toString());
                    setAppointment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
        }
    }

    private void setAppointment() {
        mAdapter = new CustomPlanningListView(getContext());
        int oldtime = 0;

        nordv.setVisibility(View.VISIBLE);

        for (int i = 0; i < appointments.size(); ++i) {
            Date date = appointments.get(i).getStart_date();
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun
            String monthNumber  = (String) DateFormat.format("MM",   date); // 06
            String year         = (String) DateFormat.format("yyyy", date);
            Log.d(TAG, appointments.get(i).getStart_date_str());
            String hour = appointments.get(i).getStart_date_str().split("T")[1].substring(0, 5);

            if (i == 0 || oldtime != date.getDate()) {
                mAdapter.addSectionHeaderItem(dayOfTheWeek + " " + day + " " + monthString);
                mAdapter.addIs_Validate(appointments.get(i).getIs_validate());
                mAdapter.addItemStat(hour +" - "+ appointments.get(i).getDescription() + " " + appointments.get(i).getType(), appointments.get(i).getId(), appointments.get(i).getIs_validate());
            } else {
                mAdapter.addItemStat(hour +" - "+ appointments.get(i).getDescription() + " " + appointments.get(i).getType(), appointments.get(i).getId(), appointments.get(i).getIs_validate());
                //mAdapter.addIs_Validate(appointments.get(i).getIs_validate());
            }
            oldtime = date.getDate();
            nordv.setVisibility(View.INVISIBLE);
        }
        planning.setAdapter(mAdapter);
        planning.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.i("PlanningLV Onclick!", String.valueOf(position) + "  " + mAdapter.getItemIdStr(position));

                AppointmentModel ourapt = getAptFromPosition(position);
                oldclickposition = position;
                AppointmentMore dialog = new AppointmentMore();
                dialog.set_apt(ourapt);
                dialog.show(getFragmentManager(), "more");

            }

        });
    }

    public AppointmentModel getAptFromPosition(int position) {
        AppointmentModel ourapt = null;
        for(int i = 0; i < appointments.size(); ++i) {
            if (appointments.get(i).getId().equals(mAdapter.getItemIdStr(position))) {
                ourapt = appointments.get(i);
            }
        }
        return ourapt;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);
            if (response != null) {
                if (response.equals("204")) {
                    JSONArray tmparray = new JSONArray();
                    for (int i = 0; i < jsonevents.length(); ++i) {
                        try {
                            JSONObject tmp = jsonevents.getJSONObject(i);
                            if (!tmp.getString("id").equals(mAdapter.getItemIdStr(oldclickposition))) {
                                tmparray.put(tmp);
                            } else {
                                Log.i(TAG, tmp.getString("id") + " " + mAdapter.getItemIdStr(oldclickposition));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    jsonevents = tmparray;
                    UserInfo.events.set(tmparray.toString());
                    //mAdapter.remove(oldclickposition);

                    mAdapter.clear();
                    appointments.clear();
                    try {
                        jsonevents = new JSONArray(UserInfo.events.get());
                        for(int i = 0; i < jsonevents.length(); ++i) {
                            AppointmentModel apm = new AppointmentModel();
                            JSONObject obj_tmp = jsonevents.getJSONObject(i);
                            apm.setId(obj_tmp.getString("id"));
                            apm.setDescription(obj_tmp.getString("description"));
                            apm.setAuthor_id(obj_tmp.getString("author"));
                            apm.setClient_id(obj_tmp.getString("client"));
                            apm.setType(obj_tmp.getString("type"));
                            apm.setStart_date(obj_tmp.getString("start_date"));
                            apm.setEnd_date(obj_tmp.getString("end_date"));
                            apm.setLocation(obj_tmp.getString("location"));
                            apm.setDuration(obj_tmp.getString("duration"));
                            apm.setLink(obj_tmp.getString("link"));
                            apm.setIs_validate(obj_tmp.getBoolean("is_validate"));
                            appointments.add(apm);
                        }
                        Log.d(TAG, jsonevents.length() + " " + jsonevents.toString());
                        setAppointment();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, UserInfo.events.get());
                    //TODO remove date if only one event
                }
                if (response.equals("-1")) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Une erreur s'est produite, veuillez essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }
        }
    };

}
