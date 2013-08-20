package ru.hh.school.android.ui.fragment;


import java.util.Calendar;
import java.util.Date;

import ru.hh.school.R;
import ru.hh.school.android.DBHelper;
import ru.hh.school.android.Resume;
import ru.hh.school.android.ui.ViewResumeActivity;
import ru.hh.school.android.ui.dialog.DatePickerFragment;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
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
    private static final int GENDER_MALE_POSITION = 0;
    private static final int GENDER_FEMALE_POSITION = 1;

    private Activity activity;
    private View currentView;

    /** Currently edited resume */
    private Resume resume;

    private EditText lastFirstName;
    private Button btnChangeBirthday;
    private Spinner gender;
    private EditText desiredJobTitle;
    private EditText salary;
    private EditText phone;
    private EditText email;

    private Button btnSendResume;
    private Button btnSaveResume;

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
        fillForm();
    }

    private void initUi() {
        lastFirstName = (EditText) currentView.findViewById(R.id.et_last_first_name);
        btnChangeBirthday = (Button) currentView.findViewById(R.id.btn_change_birthday);
        gender = (Spinner) currentView.findViewById(R.id.sp_gender);
        desiredJobTitle = (EditText) currentView.findViewById(R.id.et_desired_job_title);
        salary = (EditText) currentView.findViewById(R.id.et_salary);
        phone = (EditText) currentView.findViewById(R.id.et_phone);
        email = (EditText) currentView.findViewById(R.id.et_email);

        btnSendResume = (Button) currentView.findViewById(R.id.btn_send_resume);
        btnSendResume.setOnClickListener(this);

        btnSaveResume = (Button) currentView.findViewById(R.id.btn_save_resume);
        btnSaveResume.setOnClickListener(this);
    }

    private void fillForm() {
        resume = (Resume) activity.getIntent().getParcelableExtra(Resume.class.getCanonicalName());

        lastFirstName.setText(resume.getLastFirstName());
        setBirthday(resume.getBirthday());
        setGender(resume.getGender());
        desiredJobTitle.setText(resume.getDesiredJobTitle());
        salary.setText(resume.getSalary());
        phone.setText(resume.getPhone());
        email.setText(resume.getEmail());
    }

    private void setGender(String genderString) {
        if (Resume.GENDER_MALE.equalsIgnoreCase(genderString)) {
            gender.setSelection(GENDER_MALE_POSITION);
        } else {
            gender.setSelection(GENDER_FEMALE_POSITION);
        }
    }

    /**
     * Returns gender from spinner depending on selected position ( 0 == male, 1 == female)
     *
     * @return String gender
     */
    private String getGender() {
        int genderIndex = gender.getSelectedItemPosition();
        if (genderIndex == 0) {
            return Resume.GENDER_MALE;
        } else {
            return Resume.GENDER_FEMALE;
        }
    }

    /**
     * Sets birthday text on the button.
     *
     * @param date
     */
    private void setBirthday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        setBirthday(year, month, day);
    }

    /**
     * Sets birthday text on the button.
     *
     * @param year
     * @param month
     * @param day
     */
    private void setBirthday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        resume.setBirthday(calendar.getTime());

        java.text.DateFormat dateFormat = DateFormat.getDateFormat(activity);
        String dateText = dateFormat.format(resume.getBirthday());
        String years = getResources().getString(R.string.years);

        // TODO: fix but with russian age words
        dateText += " (" + resume.getAgeYears() + " " + years + ")";

        btnChangeBirthday.setText(dateText);
    }

    /**
     * On click listener to change birthday.
     *
     * @param view
     */
    public void changeBirthday(View view) {
        showDatePickerDialog();
    }

    /**
     * Shows datePickerDialog.
     */
    private void showDatePickerDialog() {
        DatePickerFragment dpDialog = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putLong("birthday", resume.getBirthday().getTime());
        dpDialog.setArguments(args);
        dpDialog.setListener(onChangeBirthdayListener);
        dpDialog.show(getFragmentManager(), "Birthday picker");
    }

    /**
     * Listener to change birthday.
     */
    private final OnDateSetListener onChangeBirthdayListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setBirthday(year, monthOfYear, dayOfMonth);
        }
    };

    @Override
    public void onClick(View v) {
        resume.setLastFirstName(lastFirstName.getText().toString().trim());
        resume.setGender(getGender());
        resume.setDesiredJobTitle(desiredJobTitle.getText().toString().trim());
        resume.setSalary(salary.getText().toString());
        resume.setPhone(phone.getText().toString());
        resume.setEmail(email.getText().toString());

        switch (v.getId()) {
        case R.id.btn_send_resume:
            saveResume();
            sendResume();
            break;
        case R.id.btn_save_resume:
            saveResume();
            break;
        }
    }

    /**
     * Saves resume into DB, but don't send it to HR.
     */
    private void saveResume() {
        long rowId = resume.getId();
        new DBHelper(activity).updateResumeByRowId(rowId, resume);
    }

    /**
     * Sends resume to HR if it properly filled.
     */
    private void sendResume() {
        if (resume.isFilledCorrectly()) {
            // send resume
            Intent intent = new Intent(activity, ViewResumeActivity.class);
            intent.putExtra(Resume.class.getCanonicalName(), resume);
            startActivity(intent);
        } else {
            Toast.makeText(activity, R.string.fill_name_and_position_message, Toast.LENGTH_LONG).show();
        }
    }
}
