package com.example.levelup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.levelup.ui.screens.auth.LoginScreen
import com.example.levelup.ui.screens.auth.RegisterScreen
import com.example.levelup.ui.screens.products.ProductCatalogScreen
import com.example.levelup.ui.screens.home.HomeScreen
import com.example.levelup.ui.screens.cart.CartScreen
import com.example.levelup.ui.screens.profile.ProfileScreen
import com.example.levelup.ui.viewmodel.CartViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object ProductCatalog : Screen("product_catalog/{category}") {
        fun createRoute(category: String?) = "product_catalog/${category ?: "all"}"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")

}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Home.route) {
    // Crear el CartViewModel compartido usando el ViewModelStoreOwner de la actividad
    val context = LocalContext.current
    val activity = context as? androidx.activity.ComponentActivity
    val cartViewModel: CartViewModel = if (activity != null) {
        viewModel(
            viewModelStoreOwner = activity
        )
    } else {
        viewModel() // Fallback si no es una ComponentActivity
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToCategory = { category ->
                    navController.navigate(Screen.ProductCatalog.createRoute(category))
                },
                cartViewModel = cartViewModel
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Navegar al Home después del login exitoso
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navegar al Home después del registro exitoso
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.ProductCatalog.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val categoryFilter = if (category == "all") null else category
            ProductCatalogScreen(
                category = categoryFilter,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ProductCatalog.route) { inclusive = true }
                    }
                },
                cartViewModel = cartViewModel
            )
        }
        
        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                cartViewModel = cartViewModel
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = false }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Profile.route) { inclusive = false }
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }
        
    }
}

