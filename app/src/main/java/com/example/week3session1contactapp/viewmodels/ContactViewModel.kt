package com.example.week3session1contactapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week3session1contactapp.dbOps.ContactRepository
import com.example.week3session1contactapp.entity.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    val contactListLD = MutableStateFlow<List<Contact>>(emptyList())

    init {
        getAllContacts()
    }

    fun getAllContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val allContacts = contactRepository.getAllData()
            contactListLD.value = allContacts
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.addContact(contact)
            getAllContacts()
        }
    }

    fun searchContact(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchedResult = contactRepository.searchContact(keyword)
            contactListLD.value = searchedResult
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.deleteContact(contact)
            getAllContacts()
        }
    }
}



