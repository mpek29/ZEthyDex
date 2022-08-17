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
import fr.mpek29.zethydex.R
import fr.mpek29.zethydex.db.ZEthyDexDatabase
import fr.mpek29.zethydex.MyAdapter

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

        val db = ZEthyDexDatabase(context)
        db.open() //if database doesn't exist, it has now created.

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        val mCursor = db.allNames
        val mAdapter = MyAdapter(mCursor, R.layout.recycler_row, context)
        //add the adapter to the recyclerview
        mRecyclerView.adapter = mAdapter
        mAdapter.setCursor(db.allNames)

        if (db.isOpen){
            db.close()
        }

        val zethyMembers = 5

        val percent: Int = mCursor.count * 100/zethyMembers

        val mLoadingProgressBar = view.findViewById<ProgressBar>(R.id.determinateBar)
        mLoadingProgressBar.progress = percent

        val mPercentText = view.findViewById<TextView>(R.id.percent_text)
        mPercentText.text = "$percent%"

        return view
    }
}