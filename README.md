# JK MarketBridge

> **Local trade. Trusted connections.**

An offline-first Android prototype that connects buyers and traders across Zambia's local markets. Built with Jetpack Compose and Material 3.

---

## Screenshots

| Marketplace | Buyer Requests | Add Product | Trader Profile |
|:-----------:|:--------------:|:-----------:|:--------------:|
| ![Marketplace](docs/screen_marketplace.png) | ![Requests](docs/screen_requests.png) | ![Add Product](docs/screen_add.png) | ![Profile](docs/screen_profile.png) |

---

## Features

### Marketplace
- Scrollable product cards with name, Kwacha price, trader, market, stall and stock quantity
- Live search across product names, traders and markets
- Category filter chips (Vegetables, Nuts, Textiles, Poultry, Grains…)
- Tap any card to open a full product detail dialog
- Reserve a product for collection — generates a unique 4-digit collection code

### Buyer Requests
- Pre-loaded sample requests from buyers across Lusaka
- Add new requests with product, quantity, delivery location and maximum budget
- Submissions appear instantly in the list without any page reload

### Add Product
- Seven-field form: name, category, price in Kwacha, quantity, trader, market and stall
- Required-field validation with inline error messages
- New products are immediately visible in the Marketplace tab

### Trader Profile
- Verified trader card (Alice Banda — Soweto Market, Stall B14)
- Live product count that updates as products are added
- "Why Verification Matters" section explaining trust and accountability

---

## Tech Stack

| Layer | Choice |
|---|---|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| State | `mutableStateListOf` / `mutableStateOf` (no ViewModel) |
| Navigation | Tab state (`when` on `selectedTab`) |
| Data | In-memory — no database, no network |
| Build | Android Gradle Plugin 9.3.0 |
| Compose BOM | 2026.02.01 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

No Firebase, Room, Retrofit, or any external service is used. The app works completely offline.

---

## Project Structure

```
app/src/main/java/zm/edu/justinkabwe/jkmarketbridge/
├── MainActivity.kt          # All screens, composables and data models
└── ui/theme/
    ├── Color.kt             # Agricultural green palette
    ├── Theme.kt             # Material 3 light colour scheme
    └── Type.kt              # Typography
```

---

## Getting Started

### Prerequisites
- Android Studio Ladybug or later
- JDK 11
- Android SDK 36

### Build & Run

```bash
# Clone the repository
git clone https://github.com/BRICIOU/JKMarketBridge.git
cd JKMarketBridge

# Build debug APK
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

The APK is output to `app/build/outputs/apk/debug/app-debug.apk`.

---

## Sample Data

Five products are pre-loaded at startup:

| Product | Price | Market | Stall | Stock |
|---|---|---|---|---|
| Fresh Tomatoes | K25/kg | Soweto Market | B14 | 40 kg |
| Groundnuts | K40/kg | Kamwala Market | C08 | 25 kg |
| Chitenge Fabric | K120 | Chilenje Market | A04 | 16 items |
| Fresh Eggs | K65/tray | Matero Market | D11 | 20 trays |
| Maize | K180/50kg bag | Soweto Market | F03 | 30 bags |

Three buyer requests are pre-loaded (Sweet Potatoes, Dried Fish/Kapenta, Mixed Vegetables).

---

## Package

```
zm.edu.justinkabwe.jkmarketbridge
```

---

## License

This project is a prototype built for educational purposes at the University of Zambia (Justin Kabwe).