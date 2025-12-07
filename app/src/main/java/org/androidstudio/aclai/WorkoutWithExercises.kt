package org.androidstudio.aclai

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseModel>
)
    