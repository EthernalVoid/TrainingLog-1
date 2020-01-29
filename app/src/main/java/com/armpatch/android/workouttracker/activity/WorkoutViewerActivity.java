package com.armpatch.android.workouttracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.armpatch.android.workouttracker.R;
import com.armpatch.android.workouttracker.Tools;
import com.armpatch.android.workouttracker.adapters.ExerciseGroupRecyclerAdapter;
import com.armpatch.android.workouttracker.adapters.WorkoutPagerAdapter;

import org.threeten.bp.LocalDate;

public class WorkoutViewerActivity extends AppCompatActivity implements ExerciseGroupRecyclerAdapter.Callback {

    TextView dateBarText;
    ViewPager viewPager;
    WorkoutPagerAdapter adapter;
    LocalDate currentDate;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            currentDate = adapter.getSelectedItemDate(position);
            dateBarText.setText(Tools.getRelativeDateText(WorkoutViewerActivity.this, currentDate));
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
    private View.OnClickListener gotoToday = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoToday();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_pager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        dateBarText = findViewById(R.id.date_bar_text);
        dateBarText.setOnClickListener(gotoToday);

        viewPager = findViewById(R.id.view_pager);
        adapter = new WorkoutPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gotoToday();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateCurrentWorkoutHolder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_exercise) {
            Intent addExerciseIntent = ExerciseSelectionActivity.getIntent(this, currentDate);
            startActivity(addExerciseIntent);
        }

        return true;
    }

    private void gotoToday() {
        viewPager.setCurrentItem(WorkoutPagerAdapter.POSITION_TODAY, false);
    }

    @Override
    public void onExerciseGroupSelected(String exerciseName) {
        Intent exerciseTracker = ExerciseTrackerActivity.getIntent(
                this,
                Tools.stringFromDate(currentDate),
                exerciseName);

        startActivity(exerciseTracker);
    }
}
