package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel

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
                }


            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
        ) {

        }
    }
}