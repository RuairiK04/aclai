package org.androidstudio.aclai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    val allExercises: LiveData<List<ExerciseModel>>
    private val repository: ExerciseRepository

    init {
        val dao = AppDatabase.getDatabase(application).getExerciseDao()
        repository = ExerciseRepository(dao)
        allExercises = repository.allExercises
    }

    fun addExercise(exercise: ExerciseModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(exercise)
    }

    fun updateExercise(exercise: ExerciseModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(exercise)
    }

    fun deleteExercise(exercise: ExerciseModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(exercise)
    }
}