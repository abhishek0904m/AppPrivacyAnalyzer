# Changelog

All notable changes to App Privacy Analyzer (Pepper) will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2026-03-06

### 🎨 Added - UI/UX Overhaul
- **Modern Material Design 3** interface with consistent purple theme
- **Tab-based navigation** with clear visual indicators (📱 Installed Apps | 🔍 Scan APK)
- **Card-based layouts** throughout the app with proper elevation and rounded corners
- **Enhanced loading states** with branded progress indicators and purple theme
- **Popup-based scan results** to prevent UI conflicts during tab navigation
- **Color-coded risk cards** (red/yellow/green backgrounds) for better visual hierarchy

### 🔍 Added - Advanced App Analysis
- **Detailed permission breakdown** showing granted vs requested permissions with descriptions
- **Installation source detection** (Play Store, Amazon, Samsung, Sideloaded) with trust indicators
- **Digital signature verification** with SHA-256 hash display for both installed apps and APK files
- **Smart risk scoring** based on source trustworthiness and signature analysis
- **Permission descriptions** explaining what each permission allows access to
- **Context-aware messaging** for different installation sources

### 🔐 Added - Enhanced Security Features
- **Signature verification improvements** with proper hash extraction using SHA-256
- **Malicious signer detection** using signature database from assets
- **Source-based risk assessment** (Play Store = trusted, Sideloaded = higher risk)
- **Self-signed certificate handling** with context-aware messaging for different sources
- **APK file signature analysis** before installation to detect potential threats

### 📱 Added - Better App Selection
- **Search functionality** to find apps quickly in installed apps list
- **User apps only** filter to remove system app clutter from selection
- **Installation source badges** showing app origin with visual indicators
- **Improved app cards** with better visual hierarchy and information display
- **Enhanced app selection UI** with Material Design cards and proper spacing

### 🛠️ Fixed - Bug Fixes
- **Duplicate permissions display** - Fixed issue where same permission appeared multiple times
- **White loading space** - Hidden RecyclerView during loading to prevent blank white areas
- **UI navigation conflicts** - Implemented popup dialogs for scan results to prevent tab switching issues
- **Signature extraction errors** - Fixed import paths and added proper error handling
- **Hash value display** - Corrected signature utils to show actual SHA-256 hash values
- **XML entity encoding** - Fixed & display issues in signature information

### 🔧 Changed - Technical Improvements
- **Set-based permission tracking** to prevent duplicates in permission analysis
- **Enhanced error handling** throughout signature verification process
- **Improved coroutine usage** for better async operations and UI responsiveness
- **Better context management** for signature verification across different sources
- **Optimized app scanning logic** with proper filtering and source detection

### 📚 Updated - Documentation
- **Comprehensive README.md** update with v2.0 features and screenshots
- **Enhanced project structure** documentation with clear component descriptions
- **Updated installation instructions** with proper prerequisites and setup steps
- **Added contribution guidelines** with coding standards and security considerations
- **Version bump** to 2.0 in build.gradle to match feature set

---

## [1.0.0] - Initial Release

### Added
- Basic app privacy analysis for installed applications
- APK file scanning capability
- Permission risk assessment
- Simple UI with basic app listing
- Digital signature verification (basic implementation)
- Risk scoring system
- Material Design components

### Features
- Scan installed applications for privacy risks
- Analyze APK files before installation
- Basic permission analysis
- Simple risk categorization (High/Medium/Low)
- Basic digital signature checking