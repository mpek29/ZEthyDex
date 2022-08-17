package fr.mpek29.zethydex

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.View
import fr.mpek29.zethydex.db.MySQLiteHelper

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */
class MyAdapter     //constructor
    (private var mCursor: Cursor?, private val rowLayout: Int, val mContext: Context) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myName: TextView
        var myDescription: TextView

        init {
            myName = itemView.findViewById(R.id.name)
            myDescription = itemView.findViewById(R.id.description)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(rowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor!!.moveToPosition(i)
        val currentZEthy = ZEthyModel()
        //Creation of the model
        currentZEthy.name =
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_NAME))
        currentZEthy.description=
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_DESCRIPTION))
        currentZEthy.apartment=
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_APARTMENT))
        currentZEthy.favoriteDrink=
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_FAVORITE_DRINK))

        viewHolder.myName.text =
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_NAME))
        viewHolder.myDescription.text =
            mCursor!!.getString(mCursor!!.getColumnIndex(MySQLiteHelper.KEY_DESCRIPTION))
        //itemView is the whole CardView, so it easy to a click listener.
        viewHolder.itemView.setOnClickListener {
            //Display of the ZEthyPopup
            ZEthyPopup(this, currentZEthy).show()
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (mCursor == null) 0 else mCursor!!.count
    }

    //change the cursor as needed and have the system redraw the data.
    @SuppressLint("NotifyDataSetChanged")
    fun setCursor(c: Cursor?) {
        mCursor = c
        notifyDataSetChanged()
    }
}