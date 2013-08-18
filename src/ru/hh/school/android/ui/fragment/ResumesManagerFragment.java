package ru.hh.school.android.ui.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.hh.school.R;
import ru.hh.school.android.DBHelper;
import ru.hh.school.android.Resume;
import ru.hh.school.android.ui.CreateResumeActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
        resumes = getResumesList();
        initSpinner();
        initButtons();
    }

    private void initSpinner() {
        spResumesList = (Spinner) currentView.findViewById(R.id.sp_resumes_list);
        updateSpinner();
    }

    private void updateSpinner() {
        int resumesSize = resumes.size();

        List<String> spinnerList = new ArrayList<String>();
        String spinnerListItem;
        for (Resume resume : resumes) {
            spinnerListItem = resume.getId() + ". " + resume.getDesiredJobTitle();
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
        }
    }

    private List<Resume> getResumesList() {
        List<Resume> resumes = new ArrayList<Resume>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("resume", null, null, null, null, null, null);

        Resume resume;
        int colIndex;
        if (cursor.moveToFirst()) {
            do {
                resume = new Resume();

                colIndex = cursor.getColumnIndex("id");
                resume.setId(cursor.getInt(colIndex));

                colIndex = cursor.getColumnIndex("name");
                resume.setLastFirstName(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex("birthday");
                String birthdayString = cursor.getString(colIndex);
                resume.setBirthday(new Date()); //TODO:

                colIndex = cursor.getColumnIndex("gender");
                resume.setGender(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex("position");
                resume.setDesiredJobTitle(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex("salary");
                resume.setSalary(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex("phone");
                resume.setPhone(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex("email");
                resume.setEmail(cursor.getString(colIndex));
            } while (cursor.moveToNext());
        }

        return resumes;
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

    private void createResume() {
        Intent intent = new Intent(activity, CreateResumeActivity.class);
        startActivity(intent);
    }

    private void editResume() {
        // TODO Auto-generated method stub

    }

    private void removeResume() {
        // TODO Auto-generated method stub

    }

    private void sendResume() {
        // TODO Auto-generated method stub

    }
}
