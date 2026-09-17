package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.data.UserEntity
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel

/*
 * Pantalla que muestra los usuarios favoritos.
 *
 * Los datos provienen de Room mediante el ViewModel.
 */
@Composable
fun FavoritesView(
    navController: NavController,
    viewModel: UsersViewModel
) {

    /*
     * Lista de favoritos almacenados.
     */
    val favoritos = viewModel.listaFavoritos

    Scaffold(

        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B))
                    .padding(
                        top = 30.dp,
                        start = 8.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Regresar a Home.
                 */
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Favoritos",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

    ) { paddingValues ->

        /*
         * Si no existen favoritos,
         * se muestra un mensaje.
         */
        if (favoritos.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "No tienes usuarios favoritos."
                )
            }

        } else {

            /*
             * Lista de favoritos.
             */
            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0))
                    .padding(paddingValues),

                contentPadding =
                    PaddingValues(16.dp)
            ) {

                items(

                    items = favoritos,

                    /*
                     * El correo identifica de manera única
                     * al favorito.
                     */
                    key = { favorito ->
                        favorito.email
                    }

                ) { favorito ->

                    FavoriteCard(

                        favorito = favorito,

                        /*
                         * Eliminar favorito.
                         */
                        onDelete = {
                            viewModel.eliminarDeFavoritos(
                                favorito
                            )
                        },

                        /*
                         * Abrir información detallada.
                         */
                        onClick = {

                            val usuario =
                                viewModel.obtenerUsuarioPorEmail(
                                    favorito.email
                                )

                            if (usuario != null) {

                                viewModel.seleccionarUsuario(
                                    usuario
                                )

                                navController.navigate(
                                    "UserDetail"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

/*
 * Tarjeta individual de un favorito.
 */
@Composable
fun FavoriteCard(
    favorito: UserEntity,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "${favorito.title} " +
                                    "${favorito.firstName} " +
                                    "${favorito.lastName}",

                        style =
                            MaterialTheme.typography.titleSmall,

                        color = Color.Gray,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = favorito.email,

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color = Color.Gray
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = favorito.gender,

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color = Color.Gray
                    )
                }

                /*
                 * Botón para eliminar el favorito.
                 */
                Button(

                    onClick = onDelete,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFFF87171)
                        ),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Delete,

                        contentDescription =
                            "Eliminar favorito",

                        tint = Color.White
                    )
                }
            }
        }
    }
}