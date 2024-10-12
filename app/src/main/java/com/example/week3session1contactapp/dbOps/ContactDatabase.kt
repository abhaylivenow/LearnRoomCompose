package com.example.week3session1contactapp.dbOps

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.week3session1contactapp.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao() : ContactDao
}