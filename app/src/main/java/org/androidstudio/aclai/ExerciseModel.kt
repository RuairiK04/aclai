package org.androidstudio.aclai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseTable")
class ExerciseModel(
    @ColumnInfo(name = "exerciseName") val exerciseName: String,
    @ColumnInfo(name = "sets") val sets: Int,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "weight") val weight: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}