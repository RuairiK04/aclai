package org.androidstudio.aclai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var exerciseAdapter: ExerciseRVAdapter
    private lateinit var workoutAdapter: WorkoutRVAdapter
    private lateinit var userAdapter: UserRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- RecyclerViews ---
        val exerciseRV = findViewById<RecyclerView>(R.id.idRVExercises)
        val workoutRV = findViewById<RecyclerView>(R.id.idRVWorkouts)
        val userRV = findViewById<RecyclerView>(R.id.idRVUsers)

        exerciseRV.layoutManager = LinearLayoutManager(this)
        workoutRV.layoutManager = LinearLayoutManager(this)
        userRV.layoutManager = LinearLayoutManager(this)

        // --- Adapters ---
        exerciseAdapter = ExerciseRVAdapter(
            exerciseClickDeleteInterface = object : ExerciseClickDeleteInterface {
                override fun onDeleteIconClick(exercise: ExerciseModel) {
                    exerciseViewModel.deleteExercise(exercise)
                    Toast.makeText(this@MainActivity, "Exercise Deleted", Toast.LENGTH_SHORT).show()
                }
            },
            exerciseClickInterface = object : ExerciseClickInterface {
                override fun onExerciseClick(exercise: ExerciseModel) {
                    val intent = Intent(this@MainActivity, AddEditExerciseActivity::class.java)
                    intent.putExtra("exerciseType", "Edit")
                    intent.putExtra("exerciseName", exercise.exerciseName)
                    intent.putExtra("exerciseSets", exercise.sets)
                    intent.putExtra("exerciseReps", exercise.reps)
                    intent.putExtra("exerciseWeight", exercise.weight)
                    intent.putExtra("exerciseId", exercise.id)
                    startActivity(intent)
                }
            }
        )
        exerciseRV.adapter = exerciseAdapter

        workoutAdapter = WorkoutRVAdapter(
            workoutClickDeleteInterface = object : WorkoutClickDeleteInterface {
                override fun onDeleteIconClick(workout: WorkoutModel) {
                    workoutViewModel.deleteWorkout(workout)
                    Toast.makeText(this@MainActivity, "Workout Deleted", Toast.LENGTH_SHORT).show()
                }
            },
            workoutClickInterface = object : WorkoutClickInterface {
                override fun onWorkoutClick(workout: WorkoutModel) {
                    val intent = Intent(this@MainActivity, AddEditWorkoutActivity::class.java)
                    intent.putExtra("workoutType", "Edit")
                    intent.putExtra("workoutName", workout.workoutname)
                    intent.putExtra("workoutDescription", workout.workoutdescription)
                    intent.putExtra("workoutCategory", workout.category)
                    intent.putExtra("workoutId", workout.id)
                    startActivity(intent)
                }
            }
        )
        workoutRV.adapter = workoutAdapter

        userAdapter = UserRVAdapter(
            userClickDeleteInterface = object : UserClickDeleteInterface {
                override fun onDeleteIconClick(user: UserModel) {
                    userViewModel.deleteUser(user)
                    Toast.makeText(this@MainActivity, "User Deleted", Toast.LENGTH_SHORT).show()
                }
            },
            userClickInterface = object : UserClickInterface {
                override fun onUserClick(user: UserModel) {
                    val intent = Intent(this@MainActivity, AddEditUserActivity::class.java)
                    intent.putExtra("userType", "Edit")
                    intent.putExtra("username", user.username)
                    intent.putExtra("email", user.email)
                    intent.putExtra("password", user.password)
                    intent.putExtra("userId", user.id)
                    startActivity(intent)
                }
            }
        )
        userRV.adapter = userAdapter

        // --- ViewModels ---
        exerciseViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ExerciseViewModel::class.java]

        workoutViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[WorkoutViewModel::class.java]

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[UserViewModel::class.java]

        // --- Observers ---
        exerciseViewModel.allExercises.observe(this, Observer { list ->
            list?.let { exerciseAdapter.updateList(it) }
        })

        workoutViewModel.allWorkouts.observe(this, Observer { list ->
            list?.let { workoutAdapter.updateList(it) }
        })

        userViewModel.allUsers.observe(this, Observer { list ->
            list?.let { userAdapter.updateList(it) }
        })

        // --- FloatingActionButtons ---
        val fabAddExercise = findViewById<FloatingActionButton>(R.id.fabAddExercise)
        val fabAddWorkout = findViewById<FloatingActionButton>(R.id.fabAddWorkout)
        val fabAddUser = findViewById<FloatingActionButton>(R.id.fabAddUser)

        fabAddExercise.setOnClickListener {
            startActivity(Intent(this, AddEditExerciseActivity::class.java))
        }

        fabAddWorkout.setOnClickListener {
            startActivity(Intent(this, AddEditWorkoutActivity::class.java))
        }

        fabAddUser.setOnClickListener {
            startActivity(Intent(this, AddEditUserActivity::class.java))
        }
    }
}