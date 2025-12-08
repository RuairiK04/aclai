package org.androidstudio.aclai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    val allWorkouts: LiveData<List<WorkoutWithExercises>>
    private val repository: WorkoutRepository

    init {
        val dao = AppDatabase.getDatabase(application).getWorkoutDao()
        repository = WorkoutRepository(dao)
        allWorkouts = repository.allWorkouts
    }

    fun getWorkoutWithExercises(workoutId: Int): LiveData<WorkoutWithExercises> {
        return repository.getWorkoutWithExercises(workoutId)
    }

    fun addWorkout(workout: WorkoutModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(workout)
    }

    fun updateWorkout(workout: WorkoutModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(workout)
    }

    fun deleteWorkout(workout: WorkoutModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(workout)
    }
}