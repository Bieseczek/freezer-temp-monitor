package com.example.projektzespoowy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projektzespoowy.ui.theme.ProjektzespołowyTheme
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val TempC = mutableStateOf("")
    private val Limit = mutableStateOf("")
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Temp_C")
    private val limitRef = database.getReference("date")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjektzespołowyTheme {
                TemperatureScreen(TempC.value,Limit.value)
            }
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Listen for temperature updates
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                TempC.value = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read TempC: ${error.toException()}")
            }
        })

        // Listen for limit updates
        limitRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Limit.value = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read limit: ${error.toException()}")
            }
        })

        // Get Firebase Cloud Messaging token
        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val deviceId = task.result
                    Log.d("Device ID", deviceId ?: "No Token Found")
                } else {
                    Log.e("Firebase", "Failed to get token: ${task.exception}")
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen( TempC:String,Limit:String) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text("Temperature: ${TempC}°C", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ostatnia atualizacja:${Limit}")
        }
    }
}
