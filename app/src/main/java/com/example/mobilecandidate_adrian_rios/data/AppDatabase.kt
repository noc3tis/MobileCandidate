package com.example.mobilecandidate_adrian_rios.data

import androidx.room.Database
import androidx.room.RoomDatabase

/*
 * Base de datos principal de Room.
 *
 * Actualmente solamente contiene la tabla de favoritos.
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /*
     * Permite acceder al DAO de usuarios.
     */
    abstract fun userDao(): UserDao
}