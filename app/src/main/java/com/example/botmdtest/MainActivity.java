package com.example.botmdtest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //global data
    Bundle bundle;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText(R.string.home_title);
        bundle = new Bundle();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.v("homeee", "Home button is pressed");
                //NavUtils.navigateUpFromSameTask(this);
                //launch the temperature fragment
                FragmentManager fragMgr = getSupportFragmentManager();
                fragMgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragTrans = fragMgr.beginTransaction();
                TemperatureFragment temperatureFragment = new TemperatureFragment();
                //temperatureFragment.setArguments(bundle);
                temperatureFragment.setArguments(bundle);
                fragTrans.add(R.id.fragmentContainer, temperatureFragment);
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTrans.commit();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
