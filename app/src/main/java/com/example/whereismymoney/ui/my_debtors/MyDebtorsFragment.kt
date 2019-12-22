package com.example.whereismymoney.ui.my_debtors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.whereismymoney.R
import com.example.whereismymoney.helpers.RecyclerViewAdapter
import com.example.whereismymoney.helpers.SimpleItemTouchHelperCallback
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt

class MyDebtorsFragment : Fragment() {

    private lateinit var myDebtorsViewModel: MyDebtorsViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDebtorsViewModel =
            ViewModelProviders.of(this).get(MyDebtorsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_debtors, container, false)
        val recycler: RecyclerView = root.findViewById(R.id.debtsRecyclerView)

        val db = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        val items = db.debtDao().loadNotMyDebts()

        if (items.size == 0) {
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

        val callback = SimpleItemTouchHelperCallback(myAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recycler)

        return root
    }
}