package com.example.whereismymoney.ui.today

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R
import com.example.whereismymoney.helpers.recycler.RecyclerViewAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.room.Room
import com.example.whereismymoney.NewDebtActivity
import com.example.whereismymoney.helpers.CalendarHelper
import com.example.whereismymoney.helpers.recycler.SimpleItemTouchHelperCallback
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodayFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                }
            })

        recycler.adapter = myAdapter

        linearLayoutManager = LinearLayoutManager(root.context)
        recycler.layoutManager = linearLayoutManager

        val callback =
            SimpleItemTouchHelperCallback(myAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recycler)

        root.findViewById<FloatingActionButton>(R.id.addNewDebtButton).setOnClickListener {
            val intent = Intent(context, NewDebtActivity::class.java)
            startActivityForResult(intent, 1)
        }

        return root
    }
}