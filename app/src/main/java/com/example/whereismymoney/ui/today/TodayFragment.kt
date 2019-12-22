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
import com.example.whereismymoney.helpers.RecyclerViewAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.room.Room
import com.example.whereismymoney.helpers.SimpleItemTouchHelperCallback
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt
import java.text.SimpleDateFormat
import java.util.*


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
        val textView: TextView = root.findViewById(R.id.text_today)

        val recycler: RecyclerView = root.findViewById(R.id.debtsRecyclerView)

        val db = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        // TODO: fix it pls
        // костыль starts here
        val c = Calendar.getInstance()                // ЗАТО ОНО РАБОТАЕТ
        val year = c.get(Calendar.YEAR)              // ЗАТО ОНО РАБОТАЕТ
        val month = c.get(Calendar.MONTH)           // ЗАТО ОНО РАБОТАЕТ
        val day = c.get(Calendar.DAY_OF_MONTH)     // ЗАТО ОНО РАБОТАЕТ
                                                  // ЗАТО ОНО РАБОТАЕТ
        val chosenDate = day.toString() + "." +  // ЗАТО ОНО РАБОТАЕТ
                (month + 1).toString() + "." +  // ЗАТО ОНО РАБОТАЕТ
                year.toString()                // ЗАТО ОНО РАБОТАЕТ

        val pattern = "dd.MM.yyyy"
        val date = SimpleDateFormat(pattern).parse(chosenDate).time
        val items = db.debtDao().getByDate(date.toString())
        // костыль ends here

        val myAdapter = RecyclerViewAdapter(items.toMutableList(), object : RecyclerViewAdapter.Callback {
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

        todayViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}