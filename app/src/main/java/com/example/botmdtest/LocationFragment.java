package com.example.botmdtest;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Search Locations");

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
        locations.add(new LocationItem("Alexandra Hill"));
        locations.add(new LocationItem("Aljunied"));
        locations.add(new LocationItem("Anak Bukit"));
        locations.add(new LocationItem("Anson"));
        locations.add(new LocationItem("Balestier"));
        locations.add(new LocationItem("Bayfront"));
        locations.add(new LocationItem("Bendemeer"));
        locations.add(new LocationItem("Boon Keng"));
        locations.add(new LocationItem("Boulevard"));
        locations.add(new LocationItem("Bras Basah"));
        locations.add(new LocationItem("Bugis"));
        locations.add(new LocationItem("Bukit Ho Swee"));
        locations.add(new LocationItem("Cairnhill"));
        locations.add(new LocationItem("Cecil"));
        locations.add(new LocationItem("Central"));
        locations.add(new LocationItem("China Square"));
        locations.add(new LocationItem("City Hall"));
        locations.add(new LocationItem("Clifford Pier"));
        locations.add(new LocationItem("Crawford"));
        locations.add(new LocationItem("Depot Road"));
        locations.add(new LocationItem("Dhoby Ghaut"));
        locations.add(new LocationItem("Dunearn"));
        locations.add(new LocationItem("East Coast"));
        locations.add(new LocationItem("Everton Park"));
        locations.add(new LocationItem("Farrer Court"));
        locations.add(new LocationItem("Fort Canning"));
        locations.add(new LocationItem("Geylang Bahru"));
        locations.add(new LocationItem("Geylang East"));
        locations.add(new LocationItem("Goodwood Park"));
        locations.add(new LocationItem("Henderson Hill"));
        locations.add(new LocationItem("Hillcrest"));
        locations.add(new LocationItem("Holland Road"));
        locations.add(new LocationItem("Istana Negara"));
        locations.add(new LocationItem("Kallang Bahru"));
        locations.add(new LocationItem("Kallang Way"));
        locations.add(new LocationItem("Kampong Bugis"));
        locations.add(new LocationItem("Kampong Java"));
        locations.add(new LocationItem("Kampong Ubi"));
        locations.add(new LocationItem("Katong"));
        locations.add(new LocationItem("Lavender"));
        locations.add(new LocationItem("Leedon Park"));
        locations.add(new LocationItem("MacPherson"));
        locations.add(new LocationItem("Malcolm"));
        locations.add(new LocationItem("Marina Centre"));
        locations.add(new LocationItem("Marymount"));
        locations.add(new LocationItem("Maxwell"));
        locations.add(new LocationItem("Monk's Hill"));
        locations.add(new LocationItem("Moulmein"));
        locations.add(new LocationItem("Mount Pleasant"));
        locations.add(new LocationItem("Mountbatten"));
        locations.add(new LocationItem("Newton Circus"));
        locations.add(new LocationItem("Orange Grove"));
        locations.add(new LocationItem("Phillip"));
        locations.add(new LocationItem("Raffles Place"));
        locations.add(new LocationItem("Redhill"));
        locations.add(new LocationItem("Somerset"));
        locations.add(new LocationItem("Swiss Club"));
        locations.add(new LocationItem("Tanjong Pagar"));
        locations.add(new LocationItem("Tanjong Rhu"));
        locations.add(new LocationItem("Tiong Bahru"));
        locations.add(new LocationItem("Ulu Pandan"));
        locations.add(new LocationItem("Upper Thomson"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.v("homeee", "Home button is pressed");
                //NavUtils.navigateUpFromSameTask(this);
                //launch the temperature fragment
                FragmentManager fragMgr = getActivity().getSupportFragmentManager();
                fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragTrans = fragMgr.beginTransaction();
                TemperatureFragment temperatureFragment = new TemperatureFragment();
                //temperatureFragment.setArguments(bundle);
                fragTrans.add(R.id.fragmentContainer, temperatureFragment);
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTrans.commit();
                //NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
