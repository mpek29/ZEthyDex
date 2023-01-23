package fr.mpek29.zethydex.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.mpek29.zethydex.MainActivity
import fr.mpek29.zethydex.MyAdapter
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.db.ZEthyDexDatabase

class HomeFragment(
    private val context: MainActivity
) : Fragment(){

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //we create an instance of ZEthyDexDatabase
        val db = ZEthyDexDatabase(context)
        db.open() //if database doesn't exist, it has now created.
        val zethyList = db.lastCaptured

        // link between Adapter, recycler view and LoadingBar
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        val mLoadingProgressBar = view.findViewById<ProgressBar>(R.id.determinateBar)

        val mPercentText = view.findViewById<TextView>(R.id.percent_text)

        val zethyMembers = 11

        val mAdapter = MyAdapter("Home",zethyList, R.layout.recycler_row, context, mLoadingProgressBar,mPercentText, zethyMembers)
        //add the adapter to the recyclerview
        mRecyclerView.adapter = mAdapter

        if (db.isOpen){
            db.close()
        }

        return view
    }
}