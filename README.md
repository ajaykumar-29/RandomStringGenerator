# 📱 Random String Generator

A modern Android application built with **Jetpack Compose** and **Kotlin**, following the **MVVM architecture**. It fetches random strings from a custom `ContentProvider`, displays them in a reactive UI, and stores them locally using **Room Database**. The app also supports input validation and data management operations like insert, delete, and clear all.

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI Toolkit:** Jetpack Compose
- **Local Storage:** Room Database
- **State Management:** StateFlow, Flow
- **Dependency Injection:** Hilt
- **Testing:**
  - `JUnit 4` – Unit testing framework  
  - `MockK` – For mocking dependencies  
  - `kotlinx-coroutines-test` – Coroutine test utilities  
  - `Turbine` – For testing Kotlin Flows  

---

## 🎯 Features

- 🔢 **Fetch Random Strings**  
  Retrieves random strings from a custom `ContentProvider` using a `ContentResolver`.

- 💾 **Store Locally with Room**  
  Saves fetched strings into a Room database with support for insert, delete, and clear operations.

- 🧠 **Real-Time Updates**  
  Uses `Flow` and `StateFlow` to keep the UI in sync with the database in real time.

- 📝 **Input Validation**  
  Validates user input for string length and format with helpful error messages.

- 🗑️ **Data Management**  
  - Delete a specific string  
  - Clear all stored strings

- 🌙 **Dark/Light Theme Toggle**  
  A toggle button allows switching between dark and light themes.

- 📋 **Copy to Clipboard**  
  Easily copy any generated random string to the clipboard for use elsewhere.

---

## 📸 Screenshots

![image](https://github.com/user-attachments/assets/d60bb79d-f27a-4eb7-9be4-21dec0255189)

