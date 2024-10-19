package com.example.week3session1contactapp.dbOps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.week3session1contactapp.entity.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM my_table")
    suspend fun getAllContact() : List<Contact>

    @Insert
    suspend fun addContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM my_table WHERE firstName LIKE '%' || :keyword || '%' OR secondName LIKE '%' || :keyword || '%' OR number LIKE '%' || :keyword || '%'")
    suspend fun searchContact(keyword: String): List<Contact>

    @Update
    suspend fun updateContact(contact: Contact)
}