package com.example.week3session1contactapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.week3session1contactapp.dbOps.ContactDatabase
import com.example.week3session1contactapp.dbOps.ContactRepository
import com.example.week3session1contactapp.entity.Contact
import com.example.week3session1contactapp.ui.theme.Week3Session1ContactAppTheme
import com.example.week3session1contactapp.viewmodels.ContactViewModel
import com.example.week3session1contactapp.viewmodels.ContactViewModelFactory

class UpdateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contactDatabase by lazy {
                Room.databaseBuilder(applicationContext, ContactDatabase::class.java, "contact_database")
                    .build()
            }

            val repository by lazy {
                ContactRepository(contactDatabase.contactDao())
            }

            val contactViewModel: ContactViewModel by viewModels {
                ContactViewModelFactory(repository)
            }

            val id = intent.getIntExtra("id", -1)

            UpdateActivityUI(contactViewModel, id)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateActivityUI(viewModel: ContactViewModel, id: Int) {

    val context = LocalContext.current

    var secondNameValue by remember {
        mutableStateOf("")
    }

    var numberValue by remember {
        mutableStateOf("")
    }

    var isFirstNameEmpty by remember {
        mutableStateOf(false)
    }

    var isSecondNameEmpty by remember {
        mutableStateOf(false)
    }

    var isNumberNameEmpty by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var firstNameValue by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = firstNameValue, onValueChange = {
            firstNameValue = it
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            label = {
                Text(text = "First Name")
            },
            isError = isFirstNameEmpty
        )

        OutlinedTextField(value = secondNameValue, onValueChange = {
            secondNameValue = it
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            label = {
                Text(text = "Second Name")
            },
            isError = isSecondNameEmpty
        )

        OutlinedTextField(value = numberValue, onValueChange = {
            numberValue = it
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            label = {
                Text(text = "Number")
            },
            isError = isNumberNameEmpty
        )

        Button(onClick = {
            // get all the data from outlined edit field and then create contact data class
            // and then add this contact to the listOfContacts

            isFirstNameEmpty = firstNameValue.isBlank()
            isSecondNameEmpty = secondNameValue.isBlank()
            isNumberNameEmpty = numberValue.isBlank()

            if(isFirstNameEmpty || isSecondNameEmpty || isNumberNameEmpty) {

            } else {
                val contact = Contact(id, firstName = firstNameValue, secondName = secondNameValue, number = numberValue)
                viewModel.updateContact(contact)

                (context as Activity).finish()

                firstNameValue = ""
                secondNameValue = ""
                numberValue = ""

                isFirstNameEmpty = false
                isSecondNameEmpty = false
                isNumberNameEmpty = false
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {
            Text(text = "Update Contact")
        }
    }
}