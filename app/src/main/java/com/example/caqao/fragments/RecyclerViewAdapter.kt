package com.example.caqao.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.caqao.R

class RecyclerViewAdapter (private var mList : List<RecyclerViewData>):
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>(){

            inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
                val question: TextView = itemView.findViewById(R.id.question)
                val answer: TextView = itemView.findViewById(R.id.answer)
                val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
                val expand_more: LinearLayout = itemView.findViewById(R.id.expand_more)

                fun collapseExpandedView(){
                    answer.visibility = View.GONE
                    expand_more.animate().rotation(0f)
                }
            }
    private var expandedPosition = -1

    private fun isAnyItemExpanded(position: Int){
        if (expandedPosition >= 0 && expandedPosition != position){
            mList[expandedPosition].isExpanded = false
            notifyItemChanged(expandedPosition, 1)
        }
        expandedPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_f_a_qs_list_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val RecyclerViewData = mList[position]
        holder.question.text = RecyclerViewData.question
        holder.answer.text = RecyclerViewData.answer

        val isExpanded: Boolean = RecyclerViewData.isExpanded
        holder.answer.visibility = if (isExpanded) View.VISIBLE  else View.GONE

        if (isExpanded) {
            holder.expand_more.animate().rotation(180f)
        } else {
            holder.expand_more.animate().rotation(0f)
        }

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            RecyclerViewData.isExpanded = !RecyclerViewData.isExpanded
            notifyItemChanged(position, 1)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.isNotEmpty() && payloads[0] == 1){
            val isExpanded: Boolean = mList[position].isExpanded
            holder.answer.visibility = if (isExpanded) View.VISIBLE  else View.GONE

            if (isExpanded) {
                holder.expand_more.animate().rotation(180f)
            } else {
                holder.expand_more.animate().rotation(0f)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}