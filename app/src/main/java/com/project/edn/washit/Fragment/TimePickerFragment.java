package com.project.edn.washit.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;
import android.app.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by EDN on 11/27/2016.
 */

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Calendar mcurrentTime;

    @SuppressLint("ValidFragment")
    public TimePickerFragment(EditText editText) {
        this.editText=editText;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog =new TimePickerDialog(getActivity(),this,hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        return timePickerDialog;
    }

    @Override

    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        editText.setText( i + ":" + i1);

    }
}
