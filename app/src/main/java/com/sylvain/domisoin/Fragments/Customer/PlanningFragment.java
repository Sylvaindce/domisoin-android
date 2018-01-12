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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sylvain.domisoin.Dialogs.AppointmentMore;
import com.sylvain.domisoin.Models.AppointmentModel;
import com.sylvain.domisoin.Utilities.CustomPlanningExpandableListAdapter;
import com.sylvain.domisoin.Activities.HomeCustomerActivity;
import com.sylvain.domisoin.DataBind.userInfo;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.databinding.FragmentPlanningBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class PlanningFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    private static final String TAG = PlanningFragment.class.getSimpleName();
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_PLANNING";

    private FragmentPlanningBinding fragmentPlanningBinding = null;
    private HomeCustomerActivity homeActivity = null;
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

        nordv = (TextView)fragmentPlanningBinding.getRoot().findViewById(R.id.nordv);

        swipeContainer = (SwipeRefreshLayout) fragmentPlanningBinding.getRoot().findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                Log.d(TAG, "Refresh moi");
                HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url)+UserInfo.id.get()+"/", UserInfo.token.get());
                task.execute();
            }
        });


        exp_planning = (ExpandableListView)fragmentPlanningBinding.getRoot().findViewById(R.id.exp_planning);
        exp_planning.setOnChildClickListener(this);

        not_validate_container = (LinearLayout)fragmentPlanningBinding.getRoot().findViewById(R.id.not_validate_container_cust);
        not_validate_number = (TextView)fragmentPlanningBinding.getRoot().findViewById(R.id.not_validate_number_cust);

        setPlanningMap();

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
                //mAdapter.clear();
                setPlanningMap();
            }
        }
        else {
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        AppointmentModel sapt = (AppointmentModel) listAdapterExp.getChild(groupPosition, childPosition);
        AppointmentMore dialog = new AppointmentMore(ACTION_FOR_INTENT_CALLBACK, UserInfo.token.get());
        oldClickItem = sapt.getId();
        dialog.set_apt(sapt);
        dialog.show(getFragmentManager(), "more");
        return false;
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
        /*for (Object o : odatas.entrySet()) {
            Map.Entry entry_tmp = (Map.Entry) o;

            Log.d("DATE Planning MAP", String.valueOf(entry_tmp.getKey()));

            LinkedList<AppointmentModel> tmp_list = (LinkedList<AppointmentModel>) entry_tmp.getValue();
            for (int i = 0; i < tmp_list.size(); ++i) {
                AppointmentModel tmp_apt = tmp_list.get(i);
                Log.d("DETAILS Planning MAP", tmp_apt.getDescription());
            }
        }*/
    }

    private void setPlanningMap() {
        SimpleDateFormat justDate = new SimpleDateFormat("yyyyMMdd");
        Date olddate = new Date();
        LinkedList<AppointmentModel> apt_tmp = new LinkedList<>();
        TreeMap<Date, LinkedList<AppointmentModel>> organized_data = new TreeMap<>();

        number_validate_rdv = 0;
        not_validate_container.setVisibility(View.GONE);

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
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            swipeContainer.setRefreshing(false);
            String response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            }
            Log.i(TAG, "Planning Fragment RESPONSE = " + response);
            if (response != null) {

                String response_code = "-1";
                if (response.contains(" - ")) {
                    response_code = response.split(" - ")[0];
                    try {
                        response = response.split(" - ")[1];
                    } catch(ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, response);
                    }
                }
                if (response.equals("0")) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Erreur de connexion au serveur, veuillez verifier votre connexion internet et essayer plus tard.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                else if (Integer.decode(response_code) > 226) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Une erreur s'est produite, veuillez essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
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
                    //mAdapter.remove(oldclickposition);

                    //mAdapter.clear();
                    //appointments.clear();

                    setPlanningMap();

                    Log.d(TAG, UserInfo.events.get());
                    //TODO remove date if only one event
                }
                else if (Integer.decode(response_code) == 200){
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

}
