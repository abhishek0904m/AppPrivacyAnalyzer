package com.example.appprivacyanalyzer.data

object PermissionDescriptions {

    private val descriptions = mapOf(
        android.Manifest.permission.CAMERA to "Allows the app to take photos and record video.",
        android.Manifest.permission.RECORD_AUDIO to "Allows the app to record audio using the microphone.",
        android.Manifest.permission.ACCESS_FINE_LOCATION to "Allows the app to access precise location (GPS).",
        android.Manifest.permission.ACCESS_COARSE_LOCATION to "Allows the app to access approximate location.",
        android.Manifest.permission.READ_CONTACTS to "Allows the app to read your contacts.",
        android.Manifest.permission.WRITE_CONTACTS to "Allows the app to modify your contacts.",
        android.Manifest.permission.READ_SMS to "Allows the app to read your SMS messages and OTPs.",
        android.Manifest.permission.SEND_SMS to "Allows the app to send SMS messages.",
        android.Manifest.permission.RECEIVE_SMS to "Allows the app to receive SMS messages.",
        android.Manifest.permission.READ_PHONE_STATE to "Allows the app to read phone state and ID.",
        android.Manifest.permission.CALL_PHONE to "Allows the app to initiate phone calls without your intervention.",
        android.Manifest.permission.READ_CALL_LOG to "Allows the app to read your call log.",
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE to "Allows the app to write to external storage.",
        android.Manifest.permission.READ_EXTERNAL_STORAGE to "Allows the app to read from external storage.",
        android.Manifest.permission.BODY_SENSORS to "Allows the app to access body sensor data such as heart rate."
    )

    fun getDescription(permission: String): String {
        return descriptions[permission] ?: "No detailed description available for this permission."
    }
}
