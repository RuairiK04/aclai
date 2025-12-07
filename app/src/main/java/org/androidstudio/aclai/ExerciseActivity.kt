package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExerciseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORKOUT_ID = "extra_workout_id"
        const val EXTRA_WORKOUT_NAME = "extra_workout_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val workoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME)
        val workoutId = intent.getIntExtra(EXTRA_WORKOUT_ID, -1)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        toolbar.title = workoutName ?: "Exercises"
        setSupportActionBar(toolbar)

        // Make the back arrow work
        toolbar.setNavigationOnClickListener {
            finish()
        }

        if (workoutId == -1) {
            // Handle error: No workout ID was passed
            finish()
            return
        }

        val fab: FloatingActionButton = findViewById(R.id.idFABAddExercise)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditExerciseActivity::class.java)
            intent.putExtra(AddEditExerciseActivity.EXTRA_WORKOUT_ID, workoutId)
            startActivity(intent)
        }



    }
}
