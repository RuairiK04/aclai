package org.androidstudio.aclai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workoutTable")
class WorkoutModel(
    @ColumnInfo(name = "workoutname") val workoutname: String,
    @ColumnInfo(name = "workoutdescription") val workoutdescription: String,
    @ColumnInfo(name = "category") val category: String,
    ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}