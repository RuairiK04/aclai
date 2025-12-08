package org.androidstudio.aclai

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class MainActivity : AppCompatActivity(), WorkoutRVAdapter.WorkoutClickListener {

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var workoutAdapter: WorkoutRVAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val addWorkoutLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Workout Saved", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val PREFS_NAME = "notification_prefs"
        const val PREF_KEY_INTERVAL = "notification_interval"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.idFABAdd)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditWorkoutActivity::class.java)
            addWorkoutLauncher.launch(intent)
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
                val workoutWithExercises = workoutAdapter.getWorkoutAt(position)
                val workout = workoutWithExercises.workout
                workoutViewModel.deleteWorkout(workout)

                Snackbar.make(workoutRV, "Workout Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        workoutViewModel.addWorkout(workout)
                        workoutWithExercises.exercises.forEach { exercise ->
                            workoutViewModel.addExercise(exercise)
                        }
                    }
                    show()
                }
            }
        }).attachToRecyclerView(workoutRV)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        createNotificationChannel()
        // Schedule notification based on saved preference
        val savedInterval = sharedPreferences.getLong(PREF_KEY_INTERVAL, AlarmManager.INTERVAL_HOUR)
        scheduleNotification(savedInterval)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Workout Reminder"
            val descriptionText = "Channel for workout reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("workout_reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(intervalMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Cancel any existing alarms
        alarmManager.cancel(pendingIntent)

        if (intervalMillis > 0) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.MILLISECOND, intervalMillis.toInt())
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intervalMillis,
                pendingIntent
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                showNotificationFrequencyDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNotificationFrequencyDialog() {
        val frequencies = arrayOf("10 seconds", "Hourly", "Daily", "Never")
        val intervals = longArrayOf(10000L, AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_DAY, 0L)

        val currentInterval = sharedPreferences.getLong(PREF_KEY_INTERVAL, AlarmManager.INTERVAL_HOUR)
        val currentSelection = intervals.indexOf(currentInterval)

        AlertDialog.Builder(this)
            .setTitle("Set Notification Frequency")
            .setSingleChoiceItems(frequencies, currentSelection) { dialog, which ->
                val newInterval = intervals[which]
                with(sharedPreferences.edit()) {
                    putLong(PREF_KEY_INTERVAL, newInterval)
                    apply()
                }
                scheduleNotification(newInterval)
                val frequency = frequencies[which]
                Toast.makeText(this, "Notification frequency set to: $frequency", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    override fun onWorkoutClick(workout: WorkoutModel) {
        val intent = Intent(this@MainActivity, ExerciseActivity::class.java).apply {
            putExtra(ExerciseActivity.EXTRA_WORKOUT_ID, workout.id)
            putExtra(ExerciseActivity.EXTRA_WORKOUT_NAME, workout.workoutname)
        }
        startActivity(intent)
    }
}