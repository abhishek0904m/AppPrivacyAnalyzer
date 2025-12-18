package com.example.appprivacyanalyzer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.data.AppRepository
import com.example.appprivacyanalyzer.data.SummaryStats
import com.example.appprivacyanalyzer.model.AppInfo
import com.example.appprivacyanalyzer.model.RiskLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "APD"

class AppsFragment : Fragment() {

    private lateinit var repository: AppRepository
    private lateinit var adapter: AppAdapter
    private var allApps: List<AppInfo> = emptyList()

    // Views
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
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingOverlay: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_apps_list, container, false)

        repository = AppRepository(requireContext())

        // Bind views
        tvSummary = view.findViewById(R.id.tvSummary)
        tvHighRiskCount = view.findViewById(R.id.tvHighRiskCount)
        tvMediumRiskCount = view.findViewById(R.id.tvMediumRiskCount)
        tvLowRiskCount = view.findViewById(R.id.tvLowRiskCount)
        tvCameraApps = view.findViewById(R.id.tvCameraApps)
        tvMicApps = view.findViewById(R.id.tvMicApps)
        tvLocationApps = view.findViewById(R.id.tvLocationApps)
        etSearch = view.findViewById(R.id.etSearch)
        spinnerFilter = view.findViewById(R.id.spinnerFilter)
        spinnerAppType = view.findViewById(R.id.spinnerAppType)
        rvApps = view.findViewById(R.id.rvApps)
        progressBar = view.findViewById(R.id.progressBar)
        loadingOverlay = view.findViewById(R.id.loadingOverlay)

        // Setup RecyclerView
        rvApps.layoutManager = LinearLayoutManager(requireContext())
        adapter = AppAdapter(emptyList()) { app ->
            val intent = Intent(requireContext(), AppDetailActivity::class.java).apply {
                putExtra(AppDetailActivity.EXTRA_PACKAGE_NAME, app.packageName)
                putExtra(AppDetailActivity.EXTRA_APP_NAME, app.appName)
            }
            startActivity(intent)
        }
        rvApps.adapter = adapter

        setupSpinners()
        setupSearch()
        setupFilters()
        loadDataAsync()

        return view
    }

    // ---------------- SPINNERS ----------------

    private fun setupSpinners() {

        // Risk filter spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.risk_filter,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            spinnerFilter.adapter = it
        }

        // App type filter spinner (THIS FIXES EMPTY DROPDOWN)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.app_type_filter,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            spinnerAppType.adapter = it
        }
    }

    private fun setupFilters() {
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
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

    // ---------------- SEARCH ----------------

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // ---------------- DATA LOADING ----------------

    private fun showLoading(show: Boolean) {
        loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun loadDataAsync() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val apps = withContext(Dispatchers.IO) {
                    repository.loadApps()
                }
                allApps = apps
                adapter.updateData(allApps)
                updateSummary(repository.computeSummary(allApps))
            } catch (e: Exception) {
                Log.e(TAG, "Load failed", e)
            } finally {
                showLoading(false)
            }
        }
    }

    // ---------------- SUMMARY ----------------

    private fun updateSummary(stats: SummaryStats) {
        tvSummary.text = getString(R.string.total_apps_analyzed, stats.totalApps)
        tvHighRiskCount.text = "High: ${stats.highRiskCount}"
        tvMediumRiskCount.text = "Medium: ${stats.mediumRiskCount}"
        tvLowRiskCount.text = "Low: ${stats.lowRiskCount}"
        tvCameraApps.text = "Camera: ${stats.cameraApps}"
        tvMicApps.text = "Mic: ${stats.micApps}"
        tvLocationApps.text = "Location: ${stats.locationApps}"
    }

    // ---------------- FILTER LOGIC ----------------

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
                1 -> !app.isSystemApp   // User apps
                2 -> app.isSystemApp    // System apps
                else -> true            // All apps
            }

            matchesQuery && matchesRisk && matchesType
        }

        adapter.updateData(filtered)
        updateSummary(repository.computeSummary(filtered))
    }
}
