package org.androidstudio.aclai

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercisesTable",
    foreignKeys = [ForeignKey(
        entity = WorkoutModel::class,
        parentColumns = ["id"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExerciseModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutId: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double
)
