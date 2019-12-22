package com.example.whereismymoney.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R
import com.example.whereismymoney.helpers.recycler.RecyclerViewAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.room.Room
import com.example.whereismymoney.helpers.CalendarHelper
import com.example.whereismymoney.helpers.recycler.SimpleItemTouchHelperCallback
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt


class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        val recycler: RecyclerView = root.findViewById(R.id.debtsRecyclerView)

        val db = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        val date = CalendarHelper().getCurrentDateAsLong()
        val items = db.debtDao().getByDate(date.toString())

        if (items.isEmpty()){
            root.findViewById<ImageView>(R.id.placeholder).visibility = View.VISIBLE
            root.findViewById<TextView>(R.id.placeholderText).visibility = View.VISIBLE
        }

        val myAdapter = RecyclerViewAdapter(
            items.toMutableList(),
            root,
            object : RecyclerViewAdapter.Callback {
                override fun onItemClicked(item: Debt) {
                    //TODO: handle click

                    // но зачем?..
                }
            })

        recycler.adapter = myAdapter

        linearLayoutManager = LinearLayoutManager(root.context)
        recycler.setLayoutManager(linearLayoutManager)

        val callback =
            SimpleItemTouchHelperCallback(myAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recycler)

        return root
    }
}