package com.example.appprivacyanalyzer.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel

data class SummaryStats(
    val totalApps: Int,
    val highRiskCount: Int,
    val mediumRiskCount: Int,
    val lowRiskCount: Int,
    val cameraApps: Int,
    val micApps: Int,
    val locationApps: Int
)

class AppRepository(private val context: Context) {

    private val dangerousPermissions = setOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.READ_SMS,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.RECEIVE_SMS,
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.CALL_PHONE,
        android.Manifest.permission.READ_CALL_LOG,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.BODY_SENSORS
    )

    fun loadApps(): List<AppInfo> {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val apps = mutableListOf<AppInfo>()

        for (pkg in packages) {
            val appInfo = pkg.applicationInfo

            val isSystemApp =
                (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                        (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0

            val appName = appInfo.loadLabel(pm).toString()
            val packageName = pkg.packageName
            val icon = appInfo.loadIcon(pm)

            val requestedPerms = pkg.requestedPermissions?.toList() ?: emptyList()
            if (requestedPerms.isEmpty()) continue

            val granted = mutableListOf<String>()
            val flags = pkg.requestedPermissionsFlags

            if (flags != null && flags.size == requestedPerms.size) {
                for (i in requestedPerms.indices) {
                    if ((flags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        granted.add(requestedPerms[i])
                    }
                }
            } else {
                granted.addAll(requestedPerms)
            }

            if (granted.isEmpty()) continue

            val dangerousGranted = granted.filter { it in dangerousPermissions }

            val riskScore = calculateRiskScore(
                dangerousCount = dangerousGranted.size,
                totalPermissions = granted.size
            )

            val riskLevel = riskLevelFromScore(riskScore)

            // Create without icon first (Parcelable-friendly)
            val app = AppInfo(
                appName = appName,
                packageName = packageName,
                permissions = granted,
                dangerousPermissions = dangerousGranted,
                riskScore = riskScore,
                riskLevel = riskLevel,
                isSystemApp = isSystemApp
            )

            // Assign icon after creation
            app.icon = icon

            apps.add(app)
        }

        return apps.sortedByDescending { it.riskScore }
    }

    private fun calculateRiskScore(dangerousCount: Int, totalPermissions: Int): Int {
        if (totalPermissions == 0) return 0
        return ((dangerousCount * 100f) / totalPermissions).toInt().coerceIn(0, 100)
    }

    private fun riskLevelFromScore(score: Int): RiskLevel =
        when {
            score >= 70 -> RiskLevel.HIGH
            score >= 40 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

    fun computeSummary(apps: List<AppInfo>): SummaryStats {
        var high = 0
        var medium = 0
        var low = 0
        var cam = 0
        var mic = 0
        var loc = 0

        for (app in apps) {
            when (app.riskLevel) {
                RiskLevel.HIGH -> high++
                RiskLevel.MEDIUM -> medium++
                RiskLevel.LOW -> low++
            }

            if (app.dangerousPermissions.contains(android.Manifest.permission.CAMERA)) cam++
            if (app.dangerousPermissions.contains(android.Manifest.permission.RECORD_AUDIO)) mic++
            if (app.dangerousPermissions.any {
                    it == android.Manifest.permission.ACCESS_FINE_LOCATION ||
                            it == android.Manifest.permission.ACCESS_COARSE_LOCATION
                }) loc++
        }

        return SummaryStats(
            totalApps = apps.size,
            highRiskCount = high,
            mediumRiskCount = medium,
            lowRiskCount = low,
            cameraApps = cam,
            micApps = mic,
            locationApps = loc
        )
    }
}
