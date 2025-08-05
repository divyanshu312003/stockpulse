from flask import Flask, request, jsonify
import yfinance as yf
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import pickle

with open("model.pkl", "rb") as f:
    model = pickle.load(f)


app = Flask(__name__)

# Load your trained LSTM model



lookback = 30  # last 30 days as input

@app.route("/predict", methods=["GET"])
def predict_stock():
    stock_symbol = request.args.get("stock", "").upper()

    if not stock_symbol:
        return jsonify({"error": "Please provide a stock symbol, e.g., /predict?stock=AAPL"}), 400

    try:
        # 1. Fetch last 60 days of stock data
        df = yf.download(stock_symbol, period="60d", interval="1d")
        if df.empty:
            return jsonify({"error": f"No data found for {stock_symbol}"}), 404

        close_prices = df['Close'].values.reshape(-1, 1)

        # 2. Scale data
        scaler = MinMaxScaler(feature_range=(0,1))
        scaled_data = scaler.fit_transform(close_prices)

        # 3. Prepare last 30 days as input
        last_30_days = scaled_data[-lookback:]
        input_seq = np.expand_dims(last_30_days, axis=0)  # shape: (1,30,1)

        # 4. Predict next day price
        pred_scaled = model.predict(input_seq)[0][0]

        # 5. Inverse scale to get real price
        predicted_price = scaler.inverse_transform([[pred_scaled]])[0][0]

        return jsonify({
            "stock": stock_symbol,
            "Todays closing price": round(float(close_prices[-1][0]),2),
            "predicted_next_day_price": round(float(predicted_price), 2)
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
