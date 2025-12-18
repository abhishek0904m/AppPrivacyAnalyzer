package com.example.appprivacyanalyzer.scanner

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel
import java.io.File

class AppScanner(private val context: Context) {

    private val pm: PackageManager = context.packageManager

    // =========================
    // INSTALLED APPS LIST
    // =========================
    fun getInstalledApps(): List<AppInfo> {
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        return packages.map { pkg ->
            val isSystem =
                (pkg.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0

            AppInfo(
                appName = pkg.applicationInfo.loadLabel(pm).toString(),
                packageName = pkg.packageName,
                isSystemApp = isSystem,
                icon = pkg.applicationInfo.loadIcon(pm),
                permissions = pkg.requestedPermissions?.toList() ?: emptyList()
            )
        }.sortedBy { it.appName.lowercase() }
    }

    // =========================
    // SCAN INSTALLED APP
    // =========================
    fun scanInstalledApp(packageName: String): ScanResult {
        val pkgInfo = pm.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        )

        val appName = pkgInfo.applicationInfo.loadLabel(pm).toString()
        val permissions = pkgInfo.requestedPermissions ?: emptyArray()

        val risks = mutableListOf<String>()
        var score = 0

        permissions.forEach { perm ->
            when {
                perm.contains("CAMERA") -> {
                    risks.add("📷 Camera access")
                    score += 3
                }
                perm.contains("RECORD_AUDIO") -> {
                    risks.add("🎤 Microphone access")
                    score += 3
                }
                perm.contains("ACCESS_FINE_LOCATION") ||
                        perm.contains("ACCESS_COARSE_LOCATION") -> {
                    risks.add("📍 Location access")
                    score += 2
                }
                perm.contains("READ_SMS") ||
                        perm.contains("RECEIVE_SMS") -> {
                    risks.add("📩 SMS access")
                    score += 4
                }
                perm.contains("SYSTEM_ALERT_WINDOW") -> {
                    risks.add("⚠ Overlay permission")
                    score += 4
                }
            }
        }

        val level = when {
            score >= 8 -> RiskLevel.HIGH
            score >= 4 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        return ScanResult(
            appName = appName,
            packageName = packageName,
            risks = if (risks.isEmpty()) listOf("No risky permissions found") else risks,
            riskScore = score,
            riskLevel = level
        )
    }

    // =========================
    // SCAN APK FILE (FIXED)
    // =========================
    fun scanApk(uri: Uri): ScanResult {

        // 🔹 Copy APK to temp file (REQUIRED for content:// URIs)
        val tempApk = File(context.cacheDir, "scanned.apk")

        context.contentResolver.openInputStream(uri)?.use { input ->
            tempApk.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val pkgInfo = pm.getPackageArchiveInfo(
            tempApk.absolutePath,
            PackageManager.GET_PERMISSIONS
        )

        if (pkgInfo == null) {
            return ScanResult(
                appName = "Invalid APK",
                packageName = "Unknown",
                risks = listOf("Unable to read APK"),
                riskScore = 0,
                riskLevel = RiskLevel.LOW
            )
        }

        // 🔹 Required so label & icon load correctly
        pkgInfo.applicationInfo.sourceDir = tempApk.absolutePath
        pkgInfo.applicationInfo.publicSourceDir = tempApk.absolutePath

        val appName = pkgInfo.applicationInfo.loadLabel(pm).toString()
        val permissions = pkgInfo.requestedPermissions ?: emptyArray()

        val risks = mutableListOf<String>()
        var score = 0

        permissions.forEach { perm ->
            when {
                perm.contains("CAMERA") -> {
                    risks.add("📷 Camera access")
                    score += 3
                }
                perm.contains("RECORD_AUDIO") -> {
                    risks.add("🎤 Microphone access")
                    score += 3
                }
                perm.contains("ACCESS_FINE_LOCATION") ||
                        perm.contains("ACCESS_COARSE_LOCATION") -> {
                    risks.add("📍 Location access")
                    score += 2
                }
                perm.contains("READ_SMS") ||
                        perm.contains("RECEIVE_SMS") -> {
                    risks.add("📩 SMS access")
                    score += 4
                }
                perm.contains("SYSTEM_ALERT_WINDOW") -> {
                    risks.add("⚠ Overlay permission")
                    score += 4
                }
            }
        }

        val level = when {
            score >= 8 -> RiskLevel.HIGH
            score >= 4 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        return ScanResult(
            appName = appName,
            packageName = pkgInfo.packageName,
            risks = if (risks.isEmpty()) listOf("No risky permissions found") else risks,
            riskScore = score,
            riskLevel = level
        )
    }
}
