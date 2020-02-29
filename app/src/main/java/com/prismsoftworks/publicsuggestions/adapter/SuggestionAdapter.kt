package com.prismsoftworks.publicsuggestions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prismsoftworks.publicsuggestions.R
import com.prismsoftworks.publicsuggestions.model.Suggestion
import com.prismsoftworks.publicsuggestions.view.SuggestionViewHolder

class SuggestionAdapter : RecyclerView.Adapter<SuggestionViewHolder>() {
    var items: List<Suggestion> = ArrayList()
    var viewReadOnly: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_itemview, parent, false))
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.init(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Suggestion>): SuggestionAdapter{
        this.items = items
        return this
    }

    fun setReadOnly(viewReadOnly: Boolean): SuggestionAdapter{
        this.viewReadOnly = viewReadOnly
        return this
    }

//    override fun getItemViewType(position: Int): Int {
//        return
//    }
}