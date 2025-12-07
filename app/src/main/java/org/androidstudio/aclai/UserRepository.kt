package org.androidstudio.aclai

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    // Get all users
    val allUsers: LiveData<List<UserModel>> = userDao.getAllUsers()

    // Insert user
    suspend fun insert(user: UserModel) {
        userDao.insert(user)
    }

    // Delete user
    suspend fun delete(user: UserModel) {
        userDao.delete(user)
    }

    // Update user
    suspend fun update(user: UserModel) {
        userDao.update(user)
    }

    //Find user by username (for login)
    fun getUserByUsername(username: String): LiveData<UserModel?> {
        return UserDao.getUserByUsername(username)
    }
}

private fun UserDao.Companion.getUserByUsername(username: String): LiveData<UserModel?> {}
