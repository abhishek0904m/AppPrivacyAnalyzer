package com.example.appprivacyanalyzer.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.appprivacyanalyzer.R

class AppDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
        const val EXTRA_APP_NAME = "extra_app_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        val appName = intent.getStringExtra(EXTRA_APP_NAME) ?: return
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: return

        findViewById<TextView>(R.id.tvAppName).text = appName
        findViewById<TextView>(R.id.tvPackageName).text = packageName

        // ✅ Open real app permission settings
        findViewById<TextView>(R.id.btnOpenSettings).setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }

        loadSensitivePermissions(packageName)
    }

    private fun loadSensitivePermissions(pkg: String) {
        val pm = packageManager
        val container = findViewById<LinearLayout>(R.id.permissionContainer)
        container.removeAllViews()

        val info = pm.getPackageInfo(pkg, PackageManager.GET_PERMISSIONS)
        val permissions = info.requestedPermissions ?: return
        val flags = info.requestedPermissionsFlags ?: return

        permissions.forEachIndexed { index, permission ->
            val granted =
                flags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0

            if (!granted) return@forEachIndexed

            val label = when {
                permission.contains("CAMERA") -> "📷 Camera access granted"
                permission.contains("RECORD_AUDIO") -> "🎤 Microphone access granted"
                permission.contains("LOCATION") -> "📍 Location access granted"
                else -> null
            }

            label?.let {
                val tv = TextView(this).apply {
                    text = it
                    textSize = 14f
                    setPadding(8, 8, 8, 8)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_green_dark
                        )
                    )
                }
                container.addView(tv)
            }
        }
    }
}
