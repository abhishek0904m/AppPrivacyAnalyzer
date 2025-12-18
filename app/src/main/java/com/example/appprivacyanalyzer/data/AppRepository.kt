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
 * Lightweight repository to load installed apps and compute summary statistics.
 * Defensive: never crashes if PM calls fail, always returns sensible defaults.
 */
class AppRepository(private val context: Context) {

    private val pm: PackageManager = context.packageManager

    /**
     * Load installed apps and return a list of AppInfo.
     */
    fun loadApps(): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()

        // Get installed application list
        val installed = try {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        } catch (t: Throwable) {
            emptyList<ApplicationInfo>()
        }

        val fallbackIcon: Drawable? = try {
            ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
        } catch (t: Throwable) {
            null
        }

        for (ai in installed) {
            try {
                val pkgName = ai.packageName
                val label = try {
                    pm.getApplicationLabel(ai).toString()
                } catch (t: Throwable) {
                    pkgName
                }

                // requested permissions (may be null)
                val perms = try {
                    val pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS)
                    pkgInfo.requestedPermissions?.toList() ?: emptyList()
                } catch (t: Throwable) {
                    emptyList()
                }

                // attempt to get icon (may be null)
                val rawIcon: Drawable? = try {
                    pm.getApplicationIcon(pkgName)
                } catch (t: Throwable) {
                    null
                }
                val iconToUse: Drawable? = rawIcon ?: fallbackIcon

                // simple risk heuristic:
                // - count "dangerous" permissions (manifest protection level DANGEROUS)
                // - treat camera/mic/location as higher weight
                val dangerousCount = perms.count { p -> isDangerousPermission(p) }
                val cameraWeight = if (perms.any { it.equals(android.Manifest.permission.CAMERA, true) }) 2 else 0
                val micWeight = if (perms.any { it.equals(android.Manifest.permission.RECORD_AUDIO, true) }) 2 else 0
                val locWeight = if (perms.any { it.equals(android.Manifest.permission.ACCESS_FINE_LOCATION, true) ||
                            it.equals(android.Manifest.permission.ACCESS_COARSE_LOCATION, true) }) 2 else 0

                val score = dangerousCount + cameraWeight + micWeight + locWeight

                val level = when {
                    score >= 5 -> RiskLevel.HIGH
                    score >= 2 -> RiskLevel.MEDIUM
                    else -> RiskLevel.LOW
                }

                val isSystem = (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                val app = AppInfo(
                    appName = label,
                    packageName = pkgName,
                    isSystemApp = isSystem,
                    icon = iconToUse,
                    permissions = perms,
                    riskScore = score,
                    riskLevel = level
                )

                apps.add(app)
            } catch (_: Throwable) {
                // defensive: skip problematic app entry
            }
        }

        // Optionally sort: user apps first, alphabetically
        apps.sortWith(compareBy({ it.isSystemApp }, { it.appName.lowercase() }))

        return apps
    }

    /**
     * Compute summary numbers for a list of apps.
     */
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

    /**
     * Very simple classifier to mark known dangerous permissions.
     * This is not exhaustive; extend as needed.
     */
    private fun isDangerousPermission(permission: String?): Boolean {
        if (permission == null) return false
        val p = permission.lowercase()
        // Basic set of dangerous permissions — extend if needed
        val dangerousPrefixes = listOf(
            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_CONTACTS",
            "android.permission.SEND_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.READ_SMS",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
        ).map { it.lowercase() }

        return dangerousPrefixes.any { dp -> p == dp || p.contains(dp) }
    }
}
