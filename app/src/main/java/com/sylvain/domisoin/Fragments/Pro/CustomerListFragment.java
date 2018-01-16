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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.UserInfo;
import com.sylvain.domisoin.Activities.HomeProActivity;
import com.sylvain.domisoin.Dialogs.CustomerMore;
import com.sylvain.domisoin.Dialogs.ProMore;
import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.CustomProListAdapter;
import com.sylvain.domisoin.Utilities.HTTPDeleteRequest;
import com.sylvain.domisoin.Utilities.HTTPGetRequest;
import com.sylvain.domisoin.Utilities.HTTPPostRequest;
import com.sylvain.domisoin.Utilities.HTTPPutRequest;
import com.sylvain.domisoin.Utilities.ManageErrorText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class CustomerListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = CustomerListFragment.class.getSimpleName();
    private View ourView = null;
    private HomeProActivity ourActivity = null;
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_CUSTOMER_LIST_FRAG";
    public ProgressDialog progress = null;
    private ListView listView_customers = null;
    private List<String> myClientsListId = null;
    private List<UserModel> myClientsList = null;
    private CustomProListAdapter adapter = null;
    private ImageButton search_button = null;
    private EditText search_text = null;

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

        listView_customers = (ListView)ourView.findViewById(R.id.listView_Customer);
        search_button = (ImageButton)ourView.findViewById(R.id.validate_search_button_pro);
        search_text = (EditText)ourView.findViewById(R.id.search_edittext_pro);

        search_button.setOnClickListener(this);

        //List<String> clients_id = new LinkedList<String>();

        myClientsListId = new LinkedList<String>();
        myClientsList = new LinkedList<UserModel>();

        getClientsListId();
        getClientsListFromAPI();

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

    private void getClientsListId() {
        try {
            JSONArray events = new JSONArray(ourActivity.UserInfo.events.get());
            Log.d("EVENTS HERE", ourActivity.UserInfo.events.get());
            for (int i = 0; i < events.length(); ++i) {
                JSONObject objtmp = events.getJSONObject(i);
                JSONObject obj2 = new JSONObject(objtmp.get("author").toString());
                myClientsListId.add(obj2.get("id").toString());
                //Log.d("GET ID Cust list Frag", String.valueOf(obj2.get("first_name")) + " " + String.valueOf(obj2.get("last_name")) + " " + String.valueOf(obj2.get("id")));
            }

            //Remove duplicates
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(myClientsListId);
            myClientsListId.clear();
            myClientsListId.addAll(hashSet);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getClientsListFromAPI() {
        HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_users_url) + "?is_pro=false", ourActivity.UserInfo.token.get());
        //HTTPGetRequest task = new HTTPGetRequest(getActivity(), ACTION_FOR_INTENT_CALLBACK, getString(R.string.api_base_url) + "patients/", ourActivity.UserInfo.token.get());
        task.execute();
        progress = ProgressDialog.show
                (getActivity(), "Actualisation", "Mise à jour de la liste des clients en cours, merci de patienter...", true);
    }

    private void getMyClients(String usersjson) {
        try {
            JSONArray clients = new JSONArray(usersjson);
            for (int i = 0; i < clients.length(); ++i) {
                JSONObject objtmp = clients.getJSONObject(i);
                for (int j = 0; j < myClientsListId.size(); ++j) {
                    if (String.valueOf(objtmp.get("id")).equals(String.valueOf(myClientsListId.get(j)))) {
                        Log.d("My client", String.valueOf(objtmp.get("first_name")) + " " + String.valueOf(objtmp.get("last_name")) + " - " + String.valueOf(objtmp.get("id")));
                        myClientsList.add(createUserFromJson(objtmp));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListClients();
    }

    private UserModel createUserFromJson(JSONObject obj) {
        UserModel user = new UserModel();

        try {
            user.setId(obj.getString("id"));
            user.setEmail(obj.getString("email"));
            user.setFirst_name(obj.getString("first_name"));
            user.setLast_name(obj.getString("last_name"));
            user.setJob_title(obj.getString("job_title"));
            user.setAddress(obj.getString("adresse"));
            user.setWorkphone(obj.getString("workphone"));
            user.setProfile_img(obj.getString("profile_img"));
            user.setIs_pro(obj.getBoolean("is_pro"));
            user.setEvents(obj.getString("events"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void setListClients() {
        adapter = new CustomProListAdapter(getContext());
        adapter.setList(myClientsList);
        listView_customers.setAdapter(adapter);
        listView_customers.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, myClientsList.get(position).getEmail());
        CustomerMore dialog = new CustomerMore();
        dialog.set_user(myClientsList.get(position));
        dialog.show(getFragmentManager(), "more");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.validate_search_button_pro:
                if (search_text.getText().length()<=0) {
                    adapter.setList(myClientsList);
                } else {
                    adapter.setList(myClientsList);
                    adapter.getFilter().filter(search_text.getText());
                }
                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != null) {
                progress.dismiss();
            }
            String response = intent.getStringExtra(HTTPGetRequest.HTTP_RESPONSE);
            if (response == null) {
                response = intent.getStringExtra(HTTPPostRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPPutRequest.HTTP_RESPONSE);
            } if (response == null) {
                response = intent.getStringExtra(HTTPDeleteRequest.HTTP_RESPONSE);
            }
            Log.i(TAG, "RESPONSE = " + response);
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
                } else {
                    getMyClients(response);
                }
            }
        }
    };
}
