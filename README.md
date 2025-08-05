📈 StockPulse – Real-Time Stock Advisory System
StockPulse is a full-stack real-time stock advisory platform that combines:

Spring Boot Backend – Handles API aggregation, user alerts, and database management

Python ML Microservice (Flask) – Predicts stock prices and performs sentiment analysis

PostgreSQL Database – Stores user alerts and historical predictions

Docker Support – Containerized deployment of backend and ML service

🚀 Features
Stock Price Prediction

LSTM + GRU hybrid model trained on last 30 days of closing prices

Predicts next-day stock price

Supports US (AAPL, MSFT, GOOGL, etc.) and Indian stocks (RELIANCE.NS, COCHINSHIP.NS, etc.)

Stock Sentiment Analysis

Uses NewsAPI / yFinance headlines to fetch latest market news for a stock

Analyzes sentiment using FinBERT → Bullish, Bearish, Neutral

Returns a sentiment score to support trading decisions

Sample Response:

json
Copy
Edit
{
  "stock": "AAPL",
  "sentiment": "Bullish",
  "confidence": 0.82
}
Real-Time Stock Data

Fetches current price and historical data via yFinance API

Returns JSON response with:

json
Copy
Edit
{
  "stock": "AAPL",
  "todays_closing_price": 203.33,
  "predicted_next_day_price": 204.85
}
Alert System (Optional Extension)

Store alerts in PostgreSQL

Can trigger SMS notifications using Twilio or Fast2SMS

Modular Microservices

Spring Boot API: http://localhost:8080/api/stocks/predict?stock=AAPL

Flask ML Service:

http://localhost:5000/predict?stock=AAPL (Price Prediction)

http://localhost:5000/sentiment?stock=AAPL (News Sentiment)

Dockerized Deployment

Backend + ML service + PostgreSQL can run in separate containers

🏗 Project Structure
StockPulse/
├── backend-springboot/
│ ├── src/main/java/com/stockpulse
│ │ ├── controller/StockController.java
│ │ ├── dto/StockPredictionResponse.java
│ │ └── service/...
│ ├── pom.xml
│ └── ...
│
├── ml-service/
│ ├── price-predictor/
│ │ ├── app.py # Flask API for predictions
│ │ ├── model.pkl # Pickled LSTM model
│ │ ├── stock_model.keras # Original model format
│ │ ├── requirements.txt
│ │ └── Dockerfile
│ ├── sentiment-analysis/
│ │ ├── sentiment_news_api.py # News + FinBERT sentiment
│ │ └── requirements.txt
│
├── database/
│ └── init.sql # PostgreSQL initialization (optional)
│
├── docker-compose.yml # Multi-container setup
├── README.md
└── .gitignore

⚙️ Tech Stack
Backend: Spring Boot (Java 17), RestTemplate, Jackson

ML Service: Python 3.10, Flask, TensorFlow/Keras, Scikit-learn, yFinance, Transformers (FinBERT)

Database: PostgreSQL 14+

Deployment: Docker + Docker Compose

🔧 Setup Instructions
1️⃣ Clone the Repository

bash
Copy
Edit
git clone https://github.com/YourUsername/StockPulse.git
cd StockPulse
2️⃣ Setup Python ML Microservice

bash
Copy
Edit
cd ml-service/price-predictor
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
python app.py
Runs at: http://127.0.0.1:5000/predict?stock=AAPL

3️⃣ Setup Sentiment Analysis Microservice

bash
Copy
Edit
cd ml-service/sentiment-analysis
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python sentiment_news_api.py
Runs at: http://127.0.0.1:5000/sentiment?stock=AAPL

4️⃣ Setup Spring Boot Backend

bash
Copy
Edit
cd backend-springboot
./mvnw spring-boot:run
Runs at: http://127.0.0.1:8080/api/stocks/predict?stock=AAPL

5️⃣ Docker Deployment (Optional)

bash
Copy
Edit
docker-compose up --build
🧪 Example API Usage
Price Prediction

bash
Copy
Edit
GET http://localhost:5000/predict?stock=AAPL
Sentiment Analysis

bash
Copy
Edit
GET http://localhost:5000/sentiment?stock=AAPL
Combined Spring Boot Endpoint

bash
Copy
Edit
GET http://localhost:8080/api/stocks/predict?stock=AAPL
Sample Response:

json
Copy
Edit
{
  "stock": "AAPL",
  "todays_closing_price": 203.33,
  "predicted_next_day_price": 204.85,
  "sentiment": "Bullish",
  "confidence": 0.82
}
📊 Future Enhancements
Multi-stock prediction in a single API call

Store historical predictions in DB

Live sentiment score dashboard with React

SMS/Email alert system with thresholds

👨‍💻 Author
Divyanshu Srivastava
B.Tech ECE @ MANIT | ML & Data Science Enthusiast
