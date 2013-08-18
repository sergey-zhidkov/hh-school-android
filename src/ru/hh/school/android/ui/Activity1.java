package ru.hh.school.android.ui;

import java.util.Calendar;
import java.util.Date;

import ru.hh.school.android.Resume;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hh_school_android.R;

public class Activity1 extends Activity implements OnClickListener {

    private static final int DEFAULT_DELTA_START_YEAR = 18;

	private EditText lastFirstName;
	private DatePicker dpBirthday;
	private Spinner gender;
	private EditText desiredJobTitle;
	private EditText salary;
	private EditText phone;
	private EditText email;

	private Button btnSendResume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1_linear);

		init();
	}

	private void init() {
		lastFirstName = (EditText) findViewById(R.id.et_last_first_name);
		dpBirthday = (DatePicker) findViewById(R.id.dp_birthday);
		gender = (Spinner) findViewById(R.id.sp_gender);
		desiredJobTitle = (EditText) findViewById(R.id.et_desired_job_title);
		salary = (EditText) findViewById(R.id.et_salary);
		phone = (EditText) findViewById(R.id.et_phone);
		email = (EditText) findViewById(R.id.et_email);

		btnSendResume = (Button) findViewById(R.id.btn_send_resume);
		btnSendResume.setOnClickListener(this);

		setInitialBirthday();
	}

	private void setInitialBirthday() {
	    Calendar calendar = Calendar.getInstance();
	    int year = calendar.get(Calendar.YEAR) - DEFAULT_DELTA_START_YEAR;
	    int month = 0; // January
	    int day = 1;

	    dpBirthday.init(year, month, day, null);
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
            Intent intent = new Intent(this, Activity2.class);
            intent.putExtra(Resume.class.getCanonicalName(), resume);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.fill_name_and_position_message, Toast.LENGTH_LONG).show();
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
