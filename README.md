4. **Employees Screen** ➔ Searchable directory + Add / Edit dialogs.
5. **Attendance Screen** ➔ Live timecard, history table, and punch-in status.
6. **Leaves Screen** ➔ Filter by status, create leave request, manager actions.
7. **Tasks Screen** ➔ Filter by priority, assignee, status change sheet.
8. **Payroll Screen** ➔ Salary breakdown, deductions, tax, and payslips.
9. **Settings Screen** ➔ Switch between Dark Mode / Light Mode and notification toggles.
---
## 🚀 Getting Started & Installation
### Prerequisites
- **Android Studio Ladybug (2024.2+)** or newer.
- **JDK 17** installed and configured in Android Studio.
- Android device or emulator running **API 26 (Android 8.0)** or higher.
### Steps to Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/satishjadav01/Employee_Management_sys_Android.git
   ```
2. **Open in Android Studio**:
   - Open Android Studio, click **Open**, and navigate to the project directory.
3. **Gradle Sync**:
   - Allow Android Studio to sync the dependencies via Gradle (`gradle/libs.versions.toml`).
4. **Run the App**:
   - Select your connected device or emulator.
   - Click the green **Run (Shift + F10)** button.
---
## 💾 Data Storage & Preferences
- **Session & Theming**: Stored in `UserPreferences` using AndroidX Jetpack DataStore Preferences (`user_preferences.preferences_pb`).
- **Entity State**: Maintained via `EmsRepository` with reactive `StateFlow` streams, easily swappable with Room Database DAOs or REST endpoints.
---
## 🗺️ Future Roadmap
- [ ] Biometric Authentication (Fingerprint / Face Unlock).
- [ ] Push Notifications via Firebase Cloud Messaging (FCM).
- [ ] Geo-fencing & GPS-verified clock in/out for field employees.
- [ ] PDF payslip export & download.
- [ ] Offline synchronization with Room DB and WorkManager.
---
## 📄 License
This project is licensed under the [MIT License](LICENSE) - feel free to use and adapt it for learning or commercial applications.
