package com.example.appprivacyanalyzer.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.scanner.AppScanner

class InstalledAppsActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var adapter: InstalledAppsAdapter
    private var allApps: List<AppInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installed_apps)

        recycler = findViewById(R.id.rvInstalledApps)
        etSearch = findViewById(R.id.etSearch)
        
        recycler.layoutManager = LinearLayoutManager(this)

        val scanner = AppScanner(this)
        // Get only user-installed apps (not system apps)
        allApps = scanner.getInstalledApps().filter { !it.isSystemApp }

        adapter = InstalledAppsAdapter(allApps) { app ->
            val resultIntent = Intent()
            resultIntent.putExtra("pkg", app.packageName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        recycler.adapter = adapter

        setupSearch()
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterApps(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterApps(query: String) {
        val filteredApps = if (query.isEmpty()) {
            allApps
        } else {
            allApps.filter { app ->
                app.appName.contains(query, ignoreCase = true) ||
                app.packageName.contains(query, ignoreCase = true)
            }
        }
        adapter.updateData(filteredApps)
    }
}
