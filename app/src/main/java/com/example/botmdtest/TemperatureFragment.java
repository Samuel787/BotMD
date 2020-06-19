package com.example.botmdtest;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {

    private int notificationHour;
    private int notificationMinutes;

    public static final int REQUEST_CODE = 11; // Used to identify the result
    public TemperatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);
        initializeTimeDialog(rootView);
        updateNotificationTiming(rootView);
        createNotificationChannel();
        initializeReminderSwitch(rootView);
        return rootView;
    }

    /**
     * Displays the time picker dialog when reminder is pressed
     * @param rootView the inflated view of this fragment
     */
    public void initializeTimeDialog(View rootView){
        Button reminderBtn = rootView.findViewById(R.id.reminderButton);
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(TemperatureFragment.this, REQUEST_CODE);
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
            }
        });
        TextView reminderBtn1 = rootView.findViewById(R.id.reminderButton1);
        reminderBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(TemperatureFragment.this, REQUEST_CODE);
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
            }
        });
    }

    private void initializeReminderSwitch(View rootView){
        Switch mSwitch = rootView.findViewById(R.id.reminderSwitch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    updateNotificationTiming(getView());
                    Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ReminderBroadCast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    long timeAtButtonClick = System.currentTimeMillis();
                    long tenSecondsInMillis = 1000*5;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent);
                } else {
                    //cancel notifications

                }
            }
        });
    }

    /**
     * Updates the daily notification timing
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
           ((TextView)getView().findViewById(R.id.reminderButton1))
                   .setText("Daily notification at: "+data.getStringExtra("hour") +""+data.getStringExtra("minute")+" hrs");
           updateNotificationTiming(getView());
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectLocation();
        loadFormValues();

    }

    /**
     * Preserves values user input into the form if user switches fragments
     */
    private void loadFormValues(){
        if(getArguments() != null && getArguments().getString("user_id") != null){
            ((EditText)getView().findViewById(R.id.userID_holder)).setText(getArguments().getString("user_id"));
        }
        if(getArguments() != null && getArguments().getString("temperature") != null){
            ((EditText)getView().findViewById(R.id.temperature_holder)).setText(getArguments().getString("temperature"));
        }
        if(getArguments() != null && getArguments().getString("location") != null){
            ((Button)getView().findViewById(R.id.locationButton)).setText(getArguments().getString("location"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        selectLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((MainActivity)getActivity()).location != null){
            ((Button)getView().findViewById(R.id.locationButton)).setText(((MainActivity)getActivity()).location);
        }
        Log.v("Result", ""+((MainActivity)getActivity()).location);
    }

    private void selectLocation(){
        Button btn = getView().findViewById(R.id.locationButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hideSoftKeyboard(getActivity(), view);

                FragmentManager fragMgr = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragMgr.beginTransaction();

                String userId = ((EditText)getView().findViewById(R.id.userID_holder)).getText().toString();
                String temperature = ((EditText)getView().findViewById(R.id.temperature_holder)).getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("user_id", userId);
                bundle.putString("temperature", temperature);

                //TemperatureFragment myFragment = new TemperatureFragment(); //my custom fragment
                LocationFragment myFragment = new LocationFragment();
                myFragment.setArguments(bundle);
                fragTrans.replace(R.id.fragmentContainer, myFragment);
                fragTrans.addToBackStack(null);
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTrans.commit();
            }
        });
    }

    /**
     * Removes focus from any edit text that's focused
     * @param activity active activity
     * @param view view of button that's clicked
     */
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /**
     * Parses the timing from textview and updates notificationHours and notificationMinutes
     */
    private void updateNotificationTiming(View view){
        String stringTiming = ((TextView)view.findViewById(R.id.reminderButton1)).getText().toString().split(":")[1].split(" ")[1];
        Log.v("Timing", "Timing from parsing: "+stringTiming);
        this.notificationHour = Integer.parseInt(stringTiming.substring(0, 2));
        this.notificationMinutes = Integer.parseInt(stringTiming.substring(2));
    }


    /**
     * Creating a notification channel
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "mdBotReminderChannel";
            String description = "Channel for MD Bot";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyTemperature", name, importance);
            channel.setDescription(description);


            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
