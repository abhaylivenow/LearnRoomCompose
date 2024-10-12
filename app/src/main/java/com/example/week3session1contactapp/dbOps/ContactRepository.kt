package com.example.week3session1contactapp.dbOps

import com.example.week3session1contactapp.entity.Contact

class ContactRepository(
    private val contactDao: ContactDao
) {

    suspend fun getAllData(): List<Contact> = contactDao.getAllContact()

    suspend fun addContact(contact: Contact) = contactDao.addContact(contact)

    suspend fun searchContact(keyword: String) = contactDao.searchContact(keyword)

    suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)
}