package com.example.sicenetmultiplatform.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

lateinit var appContext: Context

actual fun getDatabaseBuilder(): SicenetDatabase {
    val dbFile = appContext.getDatabasePath("sicenet.db")
    return Room.databaseBuilder<SicenetDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}