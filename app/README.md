# Stock Screener Android App

A streamlined Kotlin Android application that displays stock data using MVVM architecture.

## ✅ Features
- View a list of popular stocks (from `stocks.json`)
- Mark/unmark favourites
- Filter stocks using a search bar
- See visual feedback based on stock price changes
- Refresh data on demand

---

## 🗂 App Structure
This app follows the MVVM (Model-View-ViewModel) architecture pattern to ensure clean separation of concerns and better testability.

🧱 MVVM Components:
Model:
Contains the data classes and the repository responsible for providing stock data from stocks.json.

ViewModel:
Handles the business logic and exposes observable LiveData to the UI. It processes search queries and tracks favourite selections.

View (UI):
Activities and Fragments display the data and observe changes from the ViewModel using LiveData.

---

## 🔧 Setup Instructions
1. Clone the repository
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

---

## ⚖️ Future Improvements

- Currently uses static JSON; in future, integrate with a live stock API.
- Add detailed stock views with charts and metrics.
- Implement local storage to persist favorites and offline data.