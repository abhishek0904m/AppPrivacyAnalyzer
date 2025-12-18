package com.example.appprivacyanalyzer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.scanner.AppScanner

class InstalledAppsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installed_apps)

        val recycler = findViewById<RecyclerView>(R.id.rvInstalledApps)
        recycler.layoutManager = LinearLayoutManager(this)

        val scanner = AppScanner(this)
        val apps: List<AppInfo> = scanner.getInstalledApps()

        recycler.adapter = InstalledAppsAdapter(apps) { app ->
            // ✅ ONLY return package name
            val resultIntent = Intent()
            resultIntent.putExtra("pkg", app.packageName)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
