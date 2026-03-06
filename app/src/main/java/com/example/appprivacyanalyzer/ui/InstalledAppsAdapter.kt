package com.example.appprivacyanalyzer.ui

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo

class InstalledAppsAdapter(
    private var apps: List<AppInfo>,
    private val onAppSelected: (AppInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppsAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.imgIcon)
        val name: TextView = v.findViewById(R.id.tvName)
        val pkg: TextView = v.findViewById(R.id.tvPackage)
        val source: TextView = v.findViewById(R.id.tvSource)
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

        // Get installation source
        val pm = h.itemView.context.packageManager
        val installerPackage = try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                pm.getInstallSourceInfo(app.packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(app.packageName)
            }
        } catch (e: Exception) {
            null
        }

        val sourceText = when (installerPackage) {
            "com.android.vending" -> "📱 Installed from Google Play Store"
            "com.amazon.venezia" -> "📦 Installed from Amazon Appstore"
            "com.sec.android.app.samsungapps" -> "🏪 Installed from Samsung Galaxy Store"
            "com.huawei.appmarket" -> "🏪 Installed from Huawei AppGallery"
            "com.xiaomi.mipicks" -> "🏪 Installed from Mi Store"
            "com.oppo.market" -> "🏪 Installed from Oppo App Market"
            "com.bbk.appstore" -> "🏪 Installed from Vivo App Store"
            null -> "⚠️ Unknown source (Sideloaded)"
            else -> "📋 Installed via: ${installerPackage?.substringAfterLast('.') ?: "Unknown"}"
        }

        h.source.text = sourceText

        h.itemView.setOnClickListener {
            onAppSelected(app)
        }
    }

    override fun getItemCount() = apps.size

    fun updateData(newApps: List<AppInfo>) {
        apps = newApps
        notifyDataSetChanged()
    }
}
