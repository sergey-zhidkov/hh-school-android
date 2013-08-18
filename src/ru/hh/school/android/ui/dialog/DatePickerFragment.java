package ru.hh.school.android.ui.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private OnDateSetListener listener;
    private Calendar calendar;

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void setListener(OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        long birthday = args.getLong("birthday");
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday);
    }
}
