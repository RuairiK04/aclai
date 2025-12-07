package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), WorkoutRVAdapter.WorkoutClickListener {

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var workoutAdapter: WorkoutRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.idFABAdd)
        fab.setOnClickListener {
            startActivity(Intent(this, AddEditWorkoutActivity::class.java))
        }

        // RecyclerView and other setup code

        val workoutRV = findViewById<RecyclerView>(R.id.idRVWorkouts)
        workoutRV.layoutManager = LinearLayoutManager(this)

        workoutAdapter = WorkoutRVAdapter(this)
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

    override fun onWorkoutClick(workout: WorkoutModel) {
        val intent = Intent(this@MainActivity, ExerciseActivity::class.java).apply {
            putExtra(ExerciseActivity.EXTRA_WORKOUT_ID, workout.id)
            putExtra(ExerciseActivity.EXTRA_WORKOUT_NAME, workout.workoutname)
        }
        startActivity(intent)
    }

    override fun onDeleteIconClick(workout: WorkoutModel) {
        workoutViewModel.deleteWorkout(workout)
        Toast.makeText(this, "Workout Deleted", Toast.LENGTH_SHORT).show()
    }
}
