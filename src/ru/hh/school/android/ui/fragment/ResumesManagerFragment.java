package ru.hh.school.android.ui.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.hh.school.R;
import ru.hh.school.android.DBHelper;
import ru.hh.school.android.Resume;
import ru.hh.school.android.ui.CreateResumeActivity;
import ru.hh.school.android.ui.ViewResumeActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ResumesManagerFragment extends Fragment implements OnClickListener {
    private static final String TAG = "HH_SCHOOL";

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
        Log.d(TAG, "onResume()");
        updateSpinner();
        updateButtons();
    }

    private void initSpinner() {
        spResumesList = (Spinner) currentView.findViewById(R.id.sp_resumes_list);
        updateSpinner();
    }

    private void updateSpinner() {
        resumes = getResumesList();
        int resumesSize = resumes.size();

        List<String> spinnerList = new ArrayList<String>();
        String spinnerListItem;
        int nPosition = 0;
        String jobTitle;
        String name;
        for (Resume resume : resumes) {
            nPosition++;
            name = resume.getLastFirstName();
            jobTitle = resume.getDesiredJobTitle();
            spinnerListItem = nPosition + ". " + ("".equals(jobTitle) ? name : jobTitle);
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

    private List<Resume> getResumesList() {
        List<Resume> resumes = new ArrayList<Resume>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("resume", null, null, null, null, null, null);

        Resume resume;
        int colIndex;
        if (cursor.moveToFirst()) {
            do {
                resume = new Resume();

                colIndex = cursor.getColumnIndex(DBHelper.COL_ROWID);
                resume.setId(cursor.getInt(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_NAME);
                resume.setLastFirstName(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_BIRTHDAY);
                String birthdayString = cursor.getString(colIndex);
                resume.setBirthday(new Date()); //TODO:

                colIndex = cursor.getColumnIndex(DBHelper.COL_GENDER);
                resume.setGender(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_POSITION);
                resume.setDesiredJobTitle(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_SALARY);
                resume.setSalary(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_PHONE);
                resume.setPhone(cursor.getString(colIndex));

                colIndex = cursor.getColumnIndex(DBHelper.COL_EMAIL);
                resume.setEmail(cursor.getString(colIndex));

                resumes.add(resume);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resumes;
    }

    private Resume getResumeByRowId(long rowId) {
        Resume resume = new Resume();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select = "select * from " + DBHelper.TABLE_RESUME + " where rowid = ?";
        Cursor cursor = db.rawQuery(select, new String[] {Long.toString(rowId)});

        int colIndex;
        if (cursor.moveToFirst()) {
            resume.setId((int) rowId);

            colIndex = cursor.getColumnIndex(DBHelper.COL_NAME);
            resume.setLastFirstName(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_BIRTHDAY);
            String birthdayString = cursor.getString(colIndex);
            resume.setBirthday(new Date()); //TODO:

            colIndex = cursor.getColumnIndex(DBHelper.COL_GENDER);
            resume.setGender(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_POSITION);
            resume.setDesiredJobTitle(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_SALARY);
            resume.setSalary(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_PHONE);
            resume.setPhone(cursor.getString(colIndex));

            colIndex = cursor.getColumnIndex(DBHelper.COL_EMAIL);
            resume.setEmail(cursor.getString(colIndex));
        }

        cursor.close();
        return resume;
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
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(DBHelper.COL_NAME, "");
        cv.put(DBHelper.COL_BIRTHDAY, ""); // TODO: ??
        cv.put(DBHelper.COL_GENDER, "male");
        cv.put(DBHelper.COL_POSITION, "");
        cv.put(DBHelper.COL_SALARY, "");
        cv.put(DBHelper.COL_PHONE, "");
        cv.put(DBHelper.COL_EMAIL, "");

        // TODO: check for error -1
        long rowId = db.insert(DBHelper.TABLE_RESUME, null, cv);
        db.close();

        Resume resume = getResumeByRowId(rowId);

        Intent intent = new Intent(activity, CreateResumeActivity.class);
        intent.putExtra(Resume.class.getCanonicalName(), resume);
        startActivity(intent);
    }

    private void editResume() {
        int editPosition = spResumesList.getSelectedItemPosition();
        Resume resumeToEdit = resumes.get(editPosition);

        Intent intent = new Intent(activity, CreateResumeActivity.class);
        intent.putExtra(Resume.class.getCanonicalName(), resumeToEdit);
        startActivity(intent);
    }

    private void removeResume() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedPosition = spResumesList.getSelectedItemPosition();
        int rowId = resumes.get(deletedPosition).getId();

        db.delete(DBHelper.TABLE_RESUME, "rowid=" + rowId, null);
        db.close();

        updateSpinner();
        updateButtons();
    }

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
