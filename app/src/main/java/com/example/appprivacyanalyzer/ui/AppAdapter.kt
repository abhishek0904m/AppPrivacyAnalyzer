package com.example.appprivacyanalyzer.ui

import android.graphics.Color
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
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    fun updateData(newItems: List<AppInfo>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)
        private val tvPackageName: TextView = itemView.findViewById(R.id.tvPackageName)
        private val tvRiskBadge: TextView = itemView.findViewById(R.id.tvRiskBadge)

        fun bind(app: AppInfo) {
            ivIcon.setImageDrawable(app.icon)
            tvAppName.text = app.appName
            tvPackageName.text = app.packageName

            val (label, color) = when (app.riskLevel) {
                RiskLevel.HIGH -> "HIGH" to Color.parseColor("#FF5252")
                RiskLevel.MEDIUM -> "MEDIUM" to Color.parseColor("#FFC107")
                RiskLevel.LOW -> "LOW" to Color.parseColor("#4CAF50")
            }
            tvRiskBadge.text = "$label (${app.riskScore})"
            tvRiskBadge.setBackgroundColor(color)
            tvRiskBadge.setTextColor(Color.WHITE)

            itemView.setOnClickListener {
                onClick(app)
            }
        }
    }
}
