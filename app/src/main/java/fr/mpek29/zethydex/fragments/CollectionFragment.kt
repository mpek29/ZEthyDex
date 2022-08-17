package fr.mpek29.zethydex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.mpek29.zethydex.MainActivity
import fr.mpek29.zethydex.MyAdapter
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.db.ZEthyDexDatabase

class CollectionFragment(
    private val context: MainActivity
) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        val db = ZEthyDexDatabase(context)
        db.open() //if database doesn't exist, it has now created.

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter = MyAdapter(db.allNames, R.layout.recycler_row, context)
        mAdapter.setCursor(db.allNames)
        //add the adapter to the recyclerview
        mRecyclerView.adapter = mAdapter
        if (db.isOpen){
            db.close()
        }

        return view
    }
}