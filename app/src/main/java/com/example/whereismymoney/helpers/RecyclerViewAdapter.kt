package com.example.whereismymoney.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R
import androidx.core.content.ContextCompat
import com.example.whereismymoney.models.Debt
import java.text.SimpleDateFormat

class RecyclerViewAdapter(var items: MutableList<Debt>, root: View, val callback: Callback) :
    RecyclerView.Adapter<RecyclerViewAdapter.MainHolder>(), ItemTouchHelperAdapter {

    val root = root

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
        items.removeAt(position)
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

        fun bind(item: Debt) {
            val sdf = SimpleDateFormat("dd.MM.yyyy")

            amountInfoText.text = item.amount.toString() + " " + item.currency
            dateInfoText.text = sdf.format(item.date)
            debtorInfoText.text = item.debtor
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
}