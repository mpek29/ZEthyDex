package fr.mpek29.zethydex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import fr.mpek29.zethydex.MainActivity
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.ZEthyModel
import fr.mpek29.zethydex.db.ZEthyDexDatabase

class RegisterFragment(
    private val context: MainActivity
) : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //Recuperation of the confirm button
        val confirmBtn = view.findViewById<Button>(R.id.confirm_button)
        confirmBtn.setOnClickListener {
            sendForm(view)
        }

        return view
    }

    private fun sendForm(view: View) {
        val zethyName = view.findViewById<EditText>(R.id.name_input).text.toString()
        val zethyDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
        val zethyApartment = view.findViewById<EditText>(R.id.apartment_input).text.toString()
        val zethyFavoriteDrink = view.findViewById<EditText>(R.id.favorite_drink_input).text.toString()

        //val zethyImageBlob

        // creation of a ZEthyModel
        val zethy = ZEthyModel(
            zethyName,
            zethyDescription,
            zethyApartment,
            zethyFavoriteDrink,
            //zethyImageBlob
            )

        //envoyer en bdd
        val db = ZEthyDexDatabase(context)
        db.open() //if database doesn't exist, it has now created.
        db.insertZethy(zethy)
        if (db.isOpen){
            db.close()
        }
    }
}