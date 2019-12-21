package com.example.whereismymoney.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.R

class RecyclerViewAdapter(var items: List<RecyclerItem>, val callback: Callback) : RecyclerView.Adapter<RecyclerViewAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val debtorInfoText = itemView.findViewById<TextView>(R.id.debtorInfoText)
        private val dateInfoText = itemView.findViewById<TextView>(R.id.dateInfoText)
        private val amountInfoText = itemView.findViewById<TextView>(R.id.amountInfoText)

        fun bind(item: RecyclerItem) {
            amountInfoText.text = item.amount
            dateInfoText.text = item.date
            debtorInfoText.text = item.debtor
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: RecyclerItem)
    }

}