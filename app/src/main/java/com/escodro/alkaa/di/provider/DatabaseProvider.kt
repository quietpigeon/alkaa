package com.escodro.alkaa.di.provider

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.getStringColor
import com.escodro.alkaa.data.local.TaskDatabase
import com.escodro.alkaa.data.local.model.Category
import java.util.concurrent.Executors

/**
 * Repository with the [Room] database.
 */
class DatabaseProvider(private val context: Context) {

    private var database: TaskDatabase? = null

    /**
     * Gets an instance of [TaskDatabase].
     *
     * @return an instance of [TaskDatabase]
     */
    fun getInstance(): TaskDatabase =
        database ?: synchronized(this) {
            database ?: buildDatabase().also { database = it }
        }

    private fun buildDatabase(): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "todo-db")
            .addCallback(onCreateDatabase())
            .build()

    private fun onCreateDatabase() =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    database?.categoryDao()?.insertCategory(getDefaultCategoryList())
                }
            }
        }

    private fun getDefaultCategoryList() =
        listOf(
            Category(
                context.getString(R.string.category_default_personal),
                context.getStringColor(R.color.blue)
            ),
            Category(
                context.getString(R.string.category_default_work),
                context.getStringColor(R.color.green)
            ),
            Category(
                context.getString(R.string.category_default_shopping),
                context.getStringColor(R.color.orange)
            )
        )
}
