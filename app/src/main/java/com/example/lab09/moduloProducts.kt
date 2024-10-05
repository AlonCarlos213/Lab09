package com.example.lab09

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import android.util.Log

@Composable
fun ScreenProducts(navController: NavHostController, servicio: ProductApiService) {
    val productList: SnapshotStateList<ProductModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val response = servicio.getProducts() // Llama al nuevo método que devuelve `ProductResponse`
            productList.addAll(response.products) // Agrega los productos desde la lista dentro de `ProductResponse`
        } catch (e: Exception) {
            Log.e("ScreenProducts", "Error al obtener los productos: ${e.message}")
        }
    }

    LazyColumn {
        items(productList) { product ->
            ProductItem(product = product, onClick = {
                navController.navigate("productDetail/${product.id}")
            })
        }
    }
}

@Composable
fun ProductItem(product: ProductModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // Mostrar imagen de producto usando `AsyncImage` en lugar de `rememberImagePainter`
        AsyncImage(
            model = product.thumbnail,
            contentDescription = "Product Image",
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = product.title, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Price: $${product.price}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Rating: ${product.rating}", style = MaterialTheme.typography.bodySmall)

            // Botón para ver más detalles del producto
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "Details")
            }
        }
    }
}
