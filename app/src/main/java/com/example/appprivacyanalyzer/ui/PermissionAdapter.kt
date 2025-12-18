package com.example.appprivacyanalyzer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.databinding.ItemPermissionBinding

class PermissionAdapter(private val items: List<String>) : RecyclerView.Adapter<PermissionAdapter.VH>() {
    inner class VH(val b: ItemPermissionBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.b.tvPermissionName.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
