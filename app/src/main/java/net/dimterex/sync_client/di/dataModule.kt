package net.dimterex.sync_client.di

import android.content.Context
import androidx.room.Room
import net.dimterex.sync_client.BuildConfig
import net.dimterex.sync_client.data.database.RoomAppDatabase
import org.kodein.di.Kodein
import org.kodein.di.erased.*

fun dataModule(appContext: Context) = Kodein.Module(name = "data") {

    // Room Database
    bind<RoomAppDatabase>() with singleton {
        Room
            .databaseBuilder(appContext, RoomAppDatabase::class.java, BuildConfig.DATABASE_NAME)
            .build()
    }
}