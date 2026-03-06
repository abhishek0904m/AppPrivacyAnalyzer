package com.example.appprivacyanalyzer.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel

/**
 * Loads installed apps and computes risk + summary stats.
 * Uses a realistic permission-based risk model.
 */
class AppRepository(private val context: Context) {

    private val pm: PackageManager = context.packageManager

    fun loadApps(): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()

        val installed = try {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            emptyList<ApplicationInfo>()
        }

        val fallbackIcon: Drawable? =
            ContextCompat.getDrawable(context, R.mipmap.ic_launcher)

        for (ai in installed) {
            try {
                val pkgName = ai.packageName

                val label = try {
                    pm.getApplicationLabel(ai).toString()
                } catch (e: Exception) {
                    pkgName
                }

                val perms = try {
                    val pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS)
                    pkgInfo.requestedPermissions?.toList() ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }

                val icon = try {
                    pm.getApplicationIcon(pkgName)
                } catch (e: Exception) {
                    fallbackIcon
                }

                // ----------- IMPROVED RISK LOGIC -----------

                val usesCamera =
                    perms.contains(android.Manifest.permission.CAMERA)

                val usesMic =
                    perms.contains(android.Manifest.permission.RECORD_AUDIO)

                val usesLocation =
                    perms.contains(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                            perms.contains(android.Manifest.permission.ACCESS_COARSE_LOCATION)

                val usesContacts =
                    perms.any { it.contains("READ_CONTACTS") || it.contains("WRITE_CONTACTS") }

                val usesSms =
                    perms.any { it.contains("READ_SMS") || it.contains("SEND_SMS") }

                val usesStorage =
                    perms.any {
                        it.contains("READ_EXTERNAL_STORAGE") ||
                                it.contains("WRITE_EXTERNAL_STORAGE")
                    }

                var score = 0
                if (usesCamera) score += 2
                if (usesMic) score += 2
                if (usesLocation) score += 2
                if (usesContacts) score += 2
                if (usesSms) score += 2
                if (usesStorage) score += 1

                val riskLevel = when {
                    score >= 6 -> RiskLevel.HIGH
                    score >= 3 -> RiskLevel.MEDIUM
                    else -> RiskLevel.LOW
                }

                val isSystemApp =
                    (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                apps.add(
                    AppInfo(
                        appName = label,
                        packageName = pkgName,
                        isSystemApp = isSystemApp,
                        icon = icon,
                        permissions = perms,
                        riskScore = score,
                        riskLevel = riskLevel
                    )
                )
            } catch (_: Exception) {
                // skip broken entries safely
            }
        }

        // User apps first, then alphabetically
        apps.sortWith(compareBy({ it.isSystemApp }, { it.appName.lowercase() }))

        return apps
    }

    fun computeSummary(apps: List<AppInfo>): SummaryStats {
        var high = 0
        var medium = 0
        var low = 0
        var cam = 0
        var mic = 0
        var loc = 0

        for (a in apps) {
            when (a.riskLevel) {
                RiskLevel.HIGH -> high++
                RiskLevel.MEDIUM -> medium++
                RiskLevel.LOW -> low++
            }

            if (a.usesCamera) cam++
            if (a.usesMicrophone) mic++
            if (a.usesLocation) loc++
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
