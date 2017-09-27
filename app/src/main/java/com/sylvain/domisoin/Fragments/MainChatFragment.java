package com.sylvain.domisoin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.sylvain.domisoin.Models.MessageModel;
import com.sylvain.domisoin.R;
import com.sylvain.domisoin.Utilities.MessageListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainChatFragment extends Fragment implements View.OnClickListener {

    private ArrayList<MessageModel> dataModels;
    private ListView messages_listview = null;
    private MessageListView adapter = null;

    public MainChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainChatFragment = inflater.inflate(R.layout.fragment_mainchat, container, false);
        messages_listview = (ListView)mainChatFragment.findViewById(R.id.message_list);

        dataModels= new ArrayList<>();

        dataModels.add(new MessageModel("Apple Pie", "Android 1.0", "Android 1.0"));
        dataModels.add(new MessageModel("Banana Bread", "Android 1.1", "2"));
        dataModels.add(new MessageModel("Cupcake", "Android 1.5", "3"));
        dataModels.add(new MessageModel("Donut","Android 1.6","4"));
        dataModels.add(new MessageModel("Eclair", "Android 2.0", "5"));
        dataModels.add(new MessageModel("Froyo", "Android 2.2", "8"));
        dataModels.add(new MessageModel("Gingerbread", "Android 2.3", "9"));
        dataModels.add(new MessageModel("Honeycomb","Android 3.0","11"));
        dataModels.add(new MessageModel("Ice Cream Sandwich", "Android 4.0", "14"));
        dataModels.add(new MessageModel("Jelly Bean", "Android 4.2", "16"));
        dataModels.add(new MessageModel("Kitkat", "Android 4.4", "19"));
        dataModels.add(new MessageModel("Lollipop","Android 5.0","21"));
        dataModels.add(new MessageModel("Marshmallow", "Android 6.0", "23"));

        adapter= new MessageListView(dataModels, getContext());

        messages_listview.setAdapter(adapter);

        return mainChatFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }
}
