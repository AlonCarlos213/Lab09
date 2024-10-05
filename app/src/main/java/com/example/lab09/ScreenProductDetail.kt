package com.example.lab09

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun ScreenProductDetail(id: Int, servicio: ProductApiService) {
    var product by remember { mutableStateOf<ProductModel?>(null) }

    // Obtener detalles del producto desde la API
    LaunchedEffect(Unit) {
        try {
            product = servicio.getProductById(id)
        } catch (e: Exception) {
            println("Error al obtener los detalles del producto: ${e.message}")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        product?.let {
            // Mostrar imagen del producto usando AsyncImage para Coil
            AsyncImage(
                model = it.thumbnail,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text("ID: ${it.id}")
            Text("Title: ${it.title}")
            Text("Price: ${it.price}")
            Text("Discount: ${it.discountPercentage}%")
            Text("Description: ${it.description}")
            Text("Brand: ${it.brand}")
            Text("Category: ${it.category}")
            Text("Stock Available: ${it.stock}")
        }
    }
}
