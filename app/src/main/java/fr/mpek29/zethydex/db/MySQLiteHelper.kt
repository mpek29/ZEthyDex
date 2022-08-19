package fr.mpek29.zethydex.db

import android.util.Log
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * basic class to create/update the database we are using.
 * This is used in our database class. The table name and column
 * names are defined here as Constants, so they can be used else where easily.
 */
class MySQLiteHelper  //required constructor with the super.
    : SupportSQLiteOpenHelper.Callback(DATABASE_VERSION) {
    override fun onCreate(db: SupportSQLiteDatabase) {
        //NOTE only called when the database is initial created!
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Called when the database version changes, Remember the constant from above.
        Log.w(
            TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data"
        )
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val TAG = "mySQLiteHelper"

        //column names for the table.
        const val KEY_IMAGE = "Image"
        const val KEY_NAME = "Name"
        const val KEY_DESCRIPTION = "Description"
        const val KEY_APARTMENT = "Apartment"
        const val KEY_FAVORITE_DRINK = "FavoriteDrink"
        const val KEY_ROWID = "_id" //required field for the cursorAdapter
        const val DATABASE_NAME = "ZEthyDex.db"
        const val TABLE_NAME = "ZEthy"
        private const val DATABASE_VERSION = 1

        // Database creation sql statement
        private const val DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ROWID + " integer PRIMARY KEY autoincrement," +  //this line is required for the cursorAdapter.
                KEY_IMAGE + " BLOB, " +
                KEY_NAME + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_APARTMENT + " TEXT, " +
                KEY_FAVORITE_DRINK + " TEXT);"
    }
}