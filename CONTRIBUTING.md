# Contributing to App Privacy Analyzer (Pepper)

Thank you for your interest in contributing to App Privacy Analyzer! This document provides guidelines for contributing to the project.

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK with API level 26+
- Kotlin plugin enabled
- Git for version control

### Development Setup
1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/AppPrivacyAnalyzer.git`
3. Open the project in Android Studio
4. Allow Gradle sync to complete
5. Connect an Android device or start an emulator (API 26+)

## 📝 Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comprehensive comments for security-related code
- Maintain consistent indentation (4 spaces)
- Use Material Design 3 components for UI

### Security Considerations
- All analysis must be performed offline (no network requests)
- Validate all user inputs and file operations
- Use proper error handling for signature verification
- Follow Android security best practices
- Test on multiple Android versions (API 26+)

### UI/UX Guidelines
- Maintain the purple theme consistency
- Use Material Design 3 components
- Ensure accessibility compliance
- Test on different screen sizes
- Follow the established card-based layout pattern

## 🔧 Making Changes

### Branch Naming
- Feature branches: `feature/description-of-feature`
- Bug fixes: `bugfix/description-of-bug`
- Documentation: `docs/description-of-change`

### Commit Messages
- Use clear, descriptive commit messages
- Start with a verb (Add, Fix, Update, Remove)
- Reference issues when applicable: `Fix #123: Description`

### Testing
- Test on physical devices when possible
- Verify functionality on different Android versions
- Test with various APK files and installed apps
- Ensure no crashes or memory leaks

## 📋 Pull Request Process

1. **Create a feature branch** from `main`
2. **Make your changes** following the guidelines above
3. **Test thoroughly** on multiple devices/versions
4. **Update documentation** if needed
5. **Submit a pull request** with:
   - Clear description of changes
   - Screenshots for UI changes
   - Testing details
   - Reference to related issues

### Pull Request Checklist
- [ ] Code follows project style guidelines
- [ ] Changes have been tested on Android devices
- [ ] Documentation has been updated if necessary
- [ ] No new security vulnerabilities introduced
- [ ] UI changes maintain Material Design consistency
- [ ] All security analysis remains offline

## 🐛 Reporting Issues

### Bug Reports
Include the following information:
- Android version and device model
- App version
- Steps to reproduce the issue
- Expected vs actual behavior
- Screenshots or logs if applicable

### Feature Requests
- Describe the feature and its benefits
- Explain how it fits with the app's privacy-first approach
- Consider security implications
- Provide mockups or examples if applicable

## 🔐 Security Issues

**Do not report security vulnerabilities in public issues.**

For security-related issues:
1. Email the maintainers privately
2. Provide detailed information about the vulnerability
3. Allow time for the issue to be addressed before public disclosure

## 📚 Areas for Contribution

### High Priority
- Enhanced malware detection algorithms
- Additional signature verification methods
- Performance optimizations
- Accessibility improvements
- Multi-language support

### Medium Priority
- Export functionality (PDF/JSON reports)
- Advanced permission analysis
- UI/UX enhancements
- Additional installation source detection

### Documentation
- Code documentation improvements
- User guides and tutorials
- API documentation
- Security analysis explanations

## 🎯 Code Review Process

All contributions go through code review:
1. Automated checks (build, basic tests)
2. Manual review by maintainers
3. Security review for sensitive changes
4. UI/UX review for interface changes
5. Final approval and merge

## 📞 Getting Help

- **GitHub Discussions**: For general questions and ideas
- **GitHub Issues**: For bug reports and feature requests
- **Code Comments**: For implementation-specific questions

## 🏆 Recognition

Contributors will be recognized in:
- README.md acknowledgments section
- Release notes for significant contributions
- GitHub contributor statistics

Thank you for helping make App Privacy Analyzer better and more secure! 🛡️