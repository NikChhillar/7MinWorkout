package com.example.a7minworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minworkout.databinding.ItemHistoryBinding

class HistoryAdapter(private val items: ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        val llItemHistory = binding.llItemHistory
        val tvItem = binding.tvItem
        val tvPos = binding.tvPos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String = items[position]
        holder.tvPos.text = (position+1).toString()
        holder.tvItem.text = date

        if (position % 2 ==0){
            holder.llItemHistory.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context,
                R.color.cream))
        }else{
            holder.llItemHistory.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context,
                R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}