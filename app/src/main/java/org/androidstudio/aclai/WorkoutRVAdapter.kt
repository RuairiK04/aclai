package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutRVAdapter(
    private val workoutClickListener: WorkoutClickListener
) : RecyclerView.Adapter<WorkoutRVAdapter.ViewHolder>() {

    private val allWorkouts = ArrayList<WorkoutWithExercises>()

    interface WorkoutClickListener {
        fun onWorkoutClick(workout: WorkoutModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameTV: TextView = itemView.findViewById(R.id.idTVWorkoutName)
        val workoutDescTV: TextView = itemView.findViewById(R.id.idTVWorkoutDescription)
        val categoryTV: TextView = itemView.findViewById(R.id.idTVWorkoutCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.workout_rv_item,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutWithExercises = allWorkouts[position]
        val workout = workoutWithExercises.workout

        holder.workoutNameTV.text = workout.workoutname

        if (!workout.workoutdescription.isNullOrEmpty()) {
            holder.workoutDescTV.visibility = View.VISIBLE
            holder.workoutDescTV.text = "Description: ${workout.workoutdescription}"
        } else {
            holder.workoutDescTV.visibility = View.GONE
        }

        if (!workout.category.isNullOrEmpty()) {
            holder.categoryTV.visibility = View.VISIBLE
            holder.categoryTV.text = "Category: ${workout.category}"
        } else {
            holder.categoryTV.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            workoutClickListener.onWorkoutClick(workout)
        }
    }

    override fun getItemCount(): Int = allWorkouts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<WorkoutWithExercises>) {
        allWorkouts.clear()
        allWorkouts.addAll(newList)
        notifyDataSetChanged()
    }

    fun getWorkoutAt(position: Int): WorkoutWithExercises {
        return allWorkouts[position]
    }
}
