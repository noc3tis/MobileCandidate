package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.viewModel.UserData
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel
import kotlin.collections.get


@Composable
fun HomeView(navController: NavController, viewModel: UsersViewModel){

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color(0xFF1E293B))
                    .padding(16.dp)
            ){
                Spacer(modifier = Modifier.height(30.dp))

                Text("Lista de usuarios:", color = Color.White, style = MaterialTheme.typography.titleLarge, fontFamily = FontFamily.Monospace)

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Button(
                        onClick = {
                            viewModel.performSearch("male")
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DA8DA))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Male,
                            contentDescription = "Hombres",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFFFFF)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.performSearch("female")
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF472B6))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Female,
                            contentDescription = "Mujeres",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFFFFF)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.performSearch()
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Reiniciar",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEE8C))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favoritos",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
        ) {
            val lista = viewModel.listaUsuarios

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (lista.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron usuarios.")
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(lista.size, ) { index ->
                        val usuario = lista[index]
                        Box(modifier = Modifier.clickable {

                        }) {
                            UserCard(usuario)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(usuario: UserData){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = usuario.name.title + " " + usuario.name.first + " " + usuario.name.last,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = usuario.email,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = usuario.gender,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
}
