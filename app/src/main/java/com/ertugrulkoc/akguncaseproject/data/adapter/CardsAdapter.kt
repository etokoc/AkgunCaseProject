package com.ertugrulkoc.akguncaseproject.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ertugrulkoc.akguncaseproject.R

class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {
    private var _cardList = ArrayList<String>()

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNumber: TextView = itemView.findViewById(R.id.txt_cardId)
    }

    fun setCardList(cardList: ArrayList<String>) {
        _cardList = arrayListOf()
        _cardList = cardList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_card_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _cardList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.cardNumber.text = _cardList[position]
    }
}