package com.example.petcarecjc.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class PetDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PetDetailScreen()
        }
    }
}

@Composable
fun PetDetailScreen() {
    Text(text = "Detalle de Mascota")
}
