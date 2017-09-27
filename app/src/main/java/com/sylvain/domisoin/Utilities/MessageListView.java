package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sylvain.domisoin.Models.MessageModel;
import com.sylvain.domisoin.R;

import java.util.ArrayList;


public class MessageListView extends ArrayAdapter<MessageModel> implements View.OnClickListener {

    private ArrayList<MessageModel> dataSet = null;
    private Context mContext = null;
    private int lastPosition = -1;


    // View lookup cache
    private static class ViewHolder {
        TextView from;
        TextView to;
        TextView message;
        ImageView picture;
    }

    public MessageListView(ArrayList<MessageModel> data, Context context) {
        super(context, R.layout.message_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

            /*int position=(Integer) v.getTag();
            Object object= getItem(position);
            MessageModel dataModel=(MessageModel)object;

            switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }*/
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageModel dataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.message_item_layout, parent, false);
            viewHolder.from = (TextView) convertView.findViewById(R.id.message_list_name);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message_list_message);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.from.setText(dataModel.getFrom());
        viewHolder.message.setText(dataModel.getMessage());
        return result;
    }
}
