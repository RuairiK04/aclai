package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutRVAdapter(
    private val workoutClickListener: WorkoutClickListener
) : RecyclerView.Adapter<WorkoutRVAdapter.ViewHolder>() {

    private val allWorkouts = ArrayList<WorkoutModel>()

    interface WorkoutClickListener {
        fun onWorkoutClick(workout: WorkoutModel)
        fun onDeleteIconClick(workout: WorkoutModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameTV: TextView = itemView.findViewById(R.id.idTVWorkoutName)
        val workoutDescTV: TextView = itemView.findViewById(R.id.idTVWorkoutDescription)
        val categoryTV: TextView = itemView.findViewById(R.id.idTVWorkoutCategory)
        val deleteIV: ImageView = itemView.findViewById(R.id.idIVDeleteWorkout)
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
        val workout = allWorkouts[position]

        holder.workoutNameTV.text = workout.workoutname
        holder.workoutDescTV.text = "Description: ${workout.workoutdescription}"
        holder.categoryTV.text = "Category: ${workout.category}"

        holder.deleteIV.setOnClickListener {
            workoutClickListener.onDeleteIconClick(workout)
        }

        holder.itemView.setOnClickListener {
            workoutClickListener.onWorkoutClick(workout)
        }
    }

    override fun getItemCount(): Int = allWorkouts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<WorkoutModel>) {
        allWorkouts.clear()
        allWorkouts.addAll(newList)
        notifyDataSetChanged()
    }
}
