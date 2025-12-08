package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AddEditExerciseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORKOUT_ID = "extra_workout_id"
        const val EXTRA_EXERCISE_TYPE = "exerciseType"
        const val EXTRA_EXERCISE_NAME = "exerciseName"
        const val EXTRA_EXERCISE_SETS = "exerciseSets"
        const val EXTRA_EXERCISE_REPS = "exerciseReps"
        const val EXTRA_EXERCISE_WEIGHT = "exerciseWeight"
        const val EXTRA_EXERCISE_ID = "exerciseId"
    }

    private lateinit var exerciseNameEdt: EditText
    private lateinit var setsEdt: EditText
    private lateinit var repsEdt: EditText
    private lateinit var weightEdt: EditText
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button

    private lateinit var viewModel: ExerciseViewModel
    private var id = -1
    private var workoutID = -1 // <-- 1. Add a variable to hold the workout ID

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_exercise)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ExerciseViewModel::class.java]

        exerciseNameEdt = findViewById(R.id.idEdtExerciseName)
        setsEdt = findViewById(R.id.idEdtSets)
        repsEdt = findViewById(R.id.idEdtReps)
        weightEdt = findViewById(R.id.idEdtWeight)
        saveBtn = findViewById(R.id.idBtnSaveExercise)
        cancelBtn = findViewById(R.id.idBtnCancel)

        // <-- 2. Get the workoutId from the Intent
        workoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1)

        val exerciseType = intent.getStringExtra(EXTRA_EXERCISE_TYPE)
        if (exerciseType == "Edit") {
            val name = intent.getStringExtra(EXTRA_EXERCISE_NAME)
            val sets = intent.getIntExtra(EXTRA_EXERCISE_SETS, 0)
            val reps = intent.getIntExtra(EXTRA_EXERCISE_REPS, 0)
            val weight = intent.getFloatExtra(EXTRA_EXERCISE_WEIGHT, 0f)
            id = intent.getIntExtra(EXTRA_EXERCISE_ID, -1)

            saveBtn.text = "Update Exercise"
            exerciseNameEdt.setText(name)
            setsEdt.setText(sets.toString())
            repsEdt.setText(reps.toString())
            weightEdt.setText(weight.toString())
        } else {
            saveBtn.text = "Save Exercise"
        }

        saveBtn.setOnClickListener {
            val name = exerciseNameEdt.text.toString()
            val sets = setsEdt.text.toString().toIntOrNull() ?: 0
            val reps = repsEdt.text.toString().toIntOrNull() ?: 0
            // The weight from the EditText is a floating point number
            val weight = weightEdt.text.toString().toDoubleOrNull() ?: 0.0

            // Ensure we have a valid workout ID before saving
            if (name.isNotEmpty() && sets > 0 && reps > 0 && workoutID != -1) {

                if (exerciseType == "Edit") {
                    // For updating, create the model with the existing exerciseID
                    val exercise = ExerciseModel(id, workoutID, name, sets, reps, weight)
                    viewModel.updateExercise(exercise)
                    Toast.makeText(this, "Exercise Updated", Toast.LENGTH_LONG).show()
                } else {
                    // For adding, create the model with a default id of 0
                    val exercise = ExerciseModel(0, workoutID, name, sets, reps, weight)
                    viewModel.addExercise(exercise)
                    Toast.makeText(this, "$name Added", Toast.LENGTH_LONG).show()
                }
                // It's better to go back to the ExerciseActivity, not MainActivity
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_LONG).show()
            }
        }

        cancelBtn.setOnClickListener {
            finish()
        }
    }
}