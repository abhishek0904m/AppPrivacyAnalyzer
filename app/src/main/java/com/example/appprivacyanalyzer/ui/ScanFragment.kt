package com.example.appprivacyanalyzer.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        v.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnScanApk).setOnClickListener {
            apkPicker.launch("application/vnd.android.package-archive")
        }

        v.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnScanInstalled).setOnClickListener {
            installedAppPicker.launch(
                Intent(requireContext(), InstalledAppsActivity::class.java)
            )
        }

        return v
    }

    // ---------------- APK FILE SCAN (SIGNATURE FROM APK FILE) ----------------
    private fun scanApk(uri: Uri) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                AppScanner(requireContext()).scanApk(uri)
            }

            showResultDialog(
                result.appName,
                "📦 APK File (Not Installed)",
                result.risks,
                result.riskScore,
                uri,
                isApkFile = true
            )
        }
    }

    // ---------------- INSTALLED APP SCAN ----------------
    private fun scanInstalledApp(packageName: String) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                AppScanner(requireContext()).scanInstalledApp(packageName)
            }

            showResultDialog(
                result.appName,
                result.packageName,
                result.risks,
                result.riskScore,
                packageName = packageName,
                isApkFile = false
            )
        }
    }

    // ---------------- SHOW RESULT DIALOG ----------------
    private fun showResultDialog(
        appName: String,
        packageInfo: String,
        risks: List<String>,
        baseScore: Int,
        uri: Uri? = null,
        packageName: String? = null,
        isApkFile: Boolean
    ) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_scan_result)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val tvAppName = dialog.findViewById<TextView>(R.id.tvAppName)
        val tvPackage = dialog.findViewById<TextView>(R.id.tvPackage)
        val tvRisks = dialog.findViewById<TextView>(R.id.tvRisks)
        val tvScore = dialog.findViewById<TextView>(R.id.tvScore)
        val tvSignature = dialog.findViewById<TextView>(R.id.tvSignature)
        val btnClose = dialog.findViewById<View>(R.id.btnClose)
        val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog)

        tvAppName.text = appName
        tvPackage.text = packageInfo

        var finalScore = baseScore

        if (isApkFile) {
            // APK File Result
            val formattedRisks = if (risks.isEmpty() || risks.first() == "No risky permissions found") {
                "✅ No sensitive permissions detected"
            } else {
                "⚠️ REQUESTED PERMISSIONS:\n" + risks.joinToString("\n") { "   $it" }
            }
            tvRisks.text = formattedRisks

            uri?.let {
                val signerInfo = ApkFileSignatureUtils.getSignerInfoFromApk(requireContext(), it)
                if (signerInfo != null) {
                    val (signerHash, isSelfSigned) = signerInfo
                    val malicious = MaliciousSignerRepository(requireContext()).loadHashes()

                    tvSignature.text = when {
                        malicious.contains(signerHash) -> {
                            finalScore += 50
                            "🔴 MALICIOUS SIGNER DETECTED!\n⚠️ DO NOT INSTALL THIS APK\nHash: ${signerHash.take(16)}..."
                        }
                        isSelfSigned -> {
                            finalScore += 25
                            "🟠 Self-Signed Certificate\n⚠️ Not from official store\nHash: ${signerHash.take(16)}..."
                        }
                        else -> {
                            "🟢 Trusted Signer\n✅ Verified certificate\nHash: ${signerHash.take(16)}..."
                        }
                    }
                } else {
                    tvSignature.text = "⚠️ Unable to extract signature from APK"
                }
            }
        } else {
            // Installed App Result
            packageName?.let { pkg ->
                val pm = requireContext().packageManager
                val pkgInfo = pm.getPackageInfo(pkg, android.content.pm.PackageManager.GET_PERMISSIONS)
                val requestedPerms = pkgInfo.requestedPermissions ?: emptyArray()
                val permFlags = pkgInfo.requestedPermissionsFlags ?: IntArray(0)

                // Get installation source
                val installerPackage = try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        pm.getInstallSourceInfo(pkg).installingPackageName
                    } else {
                        @Suppress("DEPRECATION")
                        pm.getInstallerPackageName(pkg)
                    }
                } catch (e: Exception) {
                    null
                }

                val permissionReport = buildString {
                    val sensitivePerms = mutableListOf<Triple<String, Boolean, String>>()

                    requestedPerms.forEachIndexed { index, perm ->
                        val granted = if (index < permFlags.size) {
                            (permFlags[index] and android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
                        } else {
                            false
                        }
                        
                        when {
                            perm.contains("CAMERA") -> sensitivePerms.add(Triple("📷 Camera", granted, "Take photos and record videos"))
                            perm.contains("RECORD_AUDIO") -> sensitivePerms.add(Triple("🎤 Microphone", granted, "Record audio and voice"))
                            perm.contains("ACCESS_FINE_LOCATION") -> sensitivePerms.add(Triple("📍 Fine Location (GPS)", granted, "Access precise location"))
                            perm.contains("ACCESS_COARSE_LOCATION") -> sensitivePerms.add(Triple("📍 Coarse Location", granted, "Access approximate location"))
                            perm.contains("READ_CONTACTS") -> sensitivePerms.add(Triple("📇 Read Contacts", granted, "Access your contact list"))
                            perm.contains("WRITE_CONTACTS") -> sensitivePerms.add(Triple("📇 Write Contacts", granted, "Modify your contacts"))
                            perm.contains("READ_SMS") -> sensitivePerms.add(Triple("📩 Read SMS", granted, "Read text messages and OTPs"))
                            perm.contains("SEND_SMS") -> sensitivePerms.add(Triple("📩 Send SMS", granted, "Send text messages"))
                            perm.contains("READ_CALL_LOG") -> sensitivePerms.add(Triple("📞 Read Call Log", granted, "Access call history"))
                            perm.contains("READ_PHONE_STATE") -> sensitivePerms.add(Triple("📱 Phone State", granted, "Access device ID and phone info"))
                            perm.contains("READ_EXTERNAL_STORAGE") -> sensitivePerms.add(Triple("💾 Read Storage", granted, "Access files and media"))
                            perm.contains("WRITE_EXTERNAL_STORAGE") -> sensitivePerms.add(Triple("💾 Write Storage", granted, "Modify files and media"))
                        }
                    }

                    if (sensitivePerms.isEmpty()) {
                        append("✅ No sensitive permissions requested\n\nThis app doesn't request access to sensitive device features.")
                    } else {
                        val granted = sensitivePerms.filter { it.second }
                        val denied = sensitivePerms.filter { !it.second }

                        if (granted.isNotEmpty()) {
                            append("✅ GRANTED PERMISSIONS:\n")
                            append("The app currently has access to:\n\n")
                            granted.forEach { 
                                append("${it.first}\n")
                                append("   └ ${it.third}\n\n")
                            }
                        }

                        if (denied.isNotEmpty()) {
                            if (granted.isNotEmpty()) append("\n")
                            append("❌ REQUESTED BUT DENIED:\n")
                            append("The app requested but you denied:\n\n")
                            denied.forEach { 
                                append("${it.first}\n")
                                append("   └ ${it.third}\n\n")
                            }
                        }

                        if (granted.isEmpty() && denied.isNotEmpty()) {
                            append("\n🛡️ Good! You've denied all sensitive permissions.")
                        }
                    }
                }

                tvRisks.text = permissionReport

                // Get actual signature information
                val signerInfo = try {
                    ApkSignatureUtils.getSignerInfo(requireContext(), pkg)
                } catch (e: Exception) {
                    android.util.Log.e("ScanFragment", "Error getting signature for $pkg", e)
                    null
                }
                
                val sourceText = when (installerPackage) {
                    "com.android.vending" -> "✅ Installed from Google Play Store"
                    "com.amazon.venezia" -> "✅ Installed from Amazon Appstore"
                    "com.sec.android.app.samsungapps" -> "✅ Installed from Samsung Galaxy Store"
                    "com.huawei.appmarket" -> "✅ Installed from Huawei AppGallery"
                    null -> "⚠️ Unknown source (Sideloaded)"
                    else -> "📋 Installed via: ${installerPackage.substringAfterLast('.')}"
                }

                val signatureText = if (signerInfo != null) {
                    val (signerHash, isSelfSigned) = signerInfo
                    val malicious = try {
                        MaliciousSignerRepository(requireContext()).loadHashes()
                    } catch (e: Exception) {
                        android.util.Log.e("ScanFragment", "Error loading malicious hashes", e)
                        emptySet()
                    }

                    when {
                        malicious.contains(signerHash) -> {
                            finalScore += 50
                            "🔴 Malicious Signer Detected!\n⚠️ This app may be dangerous"
                        }
                        isSelfSigned -> {
                            when (installerPackage) {
                                "com.android.vending" -> {
                                    // Play Store apps are self-signed but verified by Google
                                    "🔒 Digitally signed (Self-signed)\n✓ Verified by Google"
                                }
                                "com.amazon.venezia" -> {
                                    "🔒 Digitally signed (Self-signed)\n✓ Verified by Amazon"
                                }
                                else -> {
                                    finalScore += 15
                                    "🟠 Self-Signed Certificate\n⚠️ Not from official store"
                                }
                            }
                        }
                        else -> {
                            "🔒 Digitally signed\n✓ Verified certificate"
                        }
                    }
                } else {
                    "⚠️ Unable to verify signature\n(Signature extraction failed)"
                }

                val hashText = if (signerInfo != null) {
                    "\nHash: ${signerInfo.first.take(16)}..."
                } else {
                    ""
                }

                tvSignature.text = "$sourceText\n$signatureText$hashText"

                // Adjust risk score based on installation source
                when (installerPackage) {
                    "com.android.vending", "com.amazon.venezia", "com.sec.android.app.samsungapps" -> {
                        // Trusted sources - no additional penalty beyond signature analysis
                    }
                    null -> {
                        // Sideloaded - add risk if not already added by signature
                        if (signerInfo?.second != true) finalScore += 15
                    }
                    else -> {
                        // Other installers - small penalty
                        finalScore += 5
                    }
                }
            }
        }

        val riskColor = when {
            finalScore >= 75 -> "#DC2626"
            finalScore >= 40 -> "#F59E0B"
            else -> "#16A34A"
        }
        
        tvScore.text = android.text.SpannableStringBuilder().apply {
            append("RISK SCORE: ")
            val scoreText = "$finalScore / 100"
            val start = length
            append(scoreText)
            setSpan(
                android.text.style.ForegroundColorSpan(android.graphics.Color.parseColor(riskColor)),
                start,
                length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append("\nRisk Level: ${getRiskLevel(finalScore)}")
        }

        btnClose.setOnClickListener { dialog.dismiss() }
        btnCloseDialog.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun getRiskLevel(score: Int): String =
        when {
            score >= 75 -> RiskLevel.HIGH.name
            score >= 40 -> RiskLevel.MEDIUM.name
            else -> RiskLevel.LOW.name
        }
}