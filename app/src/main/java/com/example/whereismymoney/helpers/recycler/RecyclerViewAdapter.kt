package com.example.whereismymoney.helpers.recycler

import android.annotation.SuppressLint
import android.graphics.Color.*
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

        private val currentDate = CalendarHelper().getCurrentDateAsLong()

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(item: Debt) {
            val sdf = SimpleDateFormat("dd.MM.yyyy")

            itemView.amountInfoText.text = item.amount.toString() + " " + item.currency
            itemView.dateInfoText.text = sdf.format(item.date)
            itemView.debtorInfoText.text = item.debtor

            // честно говоря, я так и не понял, когда нужно перекрашивать в другой цвет
            // поэтому сделал так
            if (!item.isMyDebt) {
                itemView.card_view.setBackgroundResource(R.color.colorCardPrimary)
            }

            if (item.date < currentDate) {
                itemView.card_view.setBackgroundResource(R.color.colorCardAccent)
            }

            if (item.date == currentDate) {
                itemView.card_view.setBackgroundResource(R.color.colorCardToday)
                if (item.isMyDebt)
                    itemView.card_view.dateInfoText.text =
                        root.context.getString(R.string.need_to_return_today)
                else
                    itemView.card_view.dateInfoText.text =
                        root.context.getString(R.string.will_be_returned_today)
            }

            if (item.isMyDebt && item.date > currentDate) {
                itemView.amountInfoText.setTextColor(BLACK)
                itemView.debtorInfoText.setTextColor(GRAY)
                itemView.dateInfoText.setTextColor(GRAY)
            } else {
                itemView.amountInfoText.setTextColor(WHITE)
                itemView.debtorInfoText.setTextColor(WHITE)
                itemView.dateInfoText.setTextColor(WHITE)
            }

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

    private fun checkForEmpty() {
        if (items.size == 0) {
            root.findViewById<ImageView>(R.id.placeholder).visibility = VISIBLE
            root.findViewById<TextView>(R.id.placeholderText).visibility = VISIBLE
        }
    }

    private fun deleteDebtFromDB(debt: Debt) {
        val db = Room.databaseBuilder(
            root.context,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        db.debtDao().delete(debt)
    }
}