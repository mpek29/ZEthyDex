package fr.mpek29.zethydex.db

import android.annotation.SuppressLint
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
        initialValues.put(MySQLiteHelper.KEY_IMAGE, zethy.image)
        initialValues.put(MySQLiteHelper.KEY_NAME, zethy.name)
        initialValues.put(MySQLiteHelper.KEY_DESCRIPTION, zethy.description)
        initialValues.put(MySQLiteHelper.KEY_APARTMENT, zethy.apartment)
        initialValues.put(MySQLiteHelper.KEY_FAVORITE_DRINK, zethy.favoriteDrink)
        return db.insert(MySQLiteHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_FAIL, initialValues)
    }

    /**
     * The following a different ways to query the database
     */


    //For obtain the last entry
    val lastCaptured: ArrayList<ZEthyModel>
        @SuppressLint("Range")
        get() {
            //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORT BY KEY_NAME;
            val mCursor = qbQuery(arrayOf(                   //projection, ie columns.
                MySQLiteHelper.KEY_ROWID,
                MySQLiteHelper.KEY_IMAGE,
                MySQLiteHelper.KEY_NAME,
                MySQLiteHelper.KEY_DESCRIPTION,
                MySQLiteHelper.KEY_APARTMENT,
                MySQLiteHelper.KEY_FAVORITE_DRINK,
            ),MySQLiteHelper.KEY_ROWID
            )
            mCursor.moveToLast() //Read by the end for begin by the last entry
            val zethyList = ArrayList<ZEthyModel>()
            if(mCursor.count > 0){
                do {
                    zethyList.add(ZEthyModel(
                        mCursor.getInt(mCursor.getColumnIndex(MySQLiteHelper.KEY_ROWID)),
                        mCursor.getBlob(mCursor.getColumnIndex(MySQLiteHelper.KEY_IMAGE)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_DESCRIPTION)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_APARTMENT)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_FAVORITE_DRINK))
                    ))
                }while (mCursor.moveToPrevious())
            }
            return zethyList
        }

    //For obtain entry order by the alphabet
    val alphabeticalOrder: ArrayList<ZEthyModel>
        @SuppressLint("Range")
        get() {
            //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORT BY KEY_NAME;
            val mCursor = qbQuery(arrayOf(                   //projection, ie columns.
                MySQLiteHelper.KEY_ROWID,
                MySQLiteHelper.KEY_IMAGE,
                MySQLiteHelper.KEY_NAME,
                MySQLiteHelper.KEY_DESCRIPTION,
                MySQLiteHelper.KEY_APARTMENT,
                MySQLiteHelper.KEY_FAVORITE_DRINK,
            ),MySQLiteHelper.KEY_NAME
            )
            mCursor.moveToFirst()
            val zethyList = ArrayList<ZEthyModel>()
            if(mCursor.count > 0){
                do {
                    zethyList.add(ZEthyModel(
                        mCursor.getInt(mCursor.getColumnIndex(MySQLiteHelper.KEY_ROWID)),
                        mCursor.getBlob(mCursor.getColumnIndex(MySQLiteHelper.KEY_IMAGE)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_DESCRIPTION)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_APARTMENT)),
                        mCursor.getString(mCursor.getColumnIndex(MySQLiteHelper.KEY_FAVORITE_DRINK))
                    ))
                }while (mCursor.moveToNext())
            }
            return zethyList
        }

    //Use of the supportQueryBuilder that build a SupportSQLiteQuery for create a query.
    private fun qbQuery(projection: Array<String?>?,orderBy: String): Cursor {
        val qb = SupportSQLiteQueryBuilder.builder(MySQLiteHelper.TABLE_NAME)
        qb.columns(projection)
        qb.selection(null, null)
        qb.orderBy(orderBy)
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create())
    }

    /**
     * The following is a delete method
     */
    fun deleteZethy(zethy: ZEthyModel){
        val selection = "${MySQLiteHelper.KEY_ROWID} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(zethy.id)
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