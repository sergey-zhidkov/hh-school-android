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
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    public static final int DEFAULT_DELTA_START_YEAR = 18;

    private static final int GENDER_MALE_POSITION = 0;
    private static final int GENDER_FEMALE_POSITION = 1;

    private Activity activity;
    private View currentView;

    private Resume resume;

    private EditText lastFirstName;
    private Button btnChangeBirthday;
    private Date currentBirthday;
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

    private void setBirthday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - DEFAULT_DELTA_START_YEAR;
        int month = 0; // January
        int day = 1;

        setBirthday(year, month, day);
    }

    private void setBirthday(Date date) {
        if (date == null) {
            setBirthday();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        setBirthday(year, month, day);
    }

    private void setBirthday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        currentBirthday = calendar.getTime();

        java.text.DateFormat dateFormat = DateFormat.getDateFormat(activity);
        String dateText = dateFormat.format(currentBirthday);
        String years = getResources().getString(R.string.years);
        dateText += " (" + getDiffYears(calendar, Calendar.getInstance()) + " " + years + ")";

        btnChangeBirthday.setText(dateText);
    }

    public int getDiffYears(Calendar first, Calendar last) {
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
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        DatePickerFragment dpDialog = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putLong("birthday", currentBirthday.getTime());
        dpDialog.setArguments(args);
        dpDialog.setListener(onChangeBirthdayListener);
        dpDialog.show(getFragmentManager(), "Birthday picker");
    }

    private final OnDateSetListener onChangeBirthdayListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setBirthday(year, monthOfYear, dayOfMonth);
        }
    };

    @Override
    public void onClick(View v) {
        resume.setLastFirstName(lastFirstName.getText().toString().trim());
        resume.setBirthday(currentBirthday);
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

    private void saveResume() {
        int rowId = resume.getId();
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_NAME, resume.getLastFirstName());
        cv.put(DBHelper.COL_BIRTHDAY, currentBirthday.getTime());
        cv.put(DBHelper.COL_GENDER, resume.getGender());
        cv.put(DBHelper.COL_POSITION, resume.getDesiredJobTitle());
        cv.put(DBHelper.COL_SALARY, resume.getSalary());
        cv.put(DBHelper.COL_PHONE, resume.getPhone());
        cv.put(DBHelper.COL_EMAIL, resume.getEmail());

        db.update(DBHelper.TABLE_RESUME, cv, "rowid=" + rowId, null);
        db.close();
    }

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
