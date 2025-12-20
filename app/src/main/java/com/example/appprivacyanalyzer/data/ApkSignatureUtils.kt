package com.example.appprivacyanalyzer.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.example.appprivacyanalyzer.utils.CryptoUtils

object ApkSignatureUtils {

    fun getSignerInfo(
        context: Context,
        packageName: String
    ): Pair<String, Boolean>? {

        val pm = context.packageManager

        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pm.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
        }

        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pkgInfo.signingInfo.apkContentsSigners
        } else {
            @Suppress("DEPRECATION")
            pkgInfo.signatures
        }

        if (signatures.isEmpty()) return null

        val cert = CertificateUtils.signatureToX509(signatures[0])
        val sha256 = CryptoUtils.sha256(cert.encoded)
        val isSelfSigned = cert.subjectDN == cert.issuerDN

        return Pair(sha256, isSelfSigned)
    }
}
