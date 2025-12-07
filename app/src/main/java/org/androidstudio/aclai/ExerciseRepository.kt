package org.androidstudio.aclai

import androidx.lifecycle.LiveData

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    // Get all exercises
    val allExercises: LiveData<List<ExerciseModel>> = exerciseDao.getAllExercises()

    // Insert exercise
    suspend fun insert(exercise: ExerciseModel) {
        exerciseDao.insert(exercise)
    }

    // Delete exercise
    suspend fun delete(exercise: ExerciseModel) {
        exerciseDao.delete(exercise)
    }

    // Update exercise
    suspend fun update(exercise: ExerciseModel) {
        exerciseDao.update(exercise)
    }
}