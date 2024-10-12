package com.example.week3session1contactapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val secondName: String,
    val number: String
)