package com.example.week3session1contactapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.week3session1contactapp.dbOps.ContactDatabase
import com.example.week3session1contactapp.dbOps.ContactRepository
import com.example.week3session1contactapp.entity.Contact
import com.example.week3session1contactapp.viewmodels.ContactViewModel
import com.example.week3session1contactapp.viewmodels.ContactViewModelFactory

class MainActivity : ComponentActivity() {
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

            MyApp(contactViewModel)

        }
    }
}

@Composable
fun MyApp(viewModel: ContactViewModel) {

    val context = LocalContext.current
    val listOfContact by viewModel.contactListLD.collectAsState()

    var searchValue by remember {
        mutableStateOf("")
    }

    val updateActivityLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        viewModel.getAllContacts()
    }

    Column {
        SearchBox(searchValue, {
            searchValue = it
            viewModel.searchContact(it)
        })
        ContactListLC(listOfContact, { contactToBeDeleted ->
            viewModel.deleteContact(contactToBeDeleted)
        }, {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", it.id)
            updateActivityLauncher.launch(intent)
        })
    }
    FabAddContact(viewModel)
}


@Composable
fun SearchBox(searchValue: String, onQueryChanged:(String) -> Unit) {
    OutlinedTextField(
        value = searchValue
        ,onValueChange = {  newTypedValue ->
            onQueryChanged(newTypedValue)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
        label = {
            Text(text = "Search Contact")
        },
        shape = RoundedCornerShape(28.dp),
        leadingIcon = {
            Image(painter = painterResource(id = R.drawable.ic_search), contentDescription = null,
                modifier = Modifier.size(32.dp))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FabAddContact(viewModel: ContactViewModel) {

    var secondNameValue by remember {
        mutableStateOf("")
    }

    var numberValue by remember {
        mutableStateOf("")
    }

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(onClick = {
            isBottomSheetOpen = true
        }, modifier = Modifier.padding(16.dp)) {
            Image(painter = painterResource(id = R.drawable.ic_plus), contentDescription = null)
        }
    }

    if(isBottomSheetOpen) {
        ModalBottomSheet(onDismissRequest = {
            isBottomSheetOpen = false
        },
            windowInsets = WindowInsets.ime,
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {

            Column(
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
                        val contact = Contact(firstName = firstNameValue, secondName = secondNameValue, number = numberValue)
                        viewModel.addContact(contact)
                        firstNameValue = ""
                        secondNameValue = ""
                        numberValue = ""
                        isBottomSheetOpen = false

                        isFirstNameEmpty = false
                        isSecondNameEmpty = false
                        isNumberNameEmpty = false
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                ) {
                    Text(text = "Create Contact")
                }
            }
        }
    }
}

@Composable
fun ContactListLC(listOfContacts: List<Contact>, onPerformDelete:(Contact) -> Unit, onContactClicked:(Contact) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(listOfContacts) {
            val currentContact = it
            SingleContactUi(currentContact, {onPerformDelete(currentContact)}, {onContactClicked(currentContact)})
        }
    }
}

@Composable
fun SingleContactUi(contact: Contact, performDelete:() -> Unit, onContactClicked: () -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onContactClicked()
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color.LightGray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.firstName.toUpperCase().get(0).toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${contact.firstName} ${contact.secondName}",
                    style = TextStyle(
                        color = Color.Black, fontSize = 20.sp, fontFamily = fontFamilyRoboto
                    ),
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = contact.number,
                    style = TextStyle(
                        color = Color.Black, fontSize = 16.sp, fontFamily = fontFamilyRoboto
                    ),
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                )
            }

            Image(painter = painterResource(id = R.drawable.ic_delete), contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        performDelete()
                    }
                , alignment = Alignment.CenterEnd
            )
        }
    }

}

val fontFamilyRoboto = FontFamily(
    Font(R.font.font_roboto, FontWeight.Normal)
)