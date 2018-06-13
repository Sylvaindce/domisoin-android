package com.sylvain.domisoin.Fragments.Pro;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.Dialogs.AppointmentMore;
import com.sylvain.domisoin.Dialogs.ShowWorkingHourFragment;
import com.sylvain.domisoin.Fragments.Customer.PlanningFragment;
import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomPlanningExpandableListAdapter;
import com.sylvain.domisoin.Utilities.CustomPlanningListView;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;
import com.sylvain.domisoin.databinding.FragmentPlanningProBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;


public class PlanningProFragment extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    private static final String TAG = PlanningProFragment.class.getSimpleName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING_PRO";

    private FragmentPlanningProBinding fragmentPlanningProBinding = null;
    private HomeProActivity homeActivity = null;
    private userInfo UserInfo = null;

    public  static ProgressDialog progress;

    private JSONArray jsonevents = null;

    private TextView nordv = null;
    private ExpandableListView exp_planning = null;
    private CustomPlanningExpandableListAdapter listAdapterExp = null;
    private String oldClickItem = "";
    private SwipeRefreshLayout swipeContainer;
    private TextView not_validate_number = null;
    private LinearLayout not_validate_container = null;
    private Integer number_validate_rdv = 0;
    private Button showWD = null;
    private RelativeLayout containerShowWD = null;

    public PlanningProFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentPlanningProBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_planning_pro, container, false);
        homeActivity = (HomeProActivity)getActivity();
        UserInfo = homeActivity.UserInfo;

        fragmentPlanningProBinding.setUser(UserInfo);

        nordv = (TextView)fragmentPlanningProBinding.getRoot().findViewById(R.id.nordv_pro);

        swipeContainer = (SwipeRefreshLayout) fragmentPlanningProBinding.getRoot().findViewById(R.id.swipeContainerPro);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+UserInfo.id.get()+"/", UserInfo.token.get());
                task.execute();
            }
        });

        exp_planning = (ExpandableListView)fragmentPlanningProBinding.getRoot().findViewById(R.id.planning_pro);
        exp_planning.setOnChildClickListener(this);

        not_validate_container = (LinearLayout)fragmentPlanningProBinding.getRoot().findViewById(R.id.not_validate_container_pro);
        not_validate_number = (TextView)fragmentPlanningProBinding.getRoot().findViewById(R.id.not_validate_number_pro);

        containerShowWD = (RelativeLayout)fragmentPlanningProBinding.getRoot().findViewById(R.id.button_wd_container);

        showWD = (Button) fragmentPlanningProBinding.getRoot().findViewById(R.id.showmywkh);
        showWD.setOnClickListener(this);
        //showWD.setStateListAnimator(null);

        setPlanningMap();

        return fragmentPlanningProBinding.getRoot();
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
                //mAdapter.clear();
                setPlanningMap();
            }
        }
        else {
        }
    }

    private void setAppointment(TreeMap<Date, LinkedList<AppointmentModel>> odatas) {

        nordv.setVisibility(View.VISIBLE);
        if (odatas.size()>0) {
            nordv.setVisibility(View.INVISIBLE);
        }
        //if (listAdapterExp == null) {
        listAdapterExp = new CustomPlanningExpandableListAdapter(getContext(), odatas);
        exp_planning.setAdapter(listAdapterExp);
        //} else {
        //    listAdapterExp.updateList(odatas);
        //}
        listAdapterExp.notifyDataSetChanged();

    }

    private void setPlanningMap() {
        SimpleDateFormat justDate = new SimpleDateFormat("yyyyMMdd");
        Date olddate = new Date();
        LinkedList<AppointmentModel> apt_tmp = new LinkedList<>();
        TreeMap<Date, LinkedList<AppointmentModel>> organized_data = new TreeMap<>();

        number_validate_rdv = 0;
        not_validate_container.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) containerShowWD.getLayoutParams();
        lp.setMargins(0,10,14,0);
        containerShowWD.setLayoutParams(lp);


        try {

            jsonevents = new JSONArray(UserInfo.events.get());

            for (int i = 0; i < jsonevents.length(); ++i) {

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
                apm.setIs_validate(obj_tmp.getBoolean("validate"));

                if (!apm.getIs_validate()) {
                    ++number_validate_rdv;
                }

                if (i == 0) {
                    olddate = apm.getStart_date();
                }
                if (justDate.format(olddate).equals(justDate.format(apm.getStart_date()))) {
                    apt_tmp.add(apm);
                } else {
                    organized_data.put(olddate, (LinkedList<AppointmentModel>) apt_tmp.clone());
                    apt_tmp.clear();
                    olddate = apm.getStart_date();
                    apt_tmp.add(apm);
                }
            }
            if (apt_tmp.size() > 0) {
                organized_data.put(olddate, apt_tmp);
            }
            Log.d(TAG, jsonevents.length() + " " + jsonevents.toString());

            setAppointment(organized_data);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        if (number_validate_rdv > 0) {
            not_validate_number.setText(String.valueOf(number_validate_rdv));
            not_validate_container.setVisibility(View.VISIBLE);
            lp = (RelativeLayout.LayoutParams) containerShowWD.getLayoutParams();
            lp.setMargins(0,44,14,0);
            containerShowWD.setLayoutParams(lp);
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            swipeContainer.setRefreshing(false);
            int status = -1;
            String response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            if(response == null) {
                response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
                status = 1;
            }
            if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
                status = 2;
            } if (response == null) {
                response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            }
            Log.i(TAG, "Planning Pro RESPONSE = " + response);
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
                }
                else if (Integer.decode(response_code) == 204) {
                    JSONArray tmparray = new JSONArray();
                    for (int i = 0; i < jsonevents.length(); ++i) {
                        try {
                            JSONObject tmp = jsonevents.getJSONObject(i);
                            if (!tmp.getString("id").equals(oldClickItem)) {
                                tmparray.put(tmp);
                            } else {
                                Log.i(TAG, tmp.getString("id") + " " + oldClickItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    jsonevents = tmparray;
                    UserInfo.events.set(tmparray.toString());

                    setPlanningMap();

                    Log.d(TAG, UserInfo.events.get());
                }
                else if (Integer.decode(response_code) == 200 && status == 1) {
                    JSONArray tmparray = new JSONArray();
                    for (int i = 0; i < jsonevents.length(); ++i) {
                        try {
                            JSONObject tmp = jsonevents.getJSONObject(i);
                            if (tmp.getString("id").equals(oldClickItem)) {
                                tmp.remove("validate");
                                tmp.put("validate", true);
                            }
                            tmparray.put(tmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonevents = tmparray;
                    UserInfo.events.set(tmparray.toString());

                    setPlanningMap();

                    Log.d(TAG, UserInfo.events.get());
                }
                else if (Integer.decode(response_code) == 200 && status == 2) {
                    try {
                        JSONObject tmp = new JSONObject(response);
                        UserInfo.events.set(tmp.getString("events"));

                        setPlanningMap();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        AppointmentModel sapt = (AppointmentModel) listAdapterExp.getChild(groupPosition, childPosition);
        AppointmentMore dialog = new AppointmentMore(ACTION_FOR_INTENT_CALLBACK, UserInfo.token.get());
        oldClickItem = sapt.getId();
        dialog.set_apt(sapt);
        dialog.show(getFragmentManager(), "more");
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showmywkh:
                ShowWorkingHourFragment dialog = new ShowWorkingHourFragment(UserInfo.calendar.get());
                dialog.show(getFragmentManager(), "ShowWK");
                break;
        }
    }
}
