<p align="center">
  <img src="https://img.shields.io/badge/Android-Security-green?style=for-the-badge&logo=android"/>
  <img src="https://img.shields.io/badge/Kotlin-100%25-blue?style=for-the-badge&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Domain-Cybersecurity-red?style=for-the-badge"/>
</p>

<h1 align="center">🛡️ App Privacy Analyzer (Pepper)</h1>

<p align="center">
An Android-based cybersecurity application for analyzing app privacy risks and detecting potentially malicious behavior using <b>offline static analysis</b> and <b>security heuristics</b>.
</p>

---

## 📌 Overview

**App Privacy Analyzer (Pepper)** is an Android security application designed to help users understand the **privacy and security risks** posed by installed applications and APKs obtained from unknown sources.

Unlike cloud-based scanners, Pepper performs **entirely on-device analysis**, ensuring **maximum user privacy** and **offline usability**.

---

## 🎯 Key Objectives

- 🔍 Identify apps that misuse sensitive permissions  
- 🚨 Highlight high-risk applications clearly  
- 📦 Analyze APKs from unknown sources before installation  
- 🧠 Provide explainable, transparent risk scoring  
- 🔐 Improve Android app supply-chain security awareness  

---

## ✨ Core Features

### 🔹 Installed App Analysis
- Scans all installed applications on the device  
- Classifies apps into **High / Medium / Low risk** categories  
- Supports **User apps** and **System apps**  
- Displays app icon, name, package name, and risk score  

---

### 🔹 Unknown APK Scanner
- Allows scanning APK files from unknown sources  
- Extracts permissions **without installing the app**  
- Helps detect risky or suspicious APKs in advance  

---

### 🔹 Permission-Based Risk Scoring
- Analyzes dangerous permissions such as:
  - 📸 Camera  
  - 🎙️ Microphone  
  - 📍 Location  
  - 💾 Storage  
  - ✉️ SMS  
- Flags suspicious permission combinations  
- Assigns severity-based risk scores  

---

### 🔹 Privacy Statistics Dashboard
- Displays aggregated insights:
  - Total apps analyzed  
  - High / Medium / Low risk distribution  
  - Camera, Microphone, and Location usage counts  
- Provides quick understanding of overall device privacy exposure  

---

### 🔹 Detailed App View
- Permission breakdown per app  
- Calculated risk level with explanation  
- One-tap access to **Android App Permission Manager**  

---

### 🔹 Privacy-First & Offline
- ❌ No internet required  
- ❌ No cloud uploads  
- ❌ No user data collection  
- ✅ Fully offline static analysis  

---

## 🔐 Security Techniques Used

- Static permission analysis  
- Heuristic-based risk scoring  
- Permission combination abuse detection  
- APK metadata inspection  
- Android PackageManager analysis  

---

## 🚀 Future Enhancements

- 🔏 **APK Signature & Tampering Detection**
  - Detect self-signed and re-signed APKs  
  - Identify repackaged or modified applications  

- 🧠 **Behavioral Deviation Detection**
  - Detect apps behaving outside expected category norms  

- 🧪 **ML-based Malware Risk Classification**  
- 📄 **Exportable Security Reports (PDF)**  
- 🕵️ **Fake System App Detection**  

---

## 🛠️ Tech Stack

| Component | Technology |
|---------|-----------|
| Language | Kotlin |
| Platform | Android |
| Min SDK | Android 11 (API 30) |
| Architecture | Modular / MVVM |
| UI | XML + Material Design |

---

## 🎓 Academic Relevance

This project is suitable for:

- 🎓 MCA / B.Tech Final Year Project  
- 🛡️ Cybersecurity & Ethical Hacking domain  
- 📱 Android Security Research  
- 🔐 Privacy-focused mobile application development  

### Key Cybersecurity Concepts Demonstrated:
- Static malware analysis  
- Privacy risk assessment  
- Android permission abuse detection  
- Secure software design principles  

---
## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/abhishek0904m/AppPrivacyAnalyzer.git

2. Open the project in Android Studio

3. Allow Gradle sync to complete

4. Connect a physical Android device or start an emulator
(Android 11 / API 30 or higher recommended)

5. Click Run ▶ to build and launch the app
