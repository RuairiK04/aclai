package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExerciseActivity : AppCompatActivity(), ExerciseRVAdapter.OnExerciseListener {

    private lateinit var viewModel: ExerciseViewModel

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

        val exercisesRV: RecyclerView = findViewById(R.id.idRVExercises)
        exercisesRV.layoutManager = LinearLayoutManager(this)
        val exerciseRVAdapter = ExerciseRVAdapter(this, this)
        exercisesRV.adapter = exerciseRVAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ExerciseViewModel::class.java]
        viewModel.getExercisesForWorkout(workoutId).observe(this, {
            exerciseRVAdapter.updateList(it)
        })
    }

    override fun onExerciseClick(exercise: ExerciseModel) {
        val intent = Intent(this, AddEditExerciseActivity::class.java)
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_TYPE, "Edit")
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_NAME, exercise.exerciseName)
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_SETS, exercise.sets)
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_REPS, exercise.reps)
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_WEIGHT, exercise.weight)
        intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_ID, exercise.id)
        intent.putExtra(AddEditExerciseActivity.EXTRA_WORKOUT_ID, exercise.workoutId)
        startActivity(intent)
    }

    override fun onExerciseDeleteClick(exercise: ExerciseModel) {
        viewModel.deleteExercise(exercise)
        Toast.makeText(this, "${exercise.exerciseName} deleted", Toast.LENGTH_SHORT).show()
    }
}
