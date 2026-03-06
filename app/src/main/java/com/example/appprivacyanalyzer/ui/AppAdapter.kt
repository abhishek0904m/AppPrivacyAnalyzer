package com.example.appprivacyanalyzer.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val cardContainer: LinearLayout = view.findViewById(R.id.cardContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val app = items[position]
        val ctx = holder.itemView.context

        holder.ivIcon.setImageDrawable(app.icon)
        holder.tvName.text = app.appName
        holder.tvPackage.text = app.packageName

        when (app.riskLevel) {

            RiskLevel.HIGH -> {
                holder.tvBadge.text = "HIGH (${app.riskScore})"
                holder.tvBadge.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_badge_red)
                holder.tvBadge.setTextColor(Color.parseColor("#991B1B"))
                holder.cardContainer.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_card_pink)
            }

            RiskLevel.MEDIUM -> {
                holder.tvBadge.text = "MED (${app.riskScore})"
                holder.tvBadge.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_badge_orange)
                holder.tvBadge.setTextColor(Color.parseColor("#92400E"))
                holder.cardContainer.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_card_purple)
            }

            RiskLevel.LOW -> {
                holder.tvBadge.text = "LOW (${app.riskScore})"
                holder.tvBadge.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_badge_green)
                holder.tvBadge.setTextColor(Color.parseColor("#065F46"))
                holder.cardContainer.background =
                    ContextCompat.getDrawable(ctx, R.drawable.bg_card_mint)
            }
        }

        holder.itemView.setOnClickListener {
            onClick(app)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<AppInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}
