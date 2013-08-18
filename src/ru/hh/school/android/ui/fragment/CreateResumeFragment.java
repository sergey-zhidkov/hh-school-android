package ru.hh.school.android.ui.fragment;


import java.util.Calendar;
import java.util.Date;

import ru.hh.school.R;
import ru.hh.school.android.Resume;
import ru.hh.school.android.ui.ViewResumeActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateResumeFragment extends Fragment implements OnClickListener {
    private static final int DEFAULT_DELTA_START_YEAR = 18;

    private Activity activity;
    private View currentView;

    private EditText lastFirstName;
    private Button btnChangeBirthday;
    private Date currentBirthday;
    private DatePicker dpBirthday;
    private Spinner gender;
    private EditText desiredJobTitle;
    private EditText salary;
    private EditText phone;
    private EditText email;

    private Button btnSendResume;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_resume_fragment, container, false);
        this.currentView = view;
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
    }

    private void initUi() {
        lastFirstName = (EditText) currentView.findViewById(R.id.et_last_first_name);
        btnChangeBirthday = (Button) currentView.findViewById(R.id.btn_change_birthday);
        dpBirthday = (DatePicker) currentView.findViewById(R.id.dp_birthday);
        gender = (Spinner) currentView.findViewById(R.id.sp_gender);
        desiredJobTitle = (EditText) currentView.findViewById(R.id.et_desired_job_title);
        salary = (EditText) currentView.findViewById(R.id.et_salary);
        phone = (EditText) currentView.findViewById(R.id.et_phone);
        email = (EditText) currentView.findViewById(R.id.et_email);

        btnSendResume = (Button) currentView.findViewById(R.id.btn_send_resume);
        btnSendResume.setOnClickListener(this);

        setInitialBirthday();
    }

    private void setInitialBirthday() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR) - DEFAULT_DELTA_START_YEAR;
        int month = 0; // January
        int day = 1;

        calendar.set(year, month, day);

        dpBirthday.init(year, month, day, null);

        java.text.DateFormat dateFormat = DateFormat.getDateFormat(activity);
        String dateText = dateFormat.format(calendar.getTime());
        String years = getResources().getString(R.string.years);
        dateText += " (" + getDiffYears(calendar, Calendar.getInstance()) + " " + years + ")";

        btnChangeBirthday.setText(dateText);
    }

    private int getDiffYears(Calendar first, Calendar last) {
        int diff = last.get(Calendar.YEAR) - first.get(Calendar.YEAR);
        if (first.get(Calendar.MONTH) > last.get(Calendar.MONTH)) {
            diff--;
        }
        if (first.get(Calendar.MONTH) == last.get(Calendar.MONTH)
                && first.get(Calendar.DAY_OF_MONTH) > last.get(Calendar.DAY_OF_MONTH)) {
            diff--;
        }

        return diff;
    }

    public void changeBirthday(View view) {
        // TODO: add datepicker dialog
        Toast.makeText(activity, "Change Birthday button clicked!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        Resume resume = new Resume();
        resume.setLastFirstName(lastFirstName.getText().toString().trim());
        resume.setBirthday(getDateFromDatePicker(dpBirthday));
        resume.setGender(gender.getSelectedItem().toString());
        resume.setDesiredJobTitle(desiredJobTitle.getText().toString().trim());
        resume.setSalary(salary.getText().toString());
        resume.setPhone(phone.getText().toString());
        resume.setEmail(email.getText().toString());

        if (resume.isFilledCorrectly()) {
            // send resume
            Intent intent = new Intent(activity, ViewResumeActivity.class);
            intent.putExtra(Resume.class.getCanonicalName(), resume);
            startActivity(intent);
        } else {
            Toast.makeText(activity, R.string.fill_name_and_position_message, Toast.LENGTH_LONG).show();
        }
    }

    private Date getDateFromDatePicker(DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
