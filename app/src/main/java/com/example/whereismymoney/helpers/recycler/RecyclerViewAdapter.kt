package com.example.whereismymoney.helpers.recycler

import android.graphics.Color.WHITE
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.whereismymoney.helpers.CalendarHelper
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.text.SimpleDateFormat

class RecyclerViewAdapter(var items: MutableList<Debt>, val root: View, val callback: Callback) :
    RecyclerView.Adapter<RecyclerViewAdapter.MainHolder>(),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item,
            parent,
            false
        )
        return MainHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onItemDismiss(position: Int) {
        val debt = items.get(position)

        items.removeAt(position)
        deleteDebtFromDB(debt)

        notifyItemRemoved(position)
        checkForEmpty()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = items.removeAt(fromPosition)
        items.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {

        private val debtorInfoText = itemView.findViewById<TextView>(R.id.debtorInfoText)
        private val dateInfoText = itemView.findViewById<TextView>(R.id.dateInfoText)
        private val amountInfoText = itemView.findViewById<TextView>(R.id.amountInfoText)

        private val currentDate = CalendarHelper().getCurrentDateAsLong()

        fun bind(item: Debt) {
            val sdf = SimpleDateFormat("dd.MM.yyyy")

            amountInfoText.text = item.amount.toString() + " " + item.currency
            dateInfoText.text = sdf.format(item.date)
            debtorInfoText.text = item.debtor

            if (item.isMyDebt){
                itemView.card_view.setBackgroundResource(R.color.colorCardAccent)
            } else itemView.card_view.setBackgroundResource(R.color.colorCardPrimary)

//            if (item.date == currentDate){
//                itemView.card_view.setBackgroundResource(R.color.colorCardToday)
//            }

            amountInfoText.setTextColor(WHITE)
            debtorInfoText.setTextColor(WHITE)
            dateInfoText.setTextColor(WHITE)

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    callback.onItemClicked(items[adapterPosition])
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorCardOnSwipe
                )
            )
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    interface Callback {
        fun onItemClicked(item: Debt)
    }

    private fun checkForEmpty(){
        if (items.size == 0){
            root.findViewById<ImageView>(R.id.placeholder).visibility = VISIBLE
            root.findViewById<TextView>(R.id.placeholderText).visibility = VISIBLE
        }
    }

    private fun deleteDebtFromDB(debt: Debt){
        val db = Room.databaseBuilder(
            root.context,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        db.debtDao().delete(debt)
    }
}