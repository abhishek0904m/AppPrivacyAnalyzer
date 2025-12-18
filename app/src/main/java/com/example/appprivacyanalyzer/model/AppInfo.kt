package com.example.appprivacyanalyzer.model

import android.graphics.drawable.Drawable

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

/**
 * Canonical app model used by UI and repository.
 *
 * - icon is nullable (Drawable?) to avoid Drawable? vs Drawable mismatches.
 * - permissions is the list used across the codebase.
 * - dangerousPermissions is a compatibility alias (some older files used that name).
 */
data class AppInfo(
    val appName: String,
    val packageName: String,
    val isSystemApp: Boolean,
    val icon: Drawable?,
    val permissions: List<String> = emptyList(),
    val riskScore: Int = 0,
    val riskLevel: RiskLevel = RiskLevel.LOW
) {
    // compatibility alias for code that expects "dangerousPermissions"
    val dangerousPermissions: List<String>
        get() = permissions

    val usesCamera: Boolean
        get() = permissions.any {
            it.equals(android.Manifest.permission.CAMERA, ignoreCase = true)
        }

    val usesMicrophone: Boolean
        get() = permissions.any {
            it.equals(android.Manifest.permission.RECORD_AUDIO, ignoreCase = true)
        }

    val usesLocation: Boolean
        get() = permissions.any { p ->
            p.equals(android.Manifest.permission.ACCESS_FINE_LOCATION, ignoreCase = true) ||
                    p.equals(android.Manifest.permission.ACCESS_COARSE_LOCATION, ignoreCase = true)
        }
}
