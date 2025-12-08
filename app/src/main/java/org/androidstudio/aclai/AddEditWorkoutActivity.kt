package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AddEditWorkoutActivity : AppCompatActivity() {

    private lateinit var workoutNameEdt: EditText
    private lateinit var workoutDescEdt: EditText
    private lateinit var categoryEdt: EditText
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button

    private lateinit var viewModel: WorkoutViewModel
    private var workoutID = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_workout)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[WorkoutViewModel::class.java]

        workoutNameEdt = findViewById(R.id.idEdtWorkoutName)
        workoutDescEdt = findViewById(R.id.idEdtWorkoutDesc)
        categoryEdt = findViewById(R.id.idEdtWorkoutCategory)
        saveBtn = findViewById(R.id.idBtnSaveWorkout)
        cancelBtn = findViewById(R.id.idBtnCancel)

        val workoutType = intent.getStringExtra("workoutType")
        if (workoutType == "Edit") {
            val name = intent.getStringExtra("workoutName")
            val desc = intent.getStringExtra("workoutDescription")
            val category = intent.getStringExtra("workoutCategory")
            workoutID = intent.getIntExtra("workoutId", -1)

            saveBtn.text = "Update Workout"
            workoutNameEdt.setText(name)
            workoutDescEdt.setText(desc)
            categoryEdt.setText(category)
        } else {
            saveBtn.text = "Save Workout"
        }

        saveBtn.setOnClickListener {
            val name = workoutNameEdt.text.toString()
            val desc = workoutDescEdt.text.toString()
            val category = categoryEdt.text.toString()

            if (name.isNotEmpty()) {
                val workout = WorkoutModel(name, desc.ifEmpty { null }, category.ifEmpty { null })
                if (workoutType == "Edit") {
                    workout.id = workoutID
                    viewModel.updateWorkout(workout)
                    Toast.makeText(this, "Workout Updated", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addWorkout(workout)
                    Toast.makeText(this, "$name Added", Toast.LENGTH_LONG).show()
                }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Please enter a workout name", Toast.LENGTH_LONG).show()
            }
        }

        cancelBtn.setOnClickListener {
            finish()
        }
    }
}