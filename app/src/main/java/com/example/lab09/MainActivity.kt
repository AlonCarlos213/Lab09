package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.lab09.ui.theme.Lab09Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab09Theme {
                ProgPrincipal9()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgPrincipal9() {
    val urlBase = "https://dummyjson.com/" // Cambia esta URL base
    val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val postService = retrofit.create(PostApiService::class.java)
    val productService = retrofit.create(ProductApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues ->
            Contenido(paddingValues, navController, postService, productService)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "JSONPlaceHolder Access",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.LightGray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Posts") },
            label = { Text("Posts") },
            selected = navController.currentDestination?.route == "posts",
            onClick = { navController.navigate("posts") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = "Products") },
            label = { Text("Products") },
            selected = navController.currentDestination?.route == "products",
            onClick = { navController.navigate("products") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    postService: PostApiService,
    productService: ProductApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") { ScreenInicio() }
            composable("posts") { ScreenPosts(navController, postService) }
            composable("postsVer/{id}", arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )) {
                val postId = it.arguments?.getInt("id") ?: -1
                if (postId != -1) {
                    ScreenPost(postService, postId)
                } else {
                    Text("Error al cargar el post. ID inválido.")
                }
            }
            composable("products") { ScreenProducts(navController, productService) }
            composable("productDetail/{id}", arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )) {
                val productId = it.arguments?.getInt("id") ?: -1
                if (productId != -1) {
                    ScreenProductDetail(productId, productService)
                } else {
                    Text("Error al cargar el producto. ID inválido.")
                }
            }
        }
    }
}

@Composable
fun ScreenInicio() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("INICIO", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab09Theme {
        ProgPrincipal9()
    }
}

