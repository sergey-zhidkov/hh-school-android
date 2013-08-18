package ru.hh.school.android.ui;

import ru.hh.school.R;
import ru.hh.school.android.ui.fragment.CreateResumeFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class CreateResumeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_resume_activity);
	}

    public void changeBirthday(View view) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment createResumeFragment = fragmentManager.findFragmentById(R.id.create_resume_fragment);
        ((CreateResumeFragment) createResumeFragment).changeBirthday(view);
    }
}
