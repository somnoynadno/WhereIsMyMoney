package com.example.whereismymoney.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R
import com.example.whereismymoney.helpers.RecyclerItem
import com.example.whereismymoney.helpers.RecyclerViewAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.whereismymoney.helpers.SimpleItemTouchHelperCallback


class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mItemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        val textView: TextView = root.findViewById(R.id.text_today)

        val recycler: RecyclerView = root.findViewById(R.id.debtsRecyclerView)
        val items = listOf(
            RecyclerItem("Сергей", "12.12.2012", "1000"),
            RecyclerItem("Владимир", "11.11.2011", "20000"),
            RecyclerItem("Слава", "10.10.2010", "500"),
            RecyclerItem("Игорь", "01.01.2001", "4000")
        )

        val myAdapter = RecyclerViewAdapter(items.toMutableList(), object : RecyclerViewAdapter.Callback {
            override fun onItemClicked(item: RecyclerItem) {
                //TODO handle click
            }
        })

        recycler.adapter = myAdapter

        linearLayoutManager = LinearLayoutManager(root.context)
        recycler.setLayoutManager(linearLayoutManager)

        val callback = SimpleItemTouchHelperCallback(myAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recycler)

        todayViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}