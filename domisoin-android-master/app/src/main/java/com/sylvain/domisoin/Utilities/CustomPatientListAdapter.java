package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sylvain.domisoin.Models.UserModel;
import com.sylvain.domisoin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPatientListAdapter extends BaseAdapter implements Filterable {

    private ArrayList<UserModel> mData = new ArrayList<UserModel>();

    private LayoutInflater mInflater;
    private com.sylvain.domisoin.Utilities.CustomPatientListAdapter.ClientFilter clientFilter;

    public CustomPatientListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final UserModel user) {
        mData.add(user);
        notifyDataSetChanged();
    }

    public void setList(List<UserModel> _list) {
        mData.clear();
        mData.addAll(_list);
        notifyDataSetChanged();
        getFilter();
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
        com.sylvain.domisoin.Utilities.CustomProListAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new com.sylvain.domisoin.Utilities.CustomProListAdapter.ViewHolder();

            convertView = mInflater.inflate(R.layout.itemlist_patient, null);
            convertView.setClickable(false);
            holder.textView_name = (TextView) convertView.findViewById(R.id.item_patient_name);
            holder.textView_job = (TextView) convertView.findViewById(R.id.item_patient_job);

            convertView.setTag(holder);
        } else {
            holder = (com.sylvain.domisoin.Utilities.CustomProListAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView_name.setText(mData.get(position).getFirst_name() + " " + mData.get(position).getLast_name());
        holder.textView_job.setText(mData.get(position).getJob_title());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (clientFilter == null) {
            clientFilter = new com.sylvain.domisoin.Utilities.CustomPatientListAdapter.ClientFilter();
        }
        return clientFilter;
    }

    public static class ViewHolder {
        public TextView textView_name;
        public TextView textView_job;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class ClientFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<UserModel> tempList = new ArrayList<UserModel>();

                // search content in friend list
                for (UserModel user : mData) {
                    if (user.getFirst_name().toLowerCase().contains(constraint.toString().toLowerCase()) || user.getLast_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(user);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mData.size();
                filterResults.values = mData;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<UserModel>) results.values;
            notifyDataSetChanged();
        }
    }


}
