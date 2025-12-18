package com.example.appprivacyanalyzer.scanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appprivacyanalyzer.R

class ScanAdapter(
    private var results: List<ScanResult>
) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {

    class ScanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvPackageName: TextView = view.findViewById(R.id.tvPackageName)
        val tvRiskScore: TextView = view.findViewById(R.id.tvRiskScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_result, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val item = results[position]

        holder.tvAppName.text = item.appName
        holder.tvPackageName.text = item.packageName
        holder.tvRiskScore.text = "${item.riskLevel} (${item.riskScore})"
    }

    override fun getItemCount(): Int = results.size

    fun updateData(newResults: List<ScanResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}
