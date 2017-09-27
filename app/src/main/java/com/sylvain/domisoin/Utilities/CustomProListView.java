package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Sylvain on 04/07/2017.
 */

public class CustomProListView extends BaseAdapter {

    private ArrayList<UserModel> mData = new ArrayList<UserModel>();

    private LayoutInflater mInflater;

    public CustomProListView(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final UserModel user) {
        mData.add(user);
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public UserModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomProListView.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new CustomProListView.ViewHolder();

            convertView = mInflater.inflate(R.layout.itemlist_pro, null);
            convertView.setClickable(false);
            holder.textView_name = (TextView) convertView.findViewById(R.id.item_pro_name);
            holder.textView_job = (TextView) convertView.findViewById(R.id.item_pro_job);

            convertView.setTag(holder);
        } else {
            holder = (CustomProListView.ViewHolder) convertView.getTag();
        }
        holder.textView_name.setText(mData.get(position).getFirst_name() + " " + mData.get(position).getLast_name());
        holder.textView_job.setText(mData.get(position).getJob_title());

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView_name;
        public TextView textView_job;
    }

}
