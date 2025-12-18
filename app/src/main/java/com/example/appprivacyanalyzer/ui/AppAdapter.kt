package com.example.appprivacyanalyzer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel

class AppAdapter(
    private var items: List<AppInfo>,
    private val onClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<AppAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivAppIcon)
        val tvName: TextView = view.findViewById(R.id.tvAppName)
        val tvPackage: TextView = view.findViewById(R.id.tvPackage)
        val tvBadge: TextView = view.findViewById(R.id.tvRiskBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val app = items[position]
        holder.ivIcon.setImageDrawable(app.icon)
        holder.tvName.text = app.appName
        holder.tvPackage.text = app.packageName
        holder.tvBadge.text = when (app.riskLevel) {
            RiskLevel.HIGH -> "HIGH (${app.riskScore})"
            RiskLevel.MEDIUM -> "MED (${app.riskScore})"
            RiskLevel.LOW -> "LOW (${app.riskScore})"
        }

        // color badge background depending on risk
        val ctx = holder.itemView.context
        val bg = when (app.riskLevel) {
            RiskLevel.HIGH -> ctx.resources.getDrawable(R.drawable.bg_badge_red, null)
            RiskLevel.MEDIUM -> ctx.resources.getDrawable(R.drawable.bg_badge_orange, null)
            RiskLevel.LOW -> ctx.resources.getDrawable(R.drawable.bg_badge_green, null)
        }
        holder.tvBadge.background = bg

        holder.itemView.setOnClickListener { onClick(app) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<AppInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}
