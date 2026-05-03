package com.example.sicenetmultiplatform.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sicenetmultiplatform.presentation.viewmodel.LoginViewModel

// Paleta de colores
private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)
private val GreenDark    = Color(0xFF1B5E20)

/**
 * Pantalla de autenticación del alumno.
 * Maneja login online y offline.
 * Basada en LoginScreen.kt del proyecto Android original.
 *
 * @author
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    var usuario  by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenDark, GreenPrimary, GreenLight)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Título
                    Text(
                        text = "SICENET",
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        fontSize = 42.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Bienvenido",
                        color = GreenPrimary.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo usuario
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = {
                            usuario = it
                            viewModel.limpiarError()
                        },
                        label = { Text("Usuario") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Usuario",
                                tint = GreenPrimary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GreenPrimary,
                            unfocusedBorderColor = GreenLight.copy(alpha = 0.5f),
                            focusedLabelColor    = GreenPrimary,
                            cursorColor          = GreenPrimary
                        ),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            viewModel.limpiarError()
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Contraseña",
                                tint = GreenPrimary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GreenPrimary,
                            unfocusedBorderColor = GreenLight.copy(alpha = 0.5f),
                            focusedLabelColor    = GreenPrimary,
                            cursorColor          = GreenPrimary
                        ),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    // Mensaje de error
                    error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = Color(0xFFD32F2F),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    //Botón login
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        onClick = {
                            viewModel.login(
                                usuario  = usuario,
                                password = password,
                                onSuccess = onLoginSuccess
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation  = 4.dp,
                            pressedElevation  = 8.dp
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Iniciar sesión",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}