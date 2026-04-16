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

        setContent {
            MainScreen {
                startActivity(Intent(this, RegisterPetActivity::class.java))
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

        Text(
            text = "Bienvenido a \nPetCare CJC",
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
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comenzar")
        }
    }
}