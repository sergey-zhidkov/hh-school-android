package ru.hh.school.android.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import ru.hh.school.R;
import ru.hh.school.android.DBHelper;
import ru.hh.school.android.Resume;
import ru.hh.school.android.ui.CreateResumeActivity;
import ru.hh.school.android.ui.ViewResumeActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ResumesManagerFragment extends Fragment implements OnClickListener {
    private Activity activity;
    private View currentView;

    private DBHelper dbHelper;

    private Spinner spResumesList;
    private List<Resume> resumes;

    private Button btnCreateResume;
    private Button btnEditResume;
    private Button btnRemoveResume;
    private Button btnSendResume;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resumes_manager_fragment, container, false);
        this.currentView = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbHelper = new DBHelper(activity);
        initSpinner();
        initButtons();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSpinner();
        updateButtons();
    }

    private void initSpinner() {
        spResumesList = (Spinner) currentView.findViewById(R.id.sp_resumes_list);
        updateSpinner();
    }

    /**
     * Fill spinner with available resumes from DB.
     */
    private void updateSpinner() {
        resumes = dbHelper.getResumesList();
        int resumesSize = resumes.size();

        List<String> spinnerList = new ArrayList<String>();
        String spinnerListItem;
        int nPosition = 0;
        for (Resume resume : resumes) {
            nPosition++;
            spinnerListItem = nPosition + ". " + getFormattedTitle(resume);
            spinnerList.add(spinnerListItem);
        }

        String noResumes = activity.getResources().getString(R.string.resumes_list_is_empty);
        if (resumesSize == 0) {
            spinnerList.add(noResumes);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spResumesList.setAdapter(dataAdapter);

        if (resumesSize == 0) {
            spResumesList.setEnabled(false);
        } else {
            spResumesList.setEnabled(true);
        }
    }

    /**
     * Returns formatted spinner title.
     *
     * @param resume
     * @return String spinner title
     */
    private String getFormattedTitle(Resume resume) {
        String title = "[--  --]";
        String name = resume.getLastFirstName();
        String jobTitle = resume.getDesiredJobTitle();
        if (!"".equals(jobTitle)) {
            return jobTitle;
        }
        if (!"".equals(name)) {
            return name;
        }
        return title;
    }

    private void initButtons() {
        btnCreateResume = (Button) currentView.findViewById(R.id.btn_create_resume);
        btnCreateResume.setOnClickListener(this);

        btnEditResume = (Button) currentView.findViewById(R.id.btn_edit_resume);
        btnEditResume.setOnClickListener(this);

        btnRemoveResume = (Button) currentView.findViewById(R.id.btn_remove_resume);
        btnRemoveResume.setOnClickListener(this);

        btnSendResume = (Button) currentView.findViewById(R.id.btn_send_resume);
        btnSendResume.setOnClickListener(this);
        updateButtons();
    }

    private void updateButtons() {
        if (resumes.size() == 0) {
            btnCreateResume.setEnabled(true);
            btnEditResume.setEnabled(false);
            btnRemoveResume.setEnabled(false);
            btnSendResume.setEnabled(false);
        } else {
            btnCreateResume.setEnabled(true);
            btnEditResume.setEnabled(true);
            btnRemoveResume.setEnabled(true);
            btnSendResume.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_create_resume:
            createResume();
            break;
        case R.id.btn_edit_resume:
            editResume();
            break;
        case R.id.btn_remove_resume:
            removeResume();
            break;
        case R.id.btn_send_resume:
            sendResume();
            break;
        }
    }

    /**
     * Create new resume, put it into DB and open resumes editor.
     */
    private void createResume() {
        Resume resume = new Resume();
        long rowId = dbHelper.insertResumeIntoDB(resume);
        resume.setId(rowId);

        Intent intent = new Intent(activity, CreateResumeActivity.class);
        intent.putExtra(Resume.class.getCanonicalName(), resume);
        startActivity(intent);
    }

    /**
     * Open resumes editor to edit currently selected resume from spinner.
     */
    private void editResume() {
        int editPosition = spResumesList.getSelectedItemPosition();
        Resume resumeToEdit = resumes.get(editPosition);

        Intent intent = new Intent(activity, CreateResumeActivity.class);
        intent.putExtra(Resume.class.getCanonicalName(), resumeToEdit);
        startActivity(intent);
    }

    /**
     * Removes currently selected resume from DB and update view.
     */
    private void removeResume() {
        int deletedPosition = spResumesList.getSelectedItemPosition();
        long rowId = resumes.get(deletedPosition).getId();
        dbHelper.removeResumeByRowId(rowId);

        updateSpinner();
        updateButtons();
    }

    /**
     * Sends resume to HR if it properly filled.
     */
    private void sendResume() {
        int positionToSend = spResumesList.getSelectedItemPosition();
        Resume resumeToSend = resumes.get(positionToSend);

        if (resumeToSend.isFilledCorrectly()) {
            // send resume
            Intent intent = new Intent(activity, ViewResumeActivity.class);
            intent.putExtra(Resume.class.getCanonicalName(), resumeToSend);
            startActivity(intent);
        } else {
            Toast.makeText(activity, R.string.fill_name_and_position_message, Toast.LENGTH_LONG).show();
        }
    }

}
