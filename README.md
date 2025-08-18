# 📈 StockPulse – Real-Time Stock Advisory System

StockPulse is a **full-stack, real-time stock advisory platform** designed to empower traders and investors with actionable price predictions and sentiment analysis. It integrates a robust Spring Boot backend, Python-based ML microservices, and a PostgreSQL database—all containerized for easy deployment and scalability.

---

## 🚀 Features

1. **Stock Price Prediction**
   - Advanced LSTM + GRU hybrid model trained on the last 30 days of closing prices
   - Predicts **next-day stock price** for both US (AAPL, MSFT, GOOGL, etc.) and Indian stocks (RELIANCE.NS, COCHINSHIP.NS, etc.)

2. **Stock Sentiment Analysis**
   - Fetches latest market news via NewsAPI or yFinance headlines
   - Analyzes sentiment using **FinBERT (Transformers)**—returns *Bullish*, *Bearish*, or *Neutral* with confidence score
   - **Sample Response:**
     ```json
     {
       "stock": "AAPL",
       "sentiment": "Bullish",
       "confidence": 0.82
     }
     ```

3. **Real-Time Stock Data**
   - Fetches current and historical prices using **yFinance API**
   - Returns JSON with today’s closing price and predicted next-day price
     ```json
     {
       "stock": "AAPL",
       "todays_closing_price": 203.33,
       "predicted_next_day_price": 204.85
     }
     ```

4. **Alert System (Optional Extension)**
   - Store alerts in PostgreSQL
   - Can trigger SMS notifications using Twilio or Fast2SMS

5. **Modular Microservices**
   - **Spring Boot API:** `/api/stocks/predict?stock=AAPL`
   - **Flask ML Service:** `/predict?stock=AAPL` (Price Prediction), `/sentiment?stock=AAPL` (Sentiment Analysis)

6. **Dockerized Deployment**
   - Backend, ML service, and PostgreSQL run in isolated containers for easy setup

---

## 🏗 Project Structure

```
StockPulse/
├── backend-springboot/
│   ├── src/main/java/com/stockpulse/
│   │   ├── controller/StockController.java
│   │   ├── controller/AuthController.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JwtUserUtil.java
│   │   │   └── jwtutil.java
│   │   ├── repository/
│   │   │   ├── userrepository.java
│   │   │   └── watchlistrepository.java
│   │   └── service/...
│   ├── pom.xml
│   └── ...
│
├── ml-service/
│   ├── price-predictor/
│   │   ├── app.py                # Flask API for price predictions
│   │   ├── model.pkl             # Pickled LSTM model
│   │   ├── stock_model.keras     # Original Keras model
│   │   ├── requirements.txt
│   │   └── Dockerfile
│   ├── sentiment-analysis/
│   │   ├── sentiment_news_api.py # News + FinBERT sentiment analysis
│   │   └── requirements.txt
│
├── database/
│   └── init.sql                  # PostgreSQL initialization (optional)
│
├── docker-compose.yml            # Multi-container setup
├── README.md
└── .gitignore
```

---

## ⚙️ Tech Stack

- **Backend:** Spring Boot (Java 17), RestTemplate, Jackson, Spring Security (JWT)
- **ML Service:** Python 3.10, Flask, TensorFlow/Keras, Scikit-learn, yFinance, Transformers (FinBERT)
- **Database:** PostgreSQL 14+
- **Deployment:** Docker, Docker Compose

---

## 🔧 Setup Instructions

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/divyanshu312003/stockpulse.git
cd stockpulse
```

### 2️⃣ Setup Python ML Microservice
```bash
cd ml-service/price-predictor
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
python app.py
```
Runs at: **http://127.0.0.1:5000/predict?stock=AAPL**

### 3️⃣ Setup Sentiment Analysis Microservice
```bash
cd ml-service/sentiment-analysis
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python sentiment_news_api.py
```
Runs at: **http://127.0.0.1:5000/sentiment?stock=AAPL**

### 4️⃣ Setup Spring Boot Backend
```bash
cd backend-springboot
./mvnw spring-boot:run
```
Runs at: **http://127.0.0.1:8080/api/stocks/predict?stock=AAPL**

### 5️⃣ Docker Deployment (Optional)
```bash
docker-compose up --build
```

---

## 🧪 Example API Usage

**Price Prediction**
```
GET http://localhost:5000/predict?stock=AAPL
```

**Sentiment Analysis**
```
GET http://localhost:5000/sentiment?stock=AAPL
```

**Combined Spring Boot Endpoint**
```
GET http://localhost:8080/api/stocks/predict?stock=AAPL
```

**Sample Combined Response:**
```json
{
  "stock": "AAPL",
  "todays_closing_price": 203.33,
  "predicted_next_day_price": 204.85,
  "sentiment": "Bullish",
  "confidence": 0.82
}
```

---

## 📊 Future Enhancements

- Multi-stock prediction in a single API call
- Store historical predictions in DB
- Live sentiment score dashboard (React)
- SMS/Email alert system with thresholds

---

## 👨‍💻 Author

**Divyanshu Srivastava**  
B.Tech ECE @ MANIT | ML & Data Science Enthusiast

---

## 📝 License

This project is **open-source** under the MIT License.
