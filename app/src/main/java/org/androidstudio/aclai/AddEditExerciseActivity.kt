package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditExerciseActivity : AppCompatActivity() {

    private lateinit var exerciseNameEdt: EditText
    private lateinit var setsEdt: EditText
    private lateinit var repsEdt: EditText
    private lateinit var weightEdt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ExerciseViewModel
    private var exerciseID = -1

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

        val exerciseType = intent.getStringExtra("exerciseType")
        if (exerciseType == "Edit") {
            val name = intent.getStringExtra("exerciseName")
            val sets = intent.getIntExtra("exerciseSets", 0)
            val reps = intent.getIntExtra("exerciseReps", 0)
            val weight = intent.getFloatExtra("exerciseWeight", 0f)
            exerciseID = intent.getIntExtra("exerciseId", -1)

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
            val weight = weightEdt.text.toString().toFloatOrNull() ?: 0f

            if (name.isNotEmpty() && sets > 0 && reps > 0) {
                val exercise = ExerciseModel(name, sets, reps, weight)
                if (exerciseType == "Edit") {
                    exercise.id = exerciseID
                    viewModel.updateExercise(exercise)
                    Toast.makeText(this, "Exercise Updated", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addExercise(exercise)
                    Toast.makeText(this, "$name Added", Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_LONG).show()
            }
        }
    }
}