package com.example.sicenetmultiplatform.data.local.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun getDatabaseBuilder(): SicenetDatabase {
    // Guarda la BD en la carpeta del usuario en Desktop
    val dbFile = File(System.getProperty("user.home"), "sicenet.db")
    return Room.databaseBuilder<SicenetDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}