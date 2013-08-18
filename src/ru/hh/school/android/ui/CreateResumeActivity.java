package ru.hh.school.android.ui;

import ru.hh.school.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class CreateResumeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_resume_activity);
	}

    public void changeBirthday(View view) {
        // TODO: add datepicker dialog
        Toast.makeText(this, "Change Birthday button clicked!", Toast.LENGTH_LONG).show();
    }
}
