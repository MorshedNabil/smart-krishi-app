# 🌾 Smart Krishi App

## 📌 Overview
**Smart Krishi App** is a digital marketplace designed for **farmers and vendors in Bangladesh** to buy and sell agricultural products at **fair market prices**.  
The app integrates a **Machine Learning–based crop price prediction model** to help farmers make informed selling decisions based on **market trends and seasonal demand**.

Our goal is to reduce price exploitation and ensure farmers receive the value they truly deserve for their crops.

---

## 🎯 Target Users
- 👨‍🌾 Farmers  
- 🏪 Vendors  

---

## ✨ Key Features
- 📈 **Crop Price Prediction** using Machine Learning  
- 🤝 Fair price transparency for farmers  
- 📱 Mobile-friendly application  
- ⚡ Fast and scalable backend with API-based architecture  
- 🗄 Secure database for product and price data  

---

## 🛠 Tech Stack
- **Mobile App Development:** Kotlin, Jetpack Compose, Retrofit, Room
- **Backend API:** Django Rest Framework (Python)  
- **Machine Learning:** Python  
- **Database:** PostgreSQL  

---

## 🧠 Machine Learning Model
- **Purpose:** Predict crop prices for farmers  
- **Prediction Factors:**
  - Historical market prices  
  - Seasonal demand  
  - Market trends  
- **Benefit:** Helps farmers decide *when* and *at what price* to sell their crops

---

## 🏗 System Architecture
```mermaid
flowchart TB

    subgraph Android["Android Application"]
        UI["Jetpack Compose UI"]
        VM["ViewModel"]
        Repo["Repository"]
        Retrofit["Retrofit API Client"]
        Room[("Room Database")]

        UI --> VM
        VM --> Repo
        Repo --> Room
        Repo --> Retrofit
    end

    subgraph Backend["Backend Infrastructure"]
        Django["Django REST Framework"]
        DB[("PostgreSQL Database")]

        Django <--> DB
    end

    Firebase["Firebase Phone Authentication"]
    Cloudinary["Cloudinary Image Storage"]

    Firebase -->|"Firebase ID Token"| UI

    Retrofit <-->|"HTTPS / REST API"| Django

    Retrofit -.->|"Authenticated requests"| Django

    UI <-->|"Direct Image Upload"| Cloudinary
```
## 🚀 Future Improvements

- Real-time market price updates
- Weather-based price prediction
- User authentication & role management
- Admin dashboard for monitoring trends

## 🙌 Acknowledgements

- Farmers and vendors of Bangladesh
- Open-source libraries and datasets used for ML development
