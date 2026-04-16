package com.example.petcarecjc


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petcarecjc.ui.theme.PetCareCJCTheme
import com.example.petcarecjc.view.RegisterPetActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PetCareCJCTheme {
                MainScreen {
                    startActivity(Intent(this, RegisterPetActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun MainScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Bienvenido a PetCare CJC!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.pets), // imagen pets.png
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Cuidémoslos juntos")
        Text("Descubre como cuidar a tus mascotas")

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onStartClick) {
            Text("Comenzar")
        }
    }
}