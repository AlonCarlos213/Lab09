package com.example.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenPosts(navController: NavHostController, servicio: PostApiService) {
    val listaPosts: SnapshotStateList<PostModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        try {
            val response = servicio.getUserPosts() // Cambiado para reflejar lista directa
            listaPosts.addAll(response) // Agregar los posts directamente
        } catch (e: Exception) {
            Log.e("ScreenPosts", "Error al obtener los posts: ${e.message}")
        }
    }

    LazyColumn {
        items(listaPosts) { item ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = item.id.toString(), Modifier.weight(0.05f), textAlign = TextAlign.End)
                Spacer(Modifier.padding(horizontal = 1.dp))
                Text(text = item.title, Modifier.weight(0.7f))
                IconButton(
                    onClick = {
                        navController.navigate("postsVer/${item.id}")
                        Log.e("POSTS", "ID = ${item.id}")
                    },
                    Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPost(servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }

    LaunchedEffect(Unit) {
        try {
            val xpost = servicio.getUserPostById(id)
            post = xpost
        } catch (e: Exception) {
            Log.e("ScreenPost", "Error al obtener el post: ${e.message}")
        }
    }

    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        post?.let {
            OutlinedTextField(
                value = it.id.toString(),
                onValueChange = {},
                label = { Text("id") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.userId.toString(),
                onValueChange = {},
                label = { Text("userId") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.title,
                onValueChange = {},
                label = { Text("title") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.body,
                onValueChange = {},
                label = { Text("body") },
                readOnly = true
            )
        } ?: run {
            Text("Error al cargar los detalles del post.")
        }
    }
}
