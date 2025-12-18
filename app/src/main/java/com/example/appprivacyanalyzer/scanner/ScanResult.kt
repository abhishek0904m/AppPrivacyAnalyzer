package com.example.appprivacyanalyzer.scanner

import com.example.appprivacyanalyzer.model.RiskLevel

data class ScanResult(
    val appName: String,
    val packageName: String,
    val risks: List<String>,
    val riskScore: Int,
    val riskLevel: RiskLevel
)
