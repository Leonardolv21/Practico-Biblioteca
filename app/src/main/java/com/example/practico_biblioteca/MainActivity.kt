package com.example.practico_biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practico_biblioteca.ui.NavScreens
import com.example.practico_biblioteca.ui.screens.GeneroFormScreen
import com.example.practico_biblioteca.ui.screens.LibroDetailScreen
import com.example.practico_biblioteca.ui.screens.LibroFormScreen
import com.example.practico_biblioteca.ui.screens.LibroListScreen
import com.example.practico_biblioteca.ui.theme.PracticoBibliotecaTheme
import com.example.practico_biblioteca.viewmodels.LibroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticoBibliotecaTheme {
                NavigationApp()
            }
        }
    }

    @Composable
    fun NavigationApp(
        navController: NavHostController = rememberNavController(),
        vm: LibroViewModel = viewModel()
    ) {
        NavHost(
            navController = navController,
            startDestination = NavScreens.HOME.name
        ) {
            composable(NavScreens.HOME.name) {
                LibroListScreen(
                    viewModel = vm,
                    onLibroClick = { id -> navController.navigate("${NavScreens.DETAIL.name}/$id") },
                    onCrearClick = { navController.navigate(NavScreens.CREATE.name) }
                )
            }

            composable(route = "${NavScreens.DETAIL.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                LibroDetailScreen(
                    libroId = id,
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onEditarClick = { libroId -> navController.navigate("${NavScreens.EDIT.name}/$libroId") },
                    onEliminarClick = { navController.popBackStack() }
                )
            }

            composable(NavScreens.CREATE.name) {
                LibroFormScreen(
                    libroId = null,
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onCrearGeneroClick = { navController.navigate(NavScreens.GENERO_CREATE.name) }
                )
            }

            composable(
                route = "${NavScreens.EDIT.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                LibroFormScreen(
                    libroId = id,
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onCrearGeneroClick = { navController.navigate(NavScreens.GENERO_CREATE.name) }
                )
            }
            composable(NavScreens.GENERO_CREATE.name) {
                GeneroFormScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}