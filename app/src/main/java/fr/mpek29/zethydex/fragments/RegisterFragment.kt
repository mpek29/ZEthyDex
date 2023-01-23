package fr.mpek29.zethydex.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.test.platform.app.InstrumentationRegistry
import coil.load
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import fr.mpek29.zethydex.MainActivity
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.ZEthyModel
import fr.mpek29.zethydex.db.ZEthyDexDatabase
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterFragment(
    private val context: MainActivity
) : Fragment() {

    private val galleryRequestCode = 2

    private var previewImage: ImageView? = null
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

        //when you click on the image
        uploadBtn.setOnClickListener {
            galleryCheckPermission()
        }

        view.findViewById<ImageView>(R.id.preview_image).setOnClickListener {
            galleryCheckPermission()
        }

        confirmBtn.setOnClickListener {
            //saving in the database (with the picture)
            sendForm()
        }

        return view
    }

    private fun galleryCheckPermission() {

        Dexter.withContext(context).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    context,
                    "Vous avez refusé l'autorisation de stockage pour sélectionner l'image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, galleryRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            view?.findViewById<ImageView>(R.id.preview_image)?.load(data?.data)
        }

    }


    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(context)
            .setMessage("Il semble que vous ayez désactivé les autorisations requises pour cette fonctionnalité. Il peut être activé dans les paramètres de l'application !!!")

            .setPositiveButton("Aller dans les paramètres") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val uri = Uri.fromParts("package", appContext.packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("CutPasteId", "SetTextI18n", "UseCompatLoadingForDrawables")
    private fun sendForm() {

        //obtain the drawable
        val d = previewImage?.drawable

        //convert drawable to ByteArray
        val bitmap = (d as BitmapDrawable).bitmap
        val convertedImage: Bitmap = getResizedBitmap(bitmap)
        val stream = ByteArrayOutputStream()
        convertedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
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