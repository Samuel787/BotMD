package com.example.botmdtest;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    private ListView listView;
    private LocationAdapter mAdapter;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.locationsList);
        SearchView searchView = getView().findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        ArrayList<LocationItem> locations = new ArrayList<>();
        populateDataSet(locations);
        mAdapter = new LocationAdapter(getContext(), locations, getActivity(), getArguments());
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * Populates the listview with dummy dataset
     * @param locations arraylist of locations that listview adapter uses
     */
    private void populateDataSet(ArrayList<LocationItem> locations){
        locations.add(new LocationItem("AH"));
        locations.add(new LocationItem("AdMC"));
        locations.add(new LocationItem("AMK THKH"));
        locations.add(new LocationItem("AMK FMC"));
        locations.add(new LocationItem("Ang Mo Kio Polyclinic"));
        locations.add(new LocationItem("Bedok Polyclinic"));
        locations.add(new LocationItem("Bukit Batok Polyclinic"));
        locations.add(new LocationItem("Bukit Merah Polyclinic"));
        locations.add(new LocationItem("BV"));
        locations.add(new LocationItem("Bosnian"));
    }


}
