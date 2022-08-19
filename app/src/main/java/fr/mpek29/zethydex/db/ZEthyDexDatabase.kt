package fr.mpek29.zethydex.db

import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import fr.mpek29.zethydex.ZEthyModel
import java.io.IOException

/**
 * This an accessor class that do all the work in the database.  This is the object that the
 * system uses to access/insert/update/etc the database.
 *
 *
 * This provides a number of methods as examples for different things.
 * It also provides the accessor methods for the content provider as well.
 */
class ZEthyDexDatabase(ctx: Context?) {
    private val helper: SupportSQLiteOpenHelper
    private lateinit var db: SupportSQLiteDatabase

    //---opens the database---
    @Throws(SQLException::class)
    fun open() {
        db = helper.writableDatabase
    }

    //returns true if db is open.  Helper method.
    @get:Throws(SQLException::class)
    val isOpen: Boolean
        get() = db.isOpen

    //---closes the database---
    fun close() {
        try {
            db.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * insert methods.
     */
    fun insertZethy(zethy: ZEthyModel): Long {
        val initialValues = ContentValues()
        initialValues.put(MySQLiteHelper.KEY_NAME, zethy.name)
        initialValues.put(MySQLiteHelper.KEY_DESCRIPTION, zethy.description)
        initialValues.put(MySQLiteHelper.KEY_APARTMENT, zethy.apartment)
        initialValues.put(MySQLiteHelper.KEY_FAVORITE_DRINK, zethy.favoriteDrink)
        //initialValues.put(mySQLiteHelper.KEY_IMAGE, zethy.image)
        return db.insert(MySQLiteHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_FAIL, initialValues)
    }

    /**
     * The following a different ways to query the database.  They all return a Cursor.
     */
    //get all the rows.
    val allNames: Cursor
        get() {
            //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORT BY KEY_NAME;
            val mCursor = qbQuery(arrayOf(                   //projection, ie columns.
                    MySQLiteHelper.KEY_ROWID,
                MySQLiteHelper.KEY_NAME,
                MySQLiteHelper.KEY_DESCRIPTION,
                MySQLiteHelper.KEY_APARTMENT,
                MySQLiteHelper.KEY_FAVORITE_DRINK,
                ))
            mCursor.moveToFirst()
            return mCursor
        }

    //this one uses the supportQueryBuilder that build a SupportSQLiteQuery for the query.
    private fun qbQuery(projection: Array<String?>?): Cursor {
        val qb = SupportSQLiteQueryBuilder.builder(MySQLiteHelper.TABLE_NAME)
        qb.columns(projection)
        qb.selection(null, null)
        qb.orderBy(MySQLiteHelper.KEY_NAME)
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create())
    }

    /**
     * The following are delete methods
     */
    fun deleteZethy(zethy: ZEthyModel){
        val selection = "${MySQLiteHelper.KEY_NAME} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(zethy.name)
        // Issue SQL statement.
        db.delete(MySQLiteHelper.TABLE_NAME, selection, selectionArgs)

    }

    //constructor
    init {
        val factory: SupportSQLiteOpenHelper.Factory = FrameworkSQLiteOpenHelperFactory()
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(
            ctx!!
        )
            .name(MySQLiteHelper.DATABASE_NAME)
            .callback(MySQLiteHelper())
            .build()
        helper = factory.create(configuration)
    }
}