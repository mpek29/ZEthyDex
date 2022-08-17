package fr.mpek29.zethydex

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import fr.mpek29.zethydex.db.ZEthyDexDatabase

class ZEthyPopup(
    private val mAdapter: MyAdapter,
    private val currentZEthy: ZEthyModel
) :Dialog(mAdapter.mContext){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_zethy_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            //delete the element that we display
            val db = ZEthyDexDatabase(mAdapter.mContext)
            db.open()
            db.deleteZethy(currentZEthy)
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

        //Refresh the ZethyName
        findViewById<TextView>(R.id.popup_zethyname).text = currentZEthy.name

        findViewById<TextView>(R.id.popup_zethy_description).text = currentZEthy.description

        findViewById<TextView>(R.id.popup_zethy_apartment).text = currentZEthy.apartment

        findViewById<TextView>(R.id.popup_zethy_favorite_drink).text = currentZEthy.favoriteDrink
    }

}