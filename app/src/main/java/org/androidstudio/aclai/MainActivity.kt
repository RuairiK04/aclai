package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
// Import android.view.Menu
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var workoutAdapter: WorkoutRVAdapter
    private var deleteMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar) // This is correct!

        toolbar.setNavigationOnClickListener {
            deleteMode = !deleteMode
            workoutAdapter.setDeleteMode(deleteMode)

            val msg = if (deleteMode) "Delete mode ON" else "Delete mode OFF"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // RecyclerView and other setup code

        val workoutRV = findViewById<RecyclerView>(R.id.idRVWorkouts)
        workoutRV.layoutManager = LinearLayoutManager(this)

        workoutAdapter = WorkoutRVAdapter(
            workoutClickDeleteInterface = object : WorkoutClickDeleteInterface {
                override fun onDeleteIconClick(workout: WorkoutModel) {
                    if (deleteMode) {
                        workoutViewModel.deleteWorkout(workout)
                        Toast.makeText(this@MainActivity, "Workout Deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            workoutClickInterface = object : WorkoutClickInterface {
                override fun onWorkoutClick(workout: WorkoutModel) {
                    if (!deleteMode) {
                        val intent = Intent(this@MainActivity, ExerciseActivity::class.java).apply {
                            putExtra(ExerciseActivity.EXTRA_WORKOUT_ID, workout.id)
                            putExtra(ExerciseActivity.EXTRA_WORKOUT_NAME, workout.workoutname)
                        }
                        startActivity(intent)
                    }
                }
            }
        )

        workoutRV.adapter = workoutAdapter

        // ViewModel
        workoutViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[WorkoutViewModel::class.java]

        workoutViewModel.allWorkouts.observe(this, Observer { list ->
            list?.let { workoutAdapter.updateList(it) }
        })
    }

    // vvvvv  ADD THIS ENTIRE FUNCTION  vvvvv
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // This line inflates your menu resource file and adds the items to the app bar.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    // ^^^^^  ADD THIS ENTIRE FUNCTION  ^^^^^

    // This function will now work correctly because the menu item exists.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // The caret was here, indicating your focus.
                // This correctly starts the activity.
                startActivity(Intent(this, AddEditWorkoutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
