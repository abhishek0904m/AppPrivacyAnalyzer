package com.example.appprivacyanalyzer.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.model.RiskLevel
import com.example.appprivacyanalyzer.scanner.AppScanner

class ScanFragment : Fragment() {

    private lateinit var resultContainer: LinearLayout
    private lateinit var tvAppName: TextView
    private lateinit var tvPackage: TextView
    private lateinit var tvRisks: TextView
    private lateinit var tvScore: TextView

    // APK picker
    private val apkPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { scanApk(it) }
        }

    // Installed app picker (OUR activity)
    private val installedAppPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pkg = result.data?.getStringExtra("pkg") ?: return@registerForActivityResult
                scanInstalledApp(pkg)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_scan, container, false)

        val btnScanApk = v.findViewById<Button>(R.id.btnScanApk)
        val btnScanInstalled = v.findViewById<Button>(R.id.btnScanInstalled)

        resultContainer = v.findViewById(R.id.resultContainer)
        tvAppName = v.findViewById(R.id.tvAppName)
        tvPackage = v.findViewById(R.id.tvPackage)
        tvRisks = v.findViewById(R.id.tvRisks)
        tvScore = v.findViewById(R.id.tvScore)

        btnScanApk.setOnClickListener {
            apkPicker.launch("application/vnd.android.package-archive")
        }

        btnScanInstalled.setOnClickListener {
            installedAppPicker.launch(
                Intent(requireContext(), InstalledAppsActivity::class.java)
            )
        }

        return v
    }

    private fun scanApk(uri: Uri) {
        val scanner = AppScanner(requireContext())
        val result = scanner.scanApk(uri)

        showResult(
            result.appName,
            result.packageName,
            result.risks,
            result.riskScore,
            result.riskLevel
        )
    }

    private fun scanInstalledApp(packageName: String) {
        val scanner = AppScanner(requireContext())
        val result = scanner.scanInstalledApp(packageName)

        showResult(
            result.appName,
            result.packageName,
            result.risks,
            result.riskScore,
            result.riskLevel
        )
    }

    private fun showResult(
        appName: String,
        pkg: String,
        risks: List<String>,
        score: Int,
        level: RiskLevel
    ) {
        resultContainer.visibility = View.VISIBLE
        tvAppName.text = appName
        tvPackage.text = pkg
        tvRisks.text = risks.joinToString("\n")
        tvScore.text = "Risk Score: $score (${level.name})"
    }
}
