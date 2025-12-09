package com.example.appprivacyanalyzer.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

@Parcelize
data class AppInfo(
    val appName: String,
    val packageName: String,
    val permissions: List<String>,
    val dangerousPermissions: List<String>,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val isSystemApp: Boolean
) : Parcelable {

    // Drawable cannot be parceled → ignore it during parcelization
    @IgnoredOnParcel
    var icon: Drawable? = null
}
