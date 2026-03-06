<p align="center">
  <img src="https://img.shields.io/badge/Android-Security-green?style=for-the-badge&logo=android"/>
  <img src="https://img.shields.io/badge/Kotlin-100%25-blue?style=for-the-badge&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Domain-Cybersecurity-red?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Version-2.0-purple?style=for-the-badge"/>
</p>

<h1 align="center">🛡️ App Privacy Analyzer (Pepper) v2.0</h1>

<p align="center">
An Android-based cybersecurity application for analyzing app privacy risks and detecting potentially malicious behavior using <b>offline static analysis</b> and <b>advanced security heuristics</b>.
</p>

---

## 🆕 What's New in v2.0

### 🎨 **Complete UI/UX Overhaul**
- **Modern Material Design 3** with consistent purple theme
- **Tab-based navigation** with clear visual indicators (📱 Installed Apps | 🔍 Scan APK)
- **Card-based layouts** with proper elevation and rounded corners
- **Enhanced loading states** with branded progress indicators
- **Popup-based scan results** to prevent UI conflicts during navigation

### 🔍 **Advanced App Analysis**
- **Detailed permission breakdown** showing granted vs requested permissions
- **Installation source detection** (Play Store, Amazon, Samsung, Sideloaded)
- **Digital signature verification** with SHA-256 hash display
- **Smart risk scoring** based on source trustworthiness
- **Permission descriptions** explaining what each permission allows

### 🔐 **Enhanced Security Features**
- **Signature verification improvements** with proper hash extraction
- **Malicious signer detection** using signature database
- **Source-based risk assessment** (Play Store = trusted, Sideloaded = higher risk)
- **Self-signed certificate handling** with context-aware messaging

### 📱 **Better App Selection**
- **Search functionality** to find apps quickly
- **User apps only** filter (no system app clutter)
- **Installation source badges** showing app origin
- **Improved app cards** with better visual hierarchy

---

## 📌 Overview

**App Privacy Analyzer (Pepper)** is an Android security application designed to help users understand the **privacy and security risks** posed by installed applications and APK files obtained from unknown sources.

Unlike cloud-based scanners, Pepper performs **entirely on-device analysis**, ensuring **maximum user privacy** and **offline usability**.

---

## 🎯 Key Objectives

- 🔍 Identify apps that misuse sensitive permissions  
- 🚨 Highlight high-risk applications clearly  
- 📦 Analyze APKs from unknown sources before installation  
- 🧠 Provide explainable, transparent risk scoring  
- 🔐 Improve Android app supply-chain security awareness  
- 🏪 Detect installation source and verify digital signatures

---

## ✨ Core Features

### 🔹 Enhanced Installed App Analysis
- Scans **user-installed apps only** (filters out system apps)
- **Search functionality** to quickly find specific apps
- **Installation source detection** (Google Play Store, Amazon Appstore, Samsung Galaxy Store, etc.)
- **Digital signature verification** with SHA-256 hash display
- Classifies apps into **High / Medium / Low risk** categories  
- **Detailed permission analysis** showing granted vs requested permissions
- Displays app icon, name, package name, and comprehensive risk assessment

---

### 🔹 Advanced APK Scanner
- Allows scanning APK files from unknown sources  
- Extracts permissions **without installing the app**  
- **Signature verification** for APK files
- **Malicious signer detection** using signature database
- Helps detect risky or suspicious APKs in advance  
- **Popup-based results** for better user experience

---

### 🔹 Intelligent Permission Analysis
- Analyzes dangerous permissions such as:
  - 📸 Camera (Take photos and record videos)
  - 🎙️ Microphone (Record audio and voice)
  - 📍 Location (Fine/Coarse location access)
  - 💾 Storage (Read/Write files and media)
  - ✉️ SMS (Read/Send text messages)
  - 📇 Contacts (Read/Write contact information)
  - 📞 Phone State (Access device ID and call info)
- **Permission descriptions** explaining what each permission allows
- **Granted vs Requested** status for each permission
- Flags suspicious permission combinations  
- Assigns severity-based risk scores with explanations

---

### 🔹 Enhanced Privacy Dashboard
- Displays aggregated insights with **color-coded cards**:
  - Total apps analyzed  
  - High / Medium / Low risk distribution with visual indicators
  - Camera, Microphone, and Location usage counts with emojis
- **Interactive filtering** by risk level and app type
- **Search functionality** across all installed apps
- Provides quick understanding of overall device privacy exposure  

---

### 🔹 Comprehensive App Details
- **Installation source verification** with trust indicators
- **Digital signature analysis** with hash display
- **Permission breakdown** with descriptions per app  
- Calculated risk level with detailed explanation  
- **Direct access** to Android App Permission Manager
- **Popup-based detailed results** for better readability

---

### 🔹 Privacy-First & Offline
- ❌ No internet required  
- ❌ No cloud uploads  
- ❌ No user data collection  
- ✅ Fully offline static analysis  
- ✅ All processing happens on-device

---

## 🔐 Security Techniques Used

- **Static permission analysis** with context-aware interpretation
- **Heuristic-based risk scoring** with source verification
- **Permission combination abuse detection**
- **APK metadata inspection** with signature verification
- **Digital signature validation** using SHA-256 hashing
- **Installation source verification** for trust assessment
- **Malicious signer database** for threat detection
- **Android PackageManager analysis** with enhanced error handling

---

## 🚀 Future Enhancements

- 🔏 **Enhanced APK Signature & Tampering Detection**
  - Advanced certificate chain validation
  - Detect repackaged or modified applications  

- 🧠 **Behavioral Deviation Detection**
  - Detect apps behaving outside expected category norms  
  - Machine learning-based anomaly detection

- 🧪 **ML-based Malware Risk Classification**  
- 📄 **Exportable Security Reports (PDF/JSON)**  
- 🕵️ **Advanced Fake System App Detection**  
- 🌐 **VirusTotal Integration** (optional online verification)
- 📊 **Privacy Score Trending** over time

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|---------|-----------|---------|
| Language | Kotlin | 1.9.24 |
| Platform | Android | API 26+ |
| Min SDK | Android 8.0 (API 26) | |
| Target SDK | Android 14 (API 34) | |
| Architecture | MVVM + Repository Pattern | |
| UI Framework | Material Design 3 | |
| Build System | Gradle | 8.5.0 |
| Coroutines | Kotlinx Coroutines | 1.7.3 |

---

## 📱 Screenshots & Features

### Main Dashboard
- **Tab-based navigation** with clear indicators
- **Color-coded risk statistics** with visual appeal
- **Search and filter functionality**
- **Modern card-based layout**

### App Analysis
- **Detailed permission breakdown** with descriptions
- **Installation source verification**
- **Digital signature display** with hash values
- **Risk scoring** with explanations

### APK Scanning
- **File picker integration** for APK selection
- **Comprehensive analysis** before installation
- **Popup-based results** for better UX
- **Signature verification** for security

---

## 🎓 Academic Relevance

This project is suitable for:

- 🎓 **MCA / B.Tech Final Year Project**  
- 🛡️ **Cybersecurity & Ethical Hacking domain**  
- 📱 **Android Security Research**  
- 🔐 **Privacy-focused mobile application development**  
- 🏫 **Academic research in mobile security**

### Key Cybersecurity Concepts Demonstrated:
- **Static malware analysis** with signature verification
- **Privacy risk assessment** with contextual analysis
- **Android permission abuse detection** with smart scoring
- **Digital signature validation** and certificate analysis
- **Secure software design principles** with offline-first approach
- **Supply chain security** through installation source verification

---

## 📦 Installation & Setup

### Prerequisites
- **Android Studio** Arctic Fox or later
- **Android SDK** with API level 26+
- **Kotlin** plugin enabled
- **Physical Android device** or emulator (API 26+)

### Installation Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/abhishek0904m/AppPrivacyAnalyzer.git
   cd AppPrivacyAnalyzer
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync dependencies:**
   - Allow Gradle sync to complete
   - Resolve any dependency issues if prompted

4. **Configure device:**
   - Connect a physical Android device (recommended)
   - Or start an Android emulator (API 26+)
   - Enable USB debugging if using physical device

5. **Build and run:**
   ```bash
   ./gradlew assembleDebug
   ```
   - Or click the Run ▶️ button in Android Studio

### Required Permissions
The app requires these permissions to function:
- `QUERY_ALL_PACKAGES` - To scan installed applications
- `READ_EXTERNAL_STORAGE` - To read APK files (Android 12 and below)

---

## 🔧 Development & Contribution

### Project Structure
```
app/src/main/
├── java/com/example/appprivacyanalyzer/
│   ├── data/          # Repository, signature utils, crypto
│   ├── model/         # Data classes (AppInfo, RiskLevel)
│   ├── scanner/       # App scanning logic
│   └── ui/            # Activities, fragments, adapters
├── res/
│   ├── layout/        # XML layouts with Material Design
│   ├── drawable/      # Icons, backgrounds, shapes
│   ├── values/        # Colors, strings, themes
│   └── menu/          # Menu resources
└── assets/            # Malicious signer database
```

### Key Components
- **AppRepository**: Handles app data loading and risk calculation
- **AppScanner**: Core scanning logic for installed apps and APKs
- **Signature Utils**: Digital signature verification and hash extraction
- **UI Components**: Modern Material Design interface with tabs

### Contributing Guidelines
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow Kotlin coding conventions
4. Add comprehensive comments for security-related code
5. Test on multiple Android versions
6. Commit changes (`git commit -m 'Add amazing feature'`)
7. Push to branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 🤝 Acknowledgments

- **Android Security Community** for best practices and guidelines
- **Material Design Team** for comprehensive design system
- **Kotlin Team** for excellent language features
- **Open Source Contributors** for inspiration and code quality standards

---

## 📞 Contact & Support

- **GitHub Issues**: [Report bugs or request features](https://github.com/abhishek0904m/AppPrivacyAnalyzer/issues)
- **Discussions**: [Join community discussions](https://github.com/abhishek0904m/AppPrivacyAnalyzer/discussions)
- **Security Issues**: Please report security vulnerabilities privately

---

<p align="center">
  <b>⭐ Star this repository if you find it helpful!</b><br>
  <sub>Built with ❤️ for Android Security Research</sub>
</p>
