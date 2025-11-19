package com.olddragon.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.olddragon.app.data.database.dao.BattleDao
import com.olddragon.app.data.database.dao.CharacterDao
import com.olddragon.app.data.database.dao.EnemyDao
import com.olddragon.app.data.database.entities.BattleEntity
import com.olddragon.app.data.database.entities.CharacterEntity
import com.olddragon.app.data.database.entities.EnemyEntity

/**
 * Database principal do Old Dragon
 */
@Database(
    entities = [
        CharacterEntity::class,
        EnemyEntity::class,
        BattleEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OldDragonDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun enemyDao(): EnemyDao
    abstract fun battleDao(): BattleDao

    companion object {
        @Volatile
        private var INSTANCE: OldDragonDatabase? = null

        fun getDatabase(context: Context): OldDragonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OldDragonDatabase::class.java,
                    "old_dragon_database"
                )
                    .fallbackToDestructiveMigration() // Para desenvolvimento
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
