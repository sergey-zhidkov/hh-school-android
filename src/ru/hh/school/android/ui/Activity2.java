package ru.hh.school.android.ui;

import ru.hh.school.android.Resume;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hh_school_android.R;

public class Activity2 extends Activity implements OnClickListener {

    private TextView resumeContent;
    private EditText answer;
    private Button btnSendAnswer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_linear);

		init();
	}

    private void init() {
        resumeContent = (TextView) findViewById(R.id.tv_resume_content);
        answer = (EditText) findViewById(R.id.et_answer);
        btnSendAnswer = (Button) findViewById(R.id.btn_send_answer);
        btnSendAnswer.setOnClickListener(this);

        setResumeContent();
    }

    private void setResumeContent() {
        Resume resume = (Resume) getIntent().getParcelableExtra(Resume.class.getCanonicalName());

        Resources res = getResources();
        String lastFirstNameString = res.getString(R.string.last_first_name);
        String birthdayString = res.getString(R.string.birthday);
        String genderString = res.getString(R.string.gender);
        String desiredJobTitleString = res.getString(R.string.desired_job_title);
        String salaryString = res.getString(R.string.salary);
        String phoneString = res.getString(R.string.phone);
        String emailString = res.getString(R.string.email);

        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(lastFirstNameString).append(":").append("</b> ").append(resume.getLastFirstName()).append("<br/>");
        sb.append("<b>").append(birthdayString).append(":").append("</b> ").append(resume.getFormattedBirthday()).append("<br/>");
        sb.append("<b>").append(genderString).append(":").append("</b> ").append(resume.getGender()).append("<br/>");
        sb.append("<b>").append(desiredJobTitleString).append(":").append("</b> ").append(resume.getDesiredJobTitle()).append("<br/>");
        sb.append("<b>").append(salaryString).append(":").append("</b> ").append(resume.getSalary()).append("<br/>");
        sb.append("<b>").append(phoneString).append(":").append("</b> ").append(resume.getPhone()).append("<br/>");
        sb.append("<b>").append(emailString).append(":").append("</b> ").append(resume.getEmail()).append("<br/>");

        resumeContent.setText(Html.fromHtml(sb.toString()));
    }

    @Override
    public void onClick(View v) {
        String message = answer.getText().toString();
        message = message.trim();
        // TODO: send message to...
        Toast.makeText(this, R.string.on_send_answer_message, Toast.LENGTH_LONG).show();
    }

}
