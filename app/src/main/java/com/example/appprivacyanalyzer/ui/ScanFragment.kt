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
import androidx.lifecycle.lifecycleScope
import com.example.appprivacyanalyzer.R
import com.example.appprivacyanalyzer.data.ApkFileSignatureUtils
import com.example.appprivacyanalyzer.data.ApkSignatureUtils
import com.example.appprivacyanalyzer.data.MaliciousSignerRepository
import com.example.appprivacyanalyzer.model.RiskLevel
import com.example.appprivacyanalyzer.scanner.AppScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanFragment : Fragment() {

    private lateinit var resultContainer: LinearLayout
    private lateinit var tvAppName: TextView
    private lateinit var tvPackage: TextView
    private lateinit var tvRisks: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvSignature: TextView

    private val apkPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { scanApk(it) }
        }

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

        v.findViewById<Button>(R.id.btnScanApk).setOnClickListener {
            apkPicker.launch("application/vnd.android.package-archive")
        }

        v.findViewById<Button>(R.id.btnScanInstalled).setOnClickListener {
            installedAppPicker.launch(
                Intent(requireContext(), InstalledAppsActivity::class.java)
            )
        }

        resultContainer = v.findViewById(R.id.resultContainer)
        tvAppName = v.findViewById(R.id.tvAppName)
        tvPackage = v.findViewById(R.id.tvPackage)
        tvRisks = v.findViewById(R.id.tvRisks)
        tvScore = v.findViewById(R.id.tvScore)
        tvSignature = v.findViewById(R.id.tvSignature)

        return v
    }

    // ---------------- APK FILE SCAN (SIGNATURE FROM APK FILE) ----------------
    private fun scanApk(uri: Uri) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                AppScanner(requireContext()).scanApk(uri)
            }

            showApkFileResult(
                result.appName,
                result.risks,
                result.riskScore,
                uri
            )
        }
    }

    // ---------------- INSTALLED APP SCAN ----------------
    private fun scanInstalledApp(packageName: String) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                AppScanner(requireContext()).scanInstalledApp(packageName)
            }

            showInstalledAppResult(
                result.appName,
                result.packageName,
                result.risks,
                result.riskScore
            )
        }
    }

    // ---------------- APK FILE RESULT ----------------
    private fun showApkFileResult(
        appName: String,
        risks: List<String>,
        baseScore: Int,
        apkUri: Uri
    ) {
        resultContainer.visibility = View.VISIBLE
        tvAppName.text = appName
        tvPackage.text = "APK File (Not Installed)"
        tvRisks.text = risks.joinToString("\n")

        var finalScore = baseScore

        val signerInfo =
            ApkFileSignatureUtils.getSignerInfoFromApk(requireContext(), apkUri)

        if (signerInfo != null) {
            val (signerHash, isSelfSigned) = signerInfo
            val malicious =
                MaliciousSignerRepository(requireContext()).loadHashes()

            tvSignature.text = when {
                malicious.contains(signerHash) -> {
                    finalScore += 50
                    "Signature Verification:\n🔴 Malicious Signer\nHash: ${signerHash.take(12)}..."
                }
                isSelfSigned -> {
                    finalScore += 25
                    "Signature Verification:\n🟠 Self-Signed Certificate\nHash: ${signerHash.take(12)}..."
                }
                else -> {
                    "Signature Verification:\n🟢 Trusted Signer\nHash: ${signerHash.take(12)}..."
                }
            }
        } else {
            tvSignature.text =
                "Signature Verification:\n⚠ Unable to extract signature"
        }

        tvScore.text = "Risk Score: $finalScore (${getRiskLevel(finalScore)})"
    }

    // ---------------- INSTALLED APP RESULT ----------------
    private fun showInstalledAppResult(
        appName: String,
        packageName: String,
        risks: List<String>,
        baseScore: Int
    ) {
        resultContainer.visibility = View.VISIBLE
        tvAppName.text = appName
        tvPackage.text = packageName
        tvRisks.text = risks.joinToString("\n")

        var finalScore = baseScore

        val signerInfo =
            ApkSignatureUtils.getSignerInfo(requireContext(), packageName)

        if (signerInfo != null) {
            val (signerHash, isSelfSigned) = signerInfo
            val malicious =
                MaliciousSignerRepository(requireContext()).loadHashes()

            tvSignature.text = when {
                malicious.contains(signerHash) -> {
                    finalScore += 50
                    "Signature Verification:\n🔴 Malicious Signer\nHash: ${signerHash.take(12)}..."
                }
                isSelfSigned -> {
                    finalScore += 25
                    "Signature Verification:\n🟠 Self-Signed Certificate\nHash: ${signerHash.take(12)}..."
                }
                else -> {
                    "Signature Verification:\n🟢 Trusted Signer\nHash: ${signerHash.take(12)}..."
                }
            }
        }

        tvScore.text = "Risk Score: $finalScore (${getRiskLevel(finalScore)})"
    }

    private fun getRiskLevel(score: Int): String =
        when {
            score >= 75 -> RiskLevel.HIGH.name
            score >= 40 -> RiskLevel.MEDIUM.name
            else -> RiskLevel.LOW.name
        }
}
