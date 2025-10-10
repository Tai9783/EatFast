package com.example.foodorderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.model.ProblemDish

class AdapterItemProblemDish(private val list : List<ProblemDish>): RecyclerView.Adapter<AdapterItemProblemDish.ViewHolder>() {
    inner class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameFood: TextView = itemView.findViewById(R.id.txtNameFood)
        val issueMessage: TextView = itemView.findViewById(R.id.txtIssueMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_item_problem_dish,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item= list[position]
        holder.nameFood.text= item.nameFood
        holder.issueMessage.text=item.issueMessage
    }

    override fun getItemCount(): Int {
      return list.size
    }
}