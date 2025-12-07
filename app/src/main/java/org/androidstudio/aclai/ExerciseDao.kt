package org.androidstudio.aclai

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: ExerciseModel)

    @Delete
    suspend fun delete(exercise: ExerciseModel)

    @Update
    suspend fun update(exercise: ExerciseModel)

    @Query("SELECT * FROM exercisesTable ORDER BY id ASC")
    fun getAllExercises(): LiveData<List<ExerciseModel>>

    @Query("SELECT * FROM exercisesTable WHERE workoutId = :workoutId ORDER BY id ASC")
    fun getExercisesForWorkout(workoutId: Int): LiveData<List<ExerciseModel>>
}