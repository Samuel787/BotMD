package com.example.botmdtest;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationManagerCompat;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.net.HttpURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {

    private int notificationHour;
    private int notificationMinutes;
    private final int NOTIFICATION_ID = 200;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private boolean notificationEnabled = false;
    public static final int REQUEST_CODE = 11; // Used to identify the result
    private final String PREFERENCE_FILE_KEY = "mdbotlocaldata";
    public TemperatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText(R.string.home_title);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);
        initializeTimeDialog(rootView);
        loadStoredData(rootView);
        updateNotificationTiming(rootView);
        createNotificationChannel();
        initializeReminderSwitch(rootView);
        initializeDataSubmit(rootView);
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

    /**
     * Load locally saved data of reminder time and reminder enabled
     */
    private void loadStoredData(View rootView){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        int hour = sharedPreferences.getInt("HOUR", 12);
        int minute = sharedPreferences.getInt("MINUTE", 0);
        int enabled = sharedPreferences.getInt("notificationEnabled", 0);
        notificationEnabled = (enabled == 1);
        notificationMinutes = minute;
        notificationHour = hour;
        String hr, min;
        hr = (hour < 10) ? "0"+hour : hour+"";
        min = (minute < 10) ? "0" + minute : minute + "";
        ((TextView)rootView.findViewById(R.id.reminderButton1)).setText("Daily notification at: "+hr+""+min+" hrs");
        if(enabled == 0){
            ((Switch)rootView.findViewById(R.id.reminderSwitch)).setChecked(false);
        } else {
            ((Switch)rootView.findViewById(R.id.reminderSwitch)).setChecked(true);
        }
    }

    private void initializeReminderSwitch(View rootView){
        Switch mSwitch = rootView.findViewById(R.id.reminderSwitch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cancelNotifications(NOTIFICATION_ID);
                    updateNotificationTiming(getView());
                    Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ReminderBroadCast.class);
                    pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                    alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
                    calendar.set(Calendar.MINUTE, notificationMinutes);
                    calendar.set(Calendar.SECOND, 0);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notificationEnabled", 1);
                    editor.commit();
                } else {
                    //cancel notifications
                    Toast.makeText(getContext(), "Reminder switched off", Toast.LENGTH_SHORT).show();
                    cancelNotifications(NOTIFICATION_ID);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notificationEnabled", 0);
                    editor.commit();

                }
            }
        });
    }

    private void initializeDataSubmit(final View rootView){
        Button submitBtn  = rootView.findViewById(R.id.dataSubmitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check that all the fields are set
                String userId = "", location = "";
                double temperature = -888;
                try{
                    userId = ((EditText)rootView.findViewById(R.id.userID_holder)).getText().toString();
                    temperature = Double.parseDouble(((EditText)rootView.findViewById(R.id.temperature_holder)).getText().toString());
                    location = ((Button) rootView.findViewById(R.id.locationButton)).getText().toString();

                    if(!userId.equals("") && !location.equals("") && temperature != -888 && !location.equals("Location")){
                        String json = formatDataAsJSON(userId, temperature, location);
                        sendDataToServer(json, rootView);
                    } else {
                        Toast.makeText(getContext(), "Form is not filled properly", Toast.LENGTH_SHORT).show();
                    }

                } catch(NumberFormatException e){
                    Toast.makeText(getContext(), "Temperature field is not filled properly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancelNotifications(int id){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.cancelAll();
        if(alarmManager != null)
            alarmManager.cancel(pendingIntent);
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


    private String formatDataAsJSON(String userID, double temperature, String location){
        final JSONObject root = new JSONObject();
        try{
            root.put("token", "DcRmfWbX2HUCt68K4Nx14Q");
            JSONObject data = new JSONObject();
            data.put("id", userID);
            data.put("work_status", "occupied");
            data.put("location", location);
            data.put("temp", temperature);
            root.put("data", data);

            return root.toString();
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong. Data is not submitted", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void sendDataToServer(final String json, final View rootView){

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            String URL = "https://app.fakejson.com/q";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    if(response.equals("200")){
                        Toast.makeText(getContext(), "Data submitted Successfully", Toast.LENGTH_SHORT).show();
                        ((EditText)rootView.findViewById(R.id.userID_holder)).setText("");
                        ((EditText)rootView.findViewById(R.id.temperature_holder)).setText("");
                        ((Button)rootView.findViewById(R.id.locationButton)).setText("Location");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(getContext(), "Data could not be submitted. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
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
                FragmentManager fragMgr = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragMgr.beginTransaction();

                String userId = ((EditText)getView().findViewById(R.id.userID_holder)).getText().toString();
                String temperature = ((EditText)getView().findViewById(R.id.temperature_holder)).getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("user_id", userId);
                bundle.putString("temperature", temperature);

                ((MainActivity)getActivity()).bundle.putString("user_id", userId);
                ((MainActivity)getActivity()).bundle.putString("temperature", temperature);

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
        //updated in sharedpreference too
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("HOUR", this.notificationHour);
        editor.putInt("MINUTE", this.notificationMinutes);
        editor.commit();
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


