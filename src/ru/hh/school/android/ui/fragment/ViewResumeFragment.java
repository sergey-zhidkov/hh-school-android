package ru.hh.school.android.ui.fragment;

import ru.hh.school.R;
import ru.hh.school.android.Resume;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewResumeFragment extends Fragment implements OnClickListener {

    private Activity activity;
    private View currentView;

    private TextView resumeContent;
    private TextView resumePhone;
    private TextView resumeEmail;
    private EditText answer;
    private Button btnSendAnswer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_resume_fragment, container, false);
        this.currentView = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
    }

    private void initUi() {
        resumeContent = (TextView) currentView.findViewById(R.id.tv_resume_content);
        resumePhone = (TextView) currentView.findViewById(R.id.tv_resume_phone);
        resumeEmail = (TextView) currentView.findViewById(R.id.tv_resume_email);
        answer = (EditText) currentView.findViewById(R.id.et_answer);
        btnSendAnswer = (Button) currentView.findViewById(R.id.btn_send_answer);
        btnSendAnswer.setOnClickListener(this);

        setResumeContent();
    }

    private void setResumeContent() {
        Resume resume = (Resume) activity.getIntent().getParcelableExtra(Resume.class.getCanonicalName());

        Resources res = getResources();
        String lastFirstNameString = res.getString(R.string.last_first_name);
        String birthdayString = res.getString(R.string.birthday);
        String genderString = res.getString(R.string.gender);
        String desiredJobTitleString = res.getString(R.string.desired_job_title);
        String salaryString = res.getString(R.string.salary);
        String phoneString = res.getString(R.string.phone);
        String emailString = res.getString(R.string.email);

        // set resume content
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(lastFirstNameString).append(":").append("</b> ").append(resume.getLastFirstName()).append("<br/>");
        sb.append("<b>").append(birthdayString).append(":").append("</b> ").append(resume.getFormattedBirthday(activity)).append("<br/>");
        sb.append("<b>").append(genderString).append(":").append("</b> ").append(resume.getGender()).append("<br/>");
        sb.append("<b>").append(desiredJobTitleString).append(":").append("</b> ").append(resume.getDesiredJobTitle()).append("<br/>");
        sb.append("<b>").append(salaryString).append(":").append("</b> ").append(resume.getSalary()).append("<br/>");
        resumeContent.setText(Html.fromHtml(sb.toString()));

        // set resume phone
        sb = new StringBuilder();
        sb.append("<b>").append(phoneString).append(":").append("</b> ").append(resume.getPhone()).append("<br/>");
        resumePhone.setText(Html.fromHtml(sb.toString()));

        // set resume email
        sb = new StringBuilder();
        sb.append("<b>").append(emailString).append(":").append("</b> ").append(resume.getEmail()).append("<br/>");
        resumeEmail.setText(Html.fromHtml(sb.toString()));
    }

    @Override
    public void onClick(View v) {
        String message = answer.getText().toString();
        message = message.trim();
        // TODO: send message to...
        Toast.makeText(activity, R.string.on_send_answer_message, Toast.LENGTH_LONG).show();
    }
}
