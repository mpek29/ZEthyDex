package fr.mpek29.zethydex.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.mpek29.zethydex.MainActivity
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.ZEthyModel
import fr.mpek29.zethydex.db.ZEthyDexDatabase
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterFragment(
    private val context: MainActivity
) : Fragment() {

    private var previewImage:ImageView? = null
    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var editApartment: EditText
    private lateinit var editFavoriteDrink: EditText
    private lateinit var confirmBtn: Button
    private lateinit var uploadBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //Recuperation des elements
        previewImage= view.findViewById(R.id.preview_image)
        editName= view.findViewById(R.id.name_input)
        editDescription= view.findViewById(R.id.description_input)
        editApartment= view.findViewById(R.id.apartment_input)
        editFavoriteDrink= view.findViewById(R.id.favorite_drink_input)
        confirmBtn = view.findViewById(R.id.confirm_button)
        uploadBtn = view.findViewById(R.id.upload_btn)

        uploadBtn.setOnClickListener {
            pickupImage()
        }

        view.findViewById<ImageView>(R.id.preview_image).setOnClickListener {
            pickupImage()
        }

        confirmBtn.setOnClickListener {
            //saving in the database (with the picture)
            sendForm()
        }

        return view
    }

    private fun pickupImage() {
        //Create a windows for choose a picture
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),47)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK){
            // Check if the data are null or not
            if (data == null || data.data == null) return

            // take the picture
            val selectedImage = data.data

            //update the image preview
            previewImage?.setImageURI(selectedImage)
        }
    }

    @SuppressLint("CutPasteId", "SetTextI18n", "UseCompatLoadingForDrawables")
    private fun sendForm() {

        //obtain the drawable
        val d = previewImage?.drawable

        //convert drawable to ByteArray
        val bitmap = (d as BitmapDrawable).bitmap
        val convertedImage: Bitmap = getResizedBitmap(bitmap)
        val stream = ByteArrayOutputStream()
        convertedImage.compress(CompressFormat.JPEG, 100, stream)
        val zethyImage = stream.toByteArray()

        //take the different data from EditTexts
        var zethyName = editName.text.toString()
        var zethyDescription = editDescription.text.toString()
        var zethyApartment = editApartment.text.toString()
        var zethyFavoriteDrink = editFavoriteDrink.text.toString()

        //check if all entry are not empty
        if((zethyName != "") && (zethyDescription != "") && (zethyApartment != "") && (zethyFavoriteDrink != "")){

            //formatting of the data
            zethyName.lowercase(Locale.getDefault())
            zethyName = upperCaseFirst(zethyName)

            zethyDescription.lowercase(Locale.getDefault())
            zethyDescription = upperCaseFirst(zethyDescription)

            zethyApartment.lowercase(Locale.getDefault())
            zethyApartment = upperCaseFirst(zethyApartment)

            zethyFavoriteDrink.lowercase(Locale.getDefault())
            zethyFavoriteDrink = upperCaseFirst(zethyFavoriteDrink)

            // creation of a ZEthyModel with the data
            val zethy = ZEthyModel(
                null,
                zethyImage,
                zethyName,
                zethyDescription,
                zethyApartment,
                zethyFavoriteDrink
            )

            //sending of the data in the database
            val db = ZEthyDexDatabase(context)
            db.open() //if database doesn't exist, it has now created.
            db.insertZethy(zethy)
            if (db.isOpen) {
                db.close()
            }

            //Reset form
            val drawable = resources.getDrawable(R.drawable.default_picture)
            previewImage?.setImageDrawable(drawable)
            editName.setText("", TextView.BufferType.EDITABLE)
            editDescription.setText("", TextView.BufferType.EDITABLE)
            editApartment.setText("", TextView.BufferType.EDITABLE)
            editFavoriteDrink.setText("", TextView.BufferType.EDITABLE)
        }
    }

    //Resize the Bitmap
    private fun getResizedBitmap(image: Bitmap): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = 250
            height = (width / bitmapRatio).toInt()
        } else {
            height = 250
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    //Format the string for convert in upperCase the first character
    private fun upperCaseFirst(val1: String): String {
        val arr: CharArray = val1.toCharArray()
        arr[0] = arr[0].uppercaseChar()
        return String(arr)
    }
}