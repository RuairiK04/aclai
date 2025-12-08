package org.androidstudio.aclai

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutModel)

    @Delete
    suspend fun delete(workout: WorkoutModel)

    @Update
    suspend fun update(workout: WorkoutModel)

    @Transaction
    @Query("SELECT * FROM workoutTable")
    fun getAllWorkoutsWithExercises(): LiveData<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workoutTable WHERE id = :workoutId")
    fun getWorkoutWithExercises(workoutId: Int): LiveData<WorkoutWithExercises>

}
