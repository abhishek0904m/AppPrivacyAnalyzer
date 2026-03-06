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
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnOpenSettings).setOnClickListener {
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

        // Use a Set to track already added permissions and avoid duplicates
        val addedPermissions = mutableSetOf<String>()

        permissions.forEachIndexed { index, permission ->
            val granted =
                flags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0

            if (!granted) return@forEachIndexed

            val label = when {
                permission.contains("CAMERA") && !addedPermissions.contains("CAMERA") -> {
                    addedPermissions.add("CAMERA")
                    "📷 Camera access granted"
                }
                permission.contains("RECORD_AUDIO") && !addedPermissions.contains("MICROPHONE") -> {
                    addedPermissions.add("MICROPHONE")
                    "🎤 Microphone access granted"
                }
                permission.contains("LOCATION") && !addedPermissions.contains("LOCATION") -> {
                    addedPermissions.add("LOCATION")
                    "📍 Location access granted"
                }
                permission.contains("READ_CONTACTS") && !addedPermissions.contains("CONTACTS") -> {
                    addedPermissions.add("CONTACTS")
                    "📇 Contacts access granted"
                }
                permission.contains("READ_SMS") && !addedPermissions.contains("SMS") -> {
                    addedPermissions.add("SMS")
                    "📩 SMS access granted"
                }
                permission.contains("READ_EXTERNAL_STORAGE") && !addedPermissions.contains("STORAGE") -> {
                    addedPermissions.add("STORAGE")
                    "💾 Storage access granted"
                }
                permission.contains("READ_PHONE_STATE") && !addedPermissions.contains("PHONE") -> {
                    addedPermissions.add("PHONE")
                    "📱 Phone state access granted"
                }
                else -> null
            }

            label?.let {
                val tv = TextView(this).apply {
                    text = it
                    textSize = 15f
                    setPadding(16, 12, 16, 12)
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_green_dark
                        )
                    )
                    background = ContextCompat.getDrawable(context, R.drawable.permission_item_bg)
                }
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 12)
                }
                tv.layoutParams = params
                container.addView(tv)
            }
        }

        // Show message if no sensitive permissions found
        if (addedPermissions.isEmpty()) {
            val tv = TextView(this).apply {
                text = "✅ No sensitive permissions granted"
                textSize = 15f
                setPadding(16, 12, 16, 12)
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.darker_gray
                    )
                )
            }
            container.addView(tv)
        }
    }
}
