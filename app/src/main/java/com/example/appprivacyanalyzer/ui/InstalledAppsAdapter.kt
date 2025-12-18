package com.example.appprivacyanalyzer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo

class InstalledAppsAdapter(
    private val apps: List<AppInfo>,
    private val onAppSelected: (AppInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppsAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.imgIcon)
        val name: TextView = v.findViewById(R.id.tvName)
        val pkg: TextView = v.findViewById(R.id.tvPackage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_installed_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val app = apps[pos]
        h.icon.setImageDrawable(app.icon)
        h.name.text = app.appName
        h.pkg.text = app.packageName

        // ✅ IMPORTANT: NO system intent here
        h.itemView.setOnClickListener {
            onAppSelected(app)
        }
    }

    override fun getItemCount() = apps.size
}
