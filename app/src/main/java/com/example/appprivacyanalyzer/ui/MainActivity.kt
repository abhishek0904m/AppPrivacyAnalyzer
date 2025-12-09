package com.example.appprivacyanalyzer.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.data.AppRepository
import com.example.appprivacyanalyzer.data.SummaryStats
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var repository: AppRepository
    private lateinit var adapter: AppAdapter
    private var allApps: List<AppInfo> = emptyList()

    private lateinit var tvSummary: TextView
    private lateinit var tvHighRiskCount: TextView
    private lateinit var tvMediumRiskCount: TextView
    private lateinit var tvLowRiskCount: TextView
    private lateinit var tvCameraApps: TextView
    private lateinit var tvMicApps: TextView
    private lateinit var tvLocationApps: TextView
    private lateinit var etSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var spinnerAppType: Spinner
    private lateinit var rvApps: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = AppRepository(this)

        tvSummary = findViewById(R.id.tvSummary)
        tvHighRiskCount = findViewById(R.id.tvHighRiskCount)
        tvMediumRiskCount = findViewById(R.id.tvMediumRiskCount)
        tvLowRiskCount = findViewById(R.id.tvLowRiskCount)
        tvCameraApps = findViewById(R.id.tvCameraApps)
        tvMicApps = findViewById(R.id.tvMicApps)
        tvLocationApps = findViewById(R.id.tvLocationApps)
        etSearch = findViewById(R.id.etSearch)
        spinnerFilter = findViewById(R.id.spinnerFilter)
        spinnerAppType = findViewById(R.id.spinnerAppType)
        rvApps = findViewById(R.id.rvApps)

        rvApps.layoutManager = LinearLayoutManager(this)
        adapter = AppAdapter(emptyList()) { app ->
            openDetailScreen(app)
        }
        rvApps.adapter = adapter

        loadData()
        setupSearch()
        setupFilters()
    }

    private fun openDetailScreen(app: AppInfo) {
        val intent = Intent(this, AppDetailActivity::class.java)
        intent.putExtra(AppDetailActivity.EXTRA_APP, app)
        startActivity(intent)
    }

    private fun loadData() {
        allApps = repository.loadApps()
        adapter.updateData(allApps)
        val stats = repository.computeSummary(allApps)
        updateSummary(stats)
    }

    private fun updateSummary(stats: SummaryStats) {
        tvSummary.text =
            "Total apps analyzed: ${stats.totalApps}\n" +
                    "High: ${stats.highRiskCount}   Medium: ${stats.mediumRiskCount}   Low: ${stats.lowRiskCount}"

        tvHighRiskCount.text = "High: ${stats.highRiskCount}"
        tvMediumRiskCount.text = "Medium: ${stats.mediumRiskCount}"
        tvLowRiskCount.text = "Low: ${stats.lowRiskCount}"
        tvCameraApps.text = "Camera: ${stats.cameraApps}"
        tvMicApps.text = "Mic: ${stats.micApps}"
        tvLocationApps.text = "Location: ${stats.locationApps}"
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupFilters() {
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                applyFilters()
            }
        }
        spinnerFilter.onItemSelectedListener = listener
        spinnerAppType.onItemSelectedListener = listener
    }

    private fun applyFilters() {
        val query = etSearch.text.toString().trim().lowercase()
        val riskIndex = spinnerFilter.selectedItemPosition
        val typeIndex = spinnerAppType.selectedItemPosition

        val filtered = allApps.filter { app ->
            val matchesQuery =
                app.appName.lowercase().contains(query) ||
                        app.packageName.lowercase().contains(query)

            val matchesRisk = when (riskIndex) {
                1 -> app.riskLevel == RiskLevel.HIGH
                2 -> app.riskLevel == RiskLevel.MEDIUM
                3 -> app.riskLevel == RiskLevel.LOW
                else -> true
            }

            val matchesType = when (typeIndex) {
                1 -> !app.isSystemApp
                2 -> app.isSystemApp
                else -> true
            }

            matchesQuery && matchesRisk && matchesType
        }

        adapter.updateData(filtered)
        updateSummary(repository.computeSummary(filtered))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_export -> {
                exportReport()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exportReport() {
        if (allApps.isEmpty()) {
            Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show()
            return
        }
        val dir = getExternalFilesDir(null) ?: filesDir
        val file = File(dir, "privacy_report.txt")
        file.printWriter().use { out ->
            out.println("App Privacy Analyzer Report")
            out.println("============================")
            out.println("Total apps: ${allApps.size}")
            out.println()
            allApps.forEach { app ->
                out.println("App: ${app.appName}")
                out.println("Package: ${app.packageName}")
                out.println("Risk: ${app.riskLevel} (${app.riskScore})")
                out.println("Dangerous permissions:")
                app.dangerousPermissions.forEach { perm ->
                    out.println(" - $perm")
                }
                out.println()
            }
        }
        Toast.makeText(this, "Report saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}
