package com.example.mobilecandidate_adrian_rios.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object).
 *
 * Contiene las operaciones que permiten interactuar
 * con la tabla de favoritos.
 */
@Dao
interface UserDao {

    /*
     * Inserta un favorito.
     *
     * REPLACE permite reemplazar el registro si ya existe
     * un usuario con el mismo correo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(usuario: UserEntity)

    /*
     * Elimina un usuario de favoritos.
     */
    @Delete
    suspend fun eliminarFavorito(usuario: UserEntity)

    /*
     * Obtiene todos los favoritos.
     *
     * Flow permite observar automáticamente los cambios
     * realizados en la base de datos.
     */
    @Query("SELECT * FROM favoritos")
    fun obtenerFavoritos(): Flow<List<UserEntity>>

    /*
     * Comprueba si un usuario ya está registrado como favorito.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE email = :email)")
    suspend fun esFavorito(email: String): Boolean
}