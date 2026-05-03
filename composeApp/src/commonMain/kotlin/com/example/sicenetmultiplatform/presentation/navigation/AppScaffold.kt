package com.example.sicenetmultiplatform.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sicenetmultiplatform.SessionManager
import kotlinx.coroutines.launch

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFF4CAF50)
private val GreenDark    = Color(0xFF1B5E20)

/**
 * Scaffold principal con TopAppBar y DrawerNavigation.
 * Reemplaza AppScaffold.kt del proyecto Android original.
 *
 * @author Erick Omar Pérez González
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    title: String = "SICENET",
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onNavigate = { route ->
                        scope.launch { drawerState.close() }
                        if (route == Routes.LOGIN) {
                            showLogoutDialog = true
                        } else {
                            navController.navigate(route) {
                                popUpTo(Routes.PERFIL) { inclusive = false }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor     = GreenPrimary,
                        titleContentColor  = Color.White
                    )
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    }

    // Diálogo de cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            },
            text = { Text("¿Está seguro que desea cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    SessionManager.cerrarSesion()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text("Cerrar Sesión", color = GreenPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        )
    }
}

// ── Contenido del Drawer ───────────────────────────────────────────
@Composable
private fun DrawerContent(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header con gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GreenDark, GreenPrimary, GreenLight)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "SICENET",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 28.sp
                )
                Text(
                    text = "Sistema de Consulta",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem(Icons.Default.Person,     "Perfil",           { onNavigate(Routes.PERFIL) })
        DrawerItem(Icons.Default.School,     "Carga Académica",  { onNavigate(Routes.CARGA) })
        DrawerItem(Icons.Default.Assignment, "Cardex",           { onNavigate(Routes.CARDEX) })
        DrawerItem(Icons.Default.Grade,      "Calificaciones",   { onNavigate(Routes.CALIFICACIONES) })

        Spacer(modifier = Modifier.weight(1f))

        Divider(
            color = GreenLight.copy(alpha = 0.3f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        DrawerItem(
            icon    = Icons.Default.ExitToApp,
            texto   = "Cerrar Sesión",
            onClick = { onNavigate(Routes.LOGIN) },
            isLogout = true
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    texto: String,
    onClick: () -> Unit,
    isLogout: Boolean = false
) {
    val textColor = if (isLogout) Color(0xFFD32F2F) else Color.Black
    val iconColor = if (isLogout) Color(0xFFD32F2F) else GreenPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector    = icon,
            contentDescription = null,
            tint           = iconColor,
            modifier       = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text       = texto,
            fontSize   = 16.sp,
            fontWeight = FontWeight.Medium,
            color      = textColor
        )
    }
}