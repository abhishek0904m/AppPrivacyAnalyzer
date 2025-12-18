🛡️ App Privacy Analyzer (Pepper)

An Android-based cybersecurity application for analyzing app privacy risks and detecting potentially malicious behavior using static analysis and security heuristics.

📱 Overview

App Privacy Analyzer (Pepper) is an Android security application designed to help users understand the privacy and security risks posed by installed applications and APKs from unknown sources.

The app performs offline static analysis to evaluate:

Dangerous permission usage

Privacy-invasive behaviors

Potential malware indicators

APK integrity and tampering risks (planned)

Pepper works entirely on-device, without uploading any data to external servers, ensuring maximum user privacy.

🎯 Key Objectives

🔍 Identify apps that misuse sensitive permissions

🚨 Highlight high-risk applications clearly

📦 Analyze APKs from unknown sources before installation

🧠 Provide explainable risk scoring for better user awareness

🔐 Strengthen Android app supply-chain security (signature analysis)

✨ Features
🔹 Installed App Analysis

Scans all installed apps on the device

Categorizes apps as High / Medium / Low risk

Supports User apps and System apps

Displays app name, package name, icon, and risk score

🔹 Unknown APK Scanner

Allows users to scan APK files from unknown sources

Extracts permissions without installing the app

Helps detect risky or suspicious APKs before installation

🔹 Permission-Based Risk Scoring

Analyzes dangerous permissions such as:

Camera

Microphone

Location

Storage

SMS

Assigns risk scores based on permission severity

Flags suspicious permission combinations

🔹 Privacy Statistics Dashboard

Displays global statistics:

Total apps analyzed

High / Medium / Low risk app counts

Camera, Mic, and Location access counts

Helps users quickly understand overall device privacy exposure

🔹 App Detail View

Shows detailed permission breakdown for each app

Displays calculated risk level and score

One-click button to open Android App Permission Manager

🔹 Offline & Privacy-Friendly

❌ No internet required

❌ No cloud scanning

❌ No data collection

✅ Fully offline static analysis

🔐 Security Techniques Used

Static permission analysis

Risk-based heuristic scoring

Permission combination abuse detection

APK metadata inspection

Android PackageManager analysis

🚀 Planned Enhancements (Future Scope)

🔏 APK Signature & Tampering Detection

Detect self-signed and re-signed APKs

Identify repackaged or modified applications

🧠 Behavioral Deviation Detection

Flag apps behaving outside expected category norms

🧪 ML-based Malware Risk Classification

📄 Exportable Security Reports (PDF)

🕵️ Fake System App Detection

🛠️ Tech Stack

Language: Kotlin

Platform: Android

Minimum SDK: Android 11 (API 30)

Architecture: MVVM (modular & extensible)

UI: XML + Material Design

📸 Screenshots

(Add screenshots here)
Example:

App list with risk badges

Privacy statistics dashboard

App detail permission view

APK scan result screen

🎓 Academic Relevance

This project is suitable for:

MCA / B.Tech Final Year Project

Cybersecurity & Ethical Hacking domain

Android Security research

Privacy-aware mobile application development

Key cybersecurity concepts demonstrated:

Static malware analysis

Privacy risk assessment

Android permission abuse detection

Secure software design principles

📦 Installation

Clone the repository:

git clone https://github.com/abhishek0904m/AppPrivacyAnalyzer.git


Open the project in Android Studio

Sync Gradle

Run on a physical device or emulator (Android 11+)

👨‍💻 Developer

Abhishek M
MCA Student | Cybersecurity Enthusiast | Android Developer

GitHub: https://github.com/abhishek0904m

📄 License

This project is developed for academic and learning purposes.
You are free to fork and extend it with proper attribution.
