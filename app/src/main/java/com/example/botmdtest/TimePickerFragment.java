package com.example.botmdtest;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener  {

    private Fragment fragment;
    public TimePickerFragment() {
        // Required empty public constructor
    }

    public TimePickerFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), TimePickerFragment.this , hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String result = hourOfDay+":"+minute;
        Log.v("Timing", result);

        //  TextView textView = (TextView) findViewById(R.id.textView);
        // textView.setText("Hour: " + hourOfDay + " Minute: " + minute);
        // send date back to the target fragment
        Intent newIntent = new Intent();

        String hr = (hourOfDay < 10) ? "0" + hourOfDay : hourOfDay+"";
        String min = (minute < 10) ? "0" + minute : minute + "";

        newIntent.putExtra("hour", hr);
        newIntent.putExtra("minute", min);
        newIntent.putExtra("selectedDate", result);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                newIntent
        );
    }

}
