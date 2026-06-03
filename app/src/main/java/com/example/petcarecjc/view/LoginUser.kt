package com.example.petcarecjc.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.petcarecjc.R
import com.example.petcarecjc.notifications.NotificationHelper
import com.google.firebase.auth.FirebaseAuth

class LoginUser : ComponentActivity() {

    private val auth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannels(this)

        if (auth.currentUser != null) {
            goToMain(auth.currentUser!!.email ?: "Usuario")
            return
        }

        setContent {
            LoginScreen(onLoginSuccess = { username -> goToMain(username) })
        }
    }

    private fun goToMain(username: String) {
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra("USERNAME", username)
        })
        finish()
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {

    var email         by remember { mutableStateOf("") }
    var password      by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var isLoading     by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth    = FirebaseAuth.getInstance()

    Column(
        modifier            = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter            = painterResource(id = R.drawable.pets),
            contentDescription = "Logo",
            modifier           = Modifier.size(220.dp).clip(RoundedCornerShape(20.dp)),
            contentScale       = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text  = if (isRegistering) stringResource(R.string.create_account)
            else stringResource(R.string.login),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value         = email,
            onValueChange = { email = it },
            label         = { Text(stringResource(R.string.user_email)) },
            leadingIcon   = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier      = Modifier.fillMaxWidth(),
            shape         = RoundedCornerShape(12.dp),
            singleLine    = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value                = password,
            onValueChange        = { password = it },
            label                = { Text(stringResource(R.string.password)) },
            leadingIcon          = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier             = Modifier.fillMaxWidth(),
            shape                = RoundedCornerShape(12.dp),
            singleLine           = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context,
                        context.getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                if (isRegistering) {
                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context,
                                context.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                            onLoginSuccess(email.trim())
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            val msg = when {
                                e.message?.contains("email address is already") == true ->
                                    context.getString(R.string.err_email_already)
                                e.message?.contains("badly formatted") == true ->
                                    context.getString(R.string.err_email_invalid)
                                e.message?.contains("least 6 characters") == true ->
                                    context.getString(R.string.err_password_short)
                                else -> context.getString(R.string.err_login_generic)
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                } else {
                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context,
                                context.getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                            onLoginSuccess(email.trim())
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            val msg = when {
                                e.message?.contains("no user record") == true ||
                                        e.message?.contains("identifier") == true ->
                                    context.getString(R.string.err_email_not_found)
                                e.message?.contains("password is invalid") == true ||
                                        e.message?.contains("credential") == true ->
                                    context.getString(R.string.err_password_wrong)
                                else -> context.getString(R.string.err_login_generic)
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape    = RoundedCornerShape(12.dp),
            enabled  = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color       = MaterialTheme.colorScheme.onPrimary,
                    modifier    = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    if (isRegistering) stringResource(R.string.register)
                    else stringResource(R.string.login_btn)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isRegistering = !isRegistering }) {
            Text(
                if (isRegistering) stringResource(R.string.have_account)
                else stringResource(R.string.no_account)
            )
        }
    }
}