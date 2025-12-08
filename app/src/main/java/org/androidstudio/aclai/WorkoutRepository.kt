package org.androidstudio.aclai

import androidx.lifecycle.LiveData

class WorkoutRepository(private val workoutDao: WorkoutDao, private val exerciseDao: ExerciseDao) {

    val allWorkouts: LiveData<List<WorkoutWithExercises>> = workoutDao.getAllWorkoutsWithExercises()

    suspend fun insert(workout: WorkoutModel) {
        workoutDao.insert(workout)
    }

    suspend fun insertExercise(exercise: ExerciseModel) {
        exerciseDao.insert(exercise)
    }

    suspend fun delete(workout: WorkoutModel) {
        workoutDao.delete(workout)
    }

    suspend fun update(workout: WorkoutModel) {
        workoutDao.update(workout)
    }

    fun getWorkoutWithExercises(workoutId: Int): LiveData<WorkoutWithExercises> {
        return workoutDao.getWorkoutWithExercises(workoutId)
    }
}