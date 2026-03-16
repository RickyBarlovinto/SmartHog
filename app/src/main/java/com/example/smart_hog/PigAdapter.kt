package com.example.smart_hog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class PigAdapter(private val pigList: List<Pig>) :
    RecyclerView.Adapter<PigAdapter.PigViewHolder>() {

    class PigViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pigID: TextView = itemView.findViewById(R.id.pigID)
        val pigBatch: TextView = itemView.findViewById(R.id.pigBatch)
        val pigWeight: TextView = itemView.findViewById(R.id.pigWeight)
        val pigStatus: TextView = itemView.findViewById(R.id.pigStatus)
        val historyBtn: Button = itemView.findViewById(R.id.historyBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PigViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pig, parent, false)
        return PigViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pigList.size
    }

    override fun onBindViewHolder(holder: PigViewHolder, position: Int) {
        val pig = pigList[position]
        holder.pigID.text = pig.id
        holder.pigBatch.text = pig.batch
        holder.pigWeight.text = "Last Weight: ${pig.weight}"
        holder.pigStatus.text = pig.status

        holder.historyBtn.setOnClickListener {
            showHistoryDialog(holder.itemView.context, pig.id)
        }
    }

    private fun showHistoryDialog(context: android.content.Context, pigId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pig $pigId")
        builder.setMessage("Weight History\n---------------\nJan 10 - 65kg\nFeb 05 - 72kg\nMar 10 - 80kg\nApr 15 - 85kg")
        builder.setPositiveButton("Close", null)
        builder.show()
    }
}
