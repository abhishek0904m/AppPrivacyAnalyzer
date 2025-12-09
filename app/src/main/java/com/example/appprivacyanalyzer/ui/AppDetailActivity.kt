package com.example.appprivacyanalyzer.ui

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo

class AppDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_APP = "extra_app"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        // IDs must match your layout (appIcon, appName, appPackage, riskBadge, permissionsContainer, managePermissionsBtn)
        val ivIcon = findViewById<android.widget.ImageView>(R.id.appIcon)
        val tvAppName = findViewById<TextView>(R.id.appName)
        val tvPkg = findViewById<TextView>(R.id.appPackage)
        val tvRisk = findViewById<TextView>(R.id.riskBadge)
        val llPermissionList = findViewById<LinearLayout>(R.id.permissionsContainer)
        val btnManage = findViewById<Button>(R.id.managePermissionsBtn)

        // getParcelableExtra is deprecated but works; keep it simple
        val app = intent.getParcelableExtra<AppInfo>(EXTRA_APP)
        if (app == null) {
            Toast.makeText(this, "Failed to load app", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // load icon safely (fallback to default if not found)
        val icon: Drawable = try {
            packageManager.getApplicationIcon(app.packageName)
        } catch (e: Exception) {
            ContextCompat.getDrawable(this, android.R.mipmap.sym_def_app_icon)!!
        }
        ivIcon.setImageDrawable(icon)

        tvAppName.text = app.appName
        tvPkg.text = app.packageName
        tvRisk.text = "${app.riskLevel} (${app.riskScore})"

        // Populate dangerous permissions list (only the granted dangerous permissions)
        llPermissionList.removeAllViews()
        if (app.dangerousPermissions.isEmpty()) {
            val none = TextView(this).apply {
                text = "No dangerous permissions granted."
                setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                setTextColor(ContextCompat.getColor(this@AppDetailActivity, R.color.text_secondary))
                val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams = lp
            }
            llPermissionList.addView(none)
        } else {
            // Title inside box
            val title = TextView(this).apply {
                text = "Granted Permissions:"
                setTypeface(null, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(this@AppDetailActivity, R.color.text_primary))
                val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.bottomMargin = dp(8)
                layoutParams = lp
            }
            llPermissionList.addView(title)

            for (perm in app.dangerousPermissions) {
                val tv = TextView(this).apply {
                    text = "•  $perm"
                    setTextColor(ContextCompat.getColor(this@AppDetailActivity, R.color.text_primary))
                    setPadding(dp(12), dp(10), dp(12), dp(10))
                    background = ContextCompat.getDrawable(this@AppDetailActivity, R.drawable.permission_item_bg)
                    val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    lp.bottomMargin = dp(8)
                    layoutParams = lp
                }
                llPermissionList.addView(tv)
            }
        }

        btnManage.setOnClickListener {
            openAppPermissions(app.packageName)
        }
    }

    private fun openAppPermissions(packageName: String) {
        // 1) Try direct permissions manager intent (use literal action + literal extra key to avoid unresolved constants)
        try {
            val permIntent = Intent("android.settings.MANAGE_APP_PERMISSIONS")
            // extra key used by platform
            permIntent.putExtra("android.provider.extra.PACKAGE_NAME", packageName)
            // set as new task (equivalent to addFlags)
            permIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(permIntent)
            return
        } catch (e: Exception) {
            // fall through to fallback
        }

        // 2) Fallback to app details settings (works on all devices)
        try {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", packageName, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(this, "Unable to open settings", Toast.LENGTH_SHORT).show()
        }
    }

    // helper: convert dp to px
    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
