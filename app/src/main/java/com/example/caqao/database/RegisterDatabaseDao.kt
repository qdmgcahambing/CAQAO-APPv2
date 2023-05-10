package com.example.caqao.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegisterDatabaseDao {

    @Insert
    suspend fun insert(register: RegisterEntity)

    //@Delete
    //suspend  fun deleteSubscriber(register: RegisterEntity):Int

//    @Query("SELECT * FROM Register_users_table ORDER BY id DESC")
//    suspend fun getAllUsers(): LiveData<List<RegisterEntity>>

    @Query("SELECT * FROM Register_users_table WHERE id = :id")
    suspend fun getById(id: String): RegisterEntity

//    @Query("DELETE FROM Register_users_table")
//    suspend fun deleteAll(): Int

//    @Query("SELECT * FROM Register_users_table WHERE username LIKE :username")
//    suspend fun getUsername(username: String): RegisterEntity?

    @Query("SELECT COUNT () FROM register_users_table WHERE username = :username")
    fun checkUser(username:String):Int

    @Query("SELECT * FROM Register_users_table WHERE username = :username AND password = :password")
    fun checkAccount(username:String, password: String):List<RegisterEntity>

}