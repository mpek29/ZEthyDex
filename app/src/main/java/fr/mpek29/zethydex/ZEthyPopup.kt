package fr.mpek29.zethydex

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import fr.mpek29.zethydex.db.ZEthyDexDatabase

/**
 * This class is create for manage the popUp generate by the click in a recycler row
 */

class ZEthyPopup(
    private val zethyList: ArrayList<ZEthyModel>,
    private val mAdapter: MyAdapter,
    private val position: Int,
    private val determinateBar: ProgressBar?,
    private val mPercentText: TextView?,
    private val zethyMembers: Int?
) :Dialog(mAdapter.mContext){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_zethy_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
    }

    @SuppressLint("Range", "SetTextI18n")
    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            //delete the element that we display
            mAdapter.notifyItemRemoved(position)

            //delete the data from the database
            val db = ZEthyDexDatabase(mAdapter.mContext)
            db.open()
            db.deleteZethy(zethyList[position])

            //remove the data for the current zethyList for change the state of the loading bar
            zethyList.remove(zethyList[position])

            if(determinateBar != null && mPercentText != null && zethyMembers != null){
                //we re-calc the percent display in the loading bar
                val percent = zethyList.size * 100/zethyMembers
                determinateBar.progress = percent
                mPercentText.text = "$percent%"
            }

            dismiss()

        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            //close the window
            dismiss()

        }
    }

    @SuppressLint("Range")
    private fun setupComponents() {
        findViewById<ImageView>(R.id.image_item)?.setImageDrawable(BitmapDrawable(bytes2Bitmap(zethyList[position].image)))
        findViewById<TextView>(R.id.popup_zethyname).text = zethyList[position].name
        findViewById<TextView>(R.id.popup_zethy_description).text = zethyList[position].description
        findViewById<TextView>(R.id.popup_zethy_apartment).text = zethyList[position].apartment
        findViewById<TextView>(R.id.popup_zethy_favorite_drink).text = zethyList[position].favoriteDrink
    }

    //convert the bytes array to a Bitmap
    private fun bytes2Bitmap(b: ByteArray): Bitmap? {
        return if (b.isNotEmpty()) {
            BitmapFactory.decodeByteArray(b, 0, b.size)
        } else null

    }
}