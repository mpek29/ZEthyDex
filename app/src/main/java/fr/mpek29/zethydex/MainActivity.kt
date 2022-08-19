package fr.mpek29.zethydex

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.mpek29.zethydex.fragments.CollectionFragment
import fr.mpek29.zethydex.fragments.HomeFragment
import fr.mpek29.zethydex.fragments.RegisterFragment

/**
 * This is a "simple" sqlite database example.   It uses a recyclerview and a simple ish adapter
 * to display the data.  There is a much better adapter that understands cursors in SqliteDemo3 and 4.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //we start the activity with the HHomeFragment
        loadFragment(HomeFragment(this), R.string.home_title)

        //the application change fragment in terms of button pressed
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> {
                    loadFragment(HomeFragment(this), R.string.home_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collectionFragment -> {
                    loadFragment(CollectionFragment(this), R.string.collection_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.registerFragment -> {
                    loadFragment(RegisterFragment(this), R.string.register_title)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    }

    //load the fragment that we have choose
     private fun loadFragment(fragment: Fragment, string: Int) {

        // Change the page title
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // Inject the fragment in the fragment_container
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}