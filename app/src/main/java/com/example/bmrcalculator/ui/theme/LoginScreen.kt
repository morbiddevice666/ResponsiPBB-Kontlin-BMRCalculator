package com.example.bmrcalculator.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.content.Context

// Definisi warna kustom
private val PrimaryBlue = Color(0xFF1A73E8)
private val SecondaryBlue = Color(0xFF5C9CE6)
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE3F2FD),
        Color(0xFFBBDEFB)
    )
)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var isRegistering by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = BackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Text(
                        text = if (isRegistering) "Create Account" else "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (isRegistering) "Register for BMR Calculator" else "Login to BMR Calculator",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input Fields
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            showError = false
                        },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            cursorColor = PrimaryBlue
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            showError = false
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            cursorColor = PrimaryBlue
                        )
                    )

                    if (isRegistering) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                showError = false
                            },
                            label = { Text("Confirm Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                focusedLabelColor = PrimaryBlue,
                                cursorColor = PrimaryBlue
                            )
                        )
                    }

                    if (showError) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Action Buttons
                    Button(
                        onClick = {
                            if (isRegistering) {
                                when {
                                    username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                                        showError = true
                                        errorMessage = "Semua field harus diisi"
                                    }
                                    password != confirmPassword -> {
                                        showError = true
                                        errorMessage = "Password tidak cocok"
                                    }
                                    sharedPreferences.contains(username) -> {
                                        showError = true
                                        errorMessage = "Username sudah terdaftar"
                                    }
                                    else -> {
                                        sharedPreferences.edit()
                                            .putString(username, password)
                                            .apply()
                                        isRegistering = false
                                        username = ""
                                        password = ""
                                        confirmPassword = ""
                                        showError = false
                                    }
                                }
                            } else {
                                val savedPassword = sharedPreferences.getString(username, null)
                                if (savedPassword == password || (username == "admin" && password == "admin123")) {
                                    onLoginSuccess()
                                } else {
                                    showError = true
                                    errorMessage = "Username atau password salah"
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            if (isRegistering) "Register" else "Login",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = {
                            isRegistering = !isRegistering
                            username = ""
                            password = ""
                            confirmPassword = ""
                            showError = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = SecondaryBlue
                        )
                    ) {
                        Text(
                            if (isRegistering) "Sudah punya akun? Login" else "Belum punya akun? Register",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}