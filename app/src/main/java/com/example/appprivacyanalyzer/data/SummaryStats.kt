package com.example.appprivacyanalyzer.data

/**
 * Single canonical SummaryStats.
 * Make sure there are no other files that declare SummaryStats anywhere else.
 */
data class SummaryStats(
    val totalApps: Int = 0,
    val highRiskCount: Int = 0,
    val mediumRiskCount: Int = 0,
    val lowRiskCount: Int = 0,
    val cameraApps: Int = 0,
    val micApps: Int = 0,
    val locationApps: Int = 0
)
