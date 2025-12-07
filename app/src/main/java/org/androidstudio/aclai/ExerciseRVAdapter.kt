package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseRVAdapter(
    private val exerciseClickDeleteInterface: ExerciseClickDeleteInterface,
    private val exerciseClickInterface: ExerciseClickInterface
) : RecyclerView.Adapter<ExerciseRVAdapter.ViewHolder>() {

    // List of exercises
    private val allExercises = ArrayList<ExerciseModel>()

    // ViewHolder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseNameTV: TextView = itemView.findViewById(R.id.idTVExerciseName)
        val setsTV: TextView = itemView.findViewById(R.id.idTVSets)
        val repsTV: TextView = itemView.findViewById(R.id.idTVReps)
        val weightTV: TextView = itemView.findViewById(R.id.idTVWeight)
        val deleteIV: ImageView = itemView.findViewById(R.id.idIVDeleteExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.exercise_rv_item,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val exercise = allExercises[position]

        holder.exerciseNameTV.text = exercise.exerciseName
        holder.setsTV.text = "Sets: ${exercise.sets}"
        holder.repsTV.text = "Reps: ${exercise.reps}"
        holder.weightTV.text = "Weight: ${exercise.weight}kg"

        // Click for delete icon
        holder.deleteIV.setOnClickListener {
            exerciseClickDeleteInterface.onDeleteIconClick(exercise)
        }

        // Click for editing the exercise
        holder.itemView.setOnClickListener {
            exerciseClickInterface.onExerciseClick(exercise)
        }
    }

    override fun getItemCount(): Int = allExercises.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ExerciseModel>) {
        allExercises.clear()
        allExercises.addAll(newList)
        notifyDataSetChanged()
    }
}

// Delete click interface
interface ExerciseClickDeleteInterface {
    fun onDeleteIconClick(exercise: ExerciseModel)
}

// Row click interface (for update)
interface ExerciseClickInterface {
    fun onExerciseClick(exercise: ExerciseModel)
}