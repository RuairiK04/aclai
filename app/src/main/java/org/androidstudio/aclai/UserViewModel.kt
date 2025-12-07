package org.androidstudio.aclai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val allUsers: LiveData<List<UserModel>>
    private val repository: UserRepository

    init {
        val dao = AppDatabase.getDatabase(application).getUserDao()
        repository = UserRepository(dao)
        allUsers = repository.allUsers
    }

    fun addUser(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun updateUser(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(user)
    }

    fun deleteUser(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(user)
    }
}