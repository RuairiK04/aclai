package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserRVAdapter(
    private val userClickDeleteInterface: UserClickDeleteInterface,
    private val userClickInterface: UserClickInterface
) : RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {

    private val allUsers = ArrayList<UserModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTV: TextView = itemView.findViewById(R.id.idTVUsername)
        val emailTV: TextView = itemView.findViewById(R.id.idTVEmail)
        val deleteIV: ImageView = itemView.findViewById(R.id.idIVDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_rv_item,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = allUsers[position]

        holder.usernameTV.text = user.username
        holder.emailTV.text = "Email: ${user.email}"

        holder.deleteIV.setOnClickListener {
            userClickDeleteInterface.onDeleteIconClick(user)
        }

        holder.itemView.setOnClickListener {
            userClickInterface.onUserClick(user)
        }
    }

    override fun getItemCount(): Int = allUsers.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<UserModel>) {
        allUsers.clear()
        allUsers.addAll(newList)
        notifyDataSetChanged()
    }
}

interface UserClickDeleteInterface {
    fun onDeleteIconClick(user: UserModel)
}

interface UserClickInterface {
    fun onUserClick(user: UserModel)
}