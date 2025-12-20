package com.example.appprivacyanalyzer.model

data class ApkSignatureInfo(
    val signerSha256: String,
    val isSelfSigned: Boolean,
    val isMaliciousSigner: Boolean
)
