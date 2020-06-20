package com.example.botmdtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<LocationItem> implements Filterable {

    private Context mContext;
    private List<LocationItem> locationsList = new ArrayList<>();
    private List<LocationItem> locationsListFull = new ArrayList<>();
    private FragmentActivity mActivity;
    private Bundle bundle;

    public LocationAdapter(@NonNull Context context, ArrayList<LocationItem> list, FragmentActivity activeActivity, Bundle bundle){
        super(context, 0, list);
        mContext = context;
        locationsList = list;
        locationsListFull = new ArrayList<>(list);
        this.mActivity = activeActivity;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.location_item, parent, false);
        }

        final LocationItem currentLocation  = locationsList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.locationName);
        name.setText(currentLocation.getLocationName());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mActivity).location = currentLocation.getLocationName();
                bundle.putString("location", ((MainActivity)mActivity).location);
                FragmentManager fragMgr = mActivity.getSupportFragmentManager();
                fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragTrans = fragMgr.beginTransaction();
                TemperatureFragment temperatureFragment = new TemperatureFragment();
                temperatureFragment.setArguments(bundle);
                fragTrans.add(R.id.fragmentContainer, temperatureFragment);
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTrans.commit();
            }
        });
        return listItem;
        //return super.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        //return super.getCount();
        return locationsList.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        //return super.getFilter();
        return mFilter;
    }

    private Filter mFilter  = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<LocationItem> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(locationsListFull);
            } else {
                String filteredPattern = charSequence.toString().toLowerCase().trim();
                Log.v("filtering", filteredPattern);
                for(LocationItem item : locationsListFull){
                    if(item.getLocationName().toLowerCase().trim().startsWith(filteredPattern)){
                        filteredList.add(item);
                        Log.v("filtering", "added item "+item.getLocationName());
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            locationsList.clear();
            locationsList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}

