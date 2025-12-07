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

    @Query("SELECT * FROM workoutTable ORDER BY id ASC")
    fun getAllWorkouts(): LiveData<List<WorkoutModel>>
}