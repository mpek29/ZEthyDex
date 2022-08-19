package fr.mpek29.zethydex

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 * this is a classical adapter create for manage a recycler view and a loadingBar
 */
class MyAdapter     //constructor
    (
    private val type: String,
    private val zethyList: ArrayList<ZEthyModel>,
    private val rowLayout: Int,
    val mContext: Context,
    private val determinateBar: ProgressBar?,
    private val mPercentText: TextView?,
    private val zethyMembers: Int?
) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Recovery of the different element of the layout
        val zethyImage:ImageView?  = itemView.findViewById(R.id.image)
        val zethyName:TextView?  = itemView.findViewById(R.id.name)
        val zethyDescription:TextView?  = itemView.findViewById(R.id.description)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(rowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range", "NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        val currentZEthy = zethyList[position]
        //Creation of the model
        holder.zethyImage?.setImageDrawable(BitmapDrawable(bytes2Bitmap(currentZEthy.image)))
        holder.zethyName?.text = currentZEthy.name
        holder.zethyDescription?.text = currentZEthy.description

        // Management of the LoadingBar
        if(determinateBar != null && mPercentText != null && zethyMembers != null){
            val percent: Int = if(zethyList.size * 100/zethyMembers<100){
                zethyList.size * 100/zethyMembers
            }else{
                100
            }
            determinateBar.progress = percent
            mPercentText.text = "$percent%"
        }

        //itemView is the whole CardView, so it easy to a click listener.
        holder.itemView.setOnClickListener {
            //Display of the ZEthyPopup
            ZEthyPopup(zethyList,this,position, determinateBar,mPercentText, zethyMembers).show()
        }
    }

    //converting of byte array to a bitmap
    private fun bytes2Bitmap(b: ByteArray): Bitmap? {
        return if (b.isNotEmpty()) {
            BitmapFactory.decodeByteArray(b, 0, b.size)
        } else null
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount(): Int {
        //if we are in the Home fragment we don't want a recycler view biggest than 5 items
        return if(type=="Home"){
            if(zethyList.size<5){
                zethyList.size
            }else{
                5
            }
        }else{
            zethyList.size
        }

    }
}