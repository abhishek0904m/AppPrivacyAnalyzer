package com.example.appprivacyanalyzer.data

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.example.appprivacyanalyzer.utils.CryptoUtils
import java.io.File

object ApkFileSignatureUtils {

    fun getSignerInfoFromApk(
        context: Context,
        apkUri: Uri
    ): Pair<String, Boolean>? {

        val pm = context.packageManager

        val apkFile = copyApkToCache(context, apkUri) ?: return null

        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pm.getPackageArchiveInfo(
                apkFile.absolutePath,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageArchiveInfo(
                apkFile.absolutePath,
                PackageManager.GET_SIGNATURES
            )
        } ?: return null

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

    private fun copyApkToCache(context: Context, uri: Uri): File? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "temp_scan.apk")
            input.use { inputStream ->
                file.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            null
        }
    }
}
