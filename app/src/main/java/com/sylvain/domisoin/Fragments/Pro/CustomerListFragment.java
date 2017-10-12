package com.sylvain.domisoin.Fragments.Pro;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class CustomerListFragment extends Fragment {
    private static final String TAG = CustomerListFragment.class.getSimpleName();
    private View ourView = null;
    private HomeProActivity ourActivity = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_CUSTOMER_LIST_FRAG";
    public ProgressDialog progress = null;
    private ListView listView_customers = null;

    public CustomerListFragment() {
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

        ourView = inflater.inflate(R.layout.fragment_customer_list, container, false);

        ourActivity = (HomeProActivity)getActivity();

        List<String> clients_id = new LinkedList<String>();

        try {
            JSONArray events = new JSONArray(ourActivity.UserInfo.events.get());
            for (int i = 0; i < events.length(); ++i) {
                JSONObject objtmp = events.getJSONObject(i);
                JSONObject obj2 = new JSONObject(objtmp.get("author").toString());
                clients_id.add(obj2.get("first_name") + " " + obj2.get("last_name") + " - " + obj2.get("id").toString());
                Log.d("GET ID Cust list Frag", obj2.get("id").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //getClientFromID(clients_id.get(0));

        listView_customers = (ListView)ourView.findViewById(R.id.listView_Customer);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, clients_id);
        listView_customers.setAdapter(adapter);

        //Log.d("CustomerList Frag", ourActivity.UserInfo.events.get());

        return ourView;
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

    public void getClientFromID(String id) {
        HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_url) + "users/?id="+id);
        task.execute();
        progress = ProgressDialog.show
                (getActivity(), "Actualisation", "Mise à jour de la liste des clients en cours, merci de patienter...", true);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            Log.i(TAG, "RESPONSE = " + response);
            if (response != null) {
                if (response.length() == 3) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Une erreur s'est produite, veuillez essayer de nouveau. (" + response + ")", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    //setItemListView(response);
                    Log.d("test", response);
                }
            }
        }
    };

}
