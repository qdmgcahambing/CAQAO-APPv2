package com.example.caqao.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Register_users_table")
class RegisterEntity(
//    @PrimaryKey(autoGenerate = true) var id: Int = 0 ,
    @PrimaryKey(autoGenerate = false)
//    @PrimaryKey
    val id: String,
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "" ,
    var confirmpass: String = ""
): Parcelable