package com.example.appprivacyanalyzer.data

import android.content.Context
import org.json.JSONArray

class MaliciousSignerRepository(private val context: Context) {

    fun loadHashes(): Set<String> {
        val json = context.assets.open("malicious_signers.json")
            .bufferedReader()
            .use { it.readText() }

        val array = JSONArray(json)
        val set = mutableSetOf<String>()

        for (i in 0 until array.length()) {
            set.add(array.getString(i))
        }
        return set
    }
}
