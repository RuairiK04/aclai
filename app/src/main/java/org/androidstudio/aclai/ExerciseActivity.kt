package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ExerciseActivity : AppCompatActivity(), ExerciseRVAdapter.OnExerciseListener {

    private lateinit var viewModel: ExerciseViewModel
    private lateinit var exerciseRVAdapter: ExerciseRVAdapter
    private lateinit var exercisesRV: RecyclerView

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

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                R.id.action_timer -> {
                    showTimerDialog()
                    true
                }
                else -> false
            }
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

        exercisesRV = findViewById(R.id.idRVExercises)
        exercisesRV.layoutManager = LinearLayoutManager(this)
        exerciseRVAdapter = ExerciseRVAdapter(this, this)
        exercisesRV.adapter = exerciseRVAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ExerciseViewModel::class.java]
        viewModel.getExercisesForWorkout(workoutId).observe(this, {
            exerciseRVAdapter.updateList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val exercise = exerciseRVAdapter.getExerciseAt(position)
                viewModel.deleteExercise(exercise)

                Snackbar.make(exercisesRV, "Exercise Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.addExercise(exercise)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(exercisesRV)
    }

    private fun showTimerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_timer, null)
        val editTextTimer = dialogView.findViewById<EditText>(R.id.editTextTimer)
        val buttonStartTimer = dialogView.findViewById<Button>(R.id.buttonStartTimer)
        val textViewTimer = dialogView.findViewById<TextView>(R.id.textViewTimer)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        buttonStartTimer.setOnClickListener {
            val timeInSeconds = editTextTimer.text.toString().toLongOrNull()
            if (timeInSeconds != null && timeInSeconds > 0) {
                object : CountDownTimer(timeInSeconds * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        textViewTimer.text = "${millisUntilFinished / 1000}s"
                    }

                    override fun onFinish() {
                        textViewTimer.text = "0s"
                        Toast.makeText(this@ExerciseActivity, "Rest Finished!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }.start()
            } else {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exercise_activity_menu, menu)
        return true
    }
}
