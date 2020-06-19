package com.example.botmdtest;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {


    public TemperatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("Meowww", "Oncreate view");
        View rootView = inflater.inflate(R.layout.fragment_temperature, container);
        Button btn = rootView.findViewById(R.id.locationButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("meow", "meow");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("Meowww", "meow");
        selectLocation();
    }


    @Override
    public void onStart() {
        super.onStart();
        selectLocation();
    }

    private void selectLocation(){
        Log.v("Lel", "test");
        Button btn = getView().findViewById(R.id.locationButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMgr = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragMgr.beginTransaction();

                //TemperatureFragment myFragment = new TemperatureFragment(); //my custom fragment
                LocationFragment myFragment = new LocationFragment();
                fragTrans.add(R.id.fragmentContainer, myFragment);
                fragTrans.addToBackStack(null);
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTrans.commit();
            }
        });
    }
}
