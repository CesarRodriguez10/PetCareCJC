package com.example.petcarecjc.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petcarecjc.R

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("USERNAME") ?: "Usuario"

        setContent {
            MainScreen(
                username = username,
                onRegisterClick = {
                    startActivity(Intent(this, RegisterPetActivity::class.java))
                },
                onViewPetsClick = {
                    startActivity(Intent(this, PetDetailActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    username: String,
    onRegisterClick: () -> Unit,
    onViewPetsClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Hola, $username\nBienvenido a PetCare CJC",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.pets),
            contentDescription = "Mascotas",
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Cuidémolos juntos",
            style = MaterialTheme.typography.headlineSmall
        )
        Text("Descubre como cuidar a tus mascotas")

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Registrar Mascota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onViewPetsClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Ver mis Mascotas")
        }
    }
}