package com.jesse.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jesse.pokemon.R
import com.jesse.pokemon.data.Moves

class MoveAdapter : RecyclerView.Adapter<MoveAdapter.MoveViewHolder>() {

    private val moveList = mutableListOf<Moves>()

    inner class MoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val move : TextView = itemView.findViewById(R.id.moveTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.move,parent,false)
        return MoveViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        holder.move.text = moveList[position].move.name
    }

    override fun getItemCount(): Int {
        return moveList.size
    }


    /**
     * Populates the [moveList] with the moves value
     * from the API call
     */
    fun populateMoveList(moves: List<Moves>){
        moveList.clear()
        moveList.addAll(moves)
        notifyDataSetChanged()
    }
}