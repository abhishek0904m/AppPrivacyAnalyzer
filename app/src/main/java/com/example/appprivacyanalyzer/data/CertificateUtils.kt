package com.example.appprivacyanalyzer.data

import android.content.pm.Signature
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object CertificateUtils {
    fun signatureToX509(signature: Signature): X509Certificate {
        val factory = CertificateFactory.getInstance("X509")
        return factory.generateCertificate(
            ByteArrayInputStream(signature.toByteArray())
        ) as X509Certificate
    }
}
