package com.example.mobilecandidate_adrian_rios.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entidad de Room utilizada para guardar los usuarios favoritos.
 *
 * El correo electrónico funciona como identificador único del usuario.
 */
@Entity(tableName = "favoritos")
data class UserEntity(

    /*
     * Cada usuario tendrá un correo único dentro de la tabla.
     */
    @PrimaryKey
    val email: String,

    val title: String,
    val firstName: String,
    val lastName: String,
    val gender: String
)