package org.androidstudio.aclai

import androidx.lifecycle.LiveData

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    // Get all workouts
    val allWorkouts: LiveData<List<WorkoutModel>> = workoutDao.getAllWorkouts()

    // Insert workout
    suspend fun insert(workout: WorkoutModel) {
        workoutDao.insert(workout)
    }

    // Delete workout
    suspend fun delete(workout: WorkoutModel) {
        workoutDao.delete(workout)
    }

    // Update workout
    suspend fun update(workout: WorkoutModel) {
        workoutDao.update(workout)
    }

    fun getWorkoutWithExercises(workoutId: Int): LiveData<WorkoutWithExercises> {
        return workoutDao.getWorkoutWithExercises(workoutId)
    }
}