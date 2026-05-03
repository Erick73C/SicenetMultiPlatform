package com.example.sicenetmultiplatform.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sicenetmultiplatform.SessionManager
import com.example.sicenetmultiplatform.data.di.AppContainer
import com.example.sicenetmultiplatform.presentation.screens.CalificacionesScreen
import com.example.sicenetmultiplatform.presentation.screens.CardexScreen
import com.example.sicenetmultiplatform.presentation.screens.CargaAcademicaScreen
import com.example.sicenetmultiplatform.presentation.screens.LoginScreen
import com.example.sicenetmultiplatform.presentation.screens.PerfilScreen
import com.example.sicenetmultiplatform.presentation.viewmodel.CalificacionesViewModel
import com.example.sicenetmultiplatform.presentation.viewmodel.CardexViewModel
import com.example.sicenetmultiplatform.presentation.viewmodel.CargaAcademicaViewModel
import com.example.sicenetmultiplatform.presentation.viewmodel.LoginViewModel
import com.example.sicenetmultiplatform.presentation.viewmodel.PerfilViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // Login
        composable(Routes.LOGIN) {
            val viewModel = remember {
                LoginViewModel(
                    networkRepository = AppContainer.networkRepository,
                    localRepository   = AppContainer.localRepository
                )
            }

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.PERFIL) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Perfil
        composable(Routes.PERFIL) {
            val viewModel = remember {
                PerfilViewModel(
                    localRepository = AppContainer.localRepository,
                    matricula       = SessionManager.matricula
                )
            }
            AppScaffold(navController = navController) {
                PerfilScreen(viewModel = viewModel)
            }
        }

        // Carga Académica
        composable(Routes.CARGA) {
            val viewModel = remember {
                CargaAcademicaViewModel(
                    localRepository   = AppContainer.localRepository,
                    networkRepository = AppContainer.networkRepository,
                    matricula         = SessionManager.matricula
                )
            }
            AppScaffold(navController = navController) {
                CargaAcademicaScreen(viewModel = viewModel)
            }
        }

        // Cardex
        composable(Routes.CARDEX) {
            val viewModel = remember {
                CardexViewModel(
                    localRepository   = AppContainer.localRepository,
                    networkRepository = AppContainer.networkRepository
                )
            }
            AppScaffold(navController = navController) {
                CardexScreen(viewModel = viewModel)
            }
        }

        // Calificaciones
        composable(Routes.CALIFICACIONES) {
            val viewModel = remember {
                CalificacionesViewModel(
                    localRepository   = AppContainer.localRepository,
                    networkRepository = AppContainer.networkRepository,
                    matricula         = SessionManager.matricula
                )
            }
            AppScaffold(navController = navController) {
                CalificacionesScreen(viewModel = viewModel)
            }
        }
    }
}