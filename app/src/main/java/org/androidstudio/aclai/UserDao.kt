package org.androidstudio.aclai

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserModel)

    @Delete
    suspend fun delete(user: UserModel)

    @Update
    suspend fun update(user: UserModel)

    @Query("SELECT * FROM userTable ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<UserModel>>


    @Query("SELECT * FROM userTable WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserModel?

    companion object
}