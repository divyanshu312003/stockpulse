from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
import yfinance as yf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
from sklearn.preprocessing import MinMaxScaler
import os

app = Flask(__name__)

# Global variables
scaler = MinMaxScaler()
model = None

# ------------------ Model Trainer ------------------
def train_model(data, sequence_length=50):
    global model

    scaled_data = scaler.fit_transform(data.reshape(-1, 1))

    x_train, y_train = [], []
    for i in range(sequence_length, len(scaled_data)):
        x_train.append(scaled_data[i-sequence_length:i])
        y_train.append(scaled_data[i])
    x_train, y_train = np.array(x_train), np.array(y_train)

    model = Sequential()
    model.add(LSTM(50, return_sequences=True, input_shape=(x_train.shape[1], 1)))
    model.add(LSTM(50))
    model.add(Dense(1))
    model.compile(optimizer='adam', loss='mean_squared_error')

    model.fit(x_train, y_train, epochs=5, batch_size=32, verbose=0)

# ------------------ Prediction ------------------
def predict_next_days(data, days=5, sequence_length=50):
    last_sequence = scaler.transform(data[-sequence_length:].reshape(-1, 1))
    predictions = []

    for _ in range(days):
        X_input = last_sequence[-sequence_length:]
        X_input = X_input.reshape(1, sequence_length, 1)
        next_value = model.predict(X_input, verbose=0)
        pred_value = scaler.inverse_transform(next_value)[0][0]
        predictions.append(float(pred_value))  # <- Converted here

        last_sequence = np.append(last_sequence, next_value)[-sequence_length:]

    return predictions

# ------------------ API Routes ------------------

@app.route('/train', methods=['POST'])
def train():
    content = request.get_json()
    if not content or 'ticker' not in content:
        return jsonify({'error': 'No ticker provided'}), 400

    ticker = content['ticker']
    try:
        df = yf.download(ticker, start="2015-01-01")
        if df.empty:
            return jsonify({'error': f'No data found for ticker {ticker}'}), 404

        close_prices = df['Close'].dropna().values
        if len(close_prices) < 60:
            return jsonify({'error': 'Not enough data to train model'}), 400

        train_model(close_prices)
        return jsonify({'message': f'Model trained successfully for {ticker}'}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/predict', methods=['POST'])
def predict():
    content = request.get_json()
    if not content or 'ticker' not in content:
        return jsonify({'error': 'No ticker provided'}), 400

    ticker = content['ticker']
    try:
        df = yf.download(ticker, start="2023-01-01")
        if df.empty:
            return jsonify({'error': f'No data found for ticker {ticker}'}), 404

        close_prices = df['Close'].dropna().values
        if model is None:
            return jsonify({'error': 'Model not trained yet'}), 400
        if len(close_prices) < 50:
            return jsonify({'error': 'Need at least 50 closing prices to predict'}), 400

        predictions = predict_next_days(close_prices)
        return jsonify({'predictions': predictions}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

# ------------------ Run App ------------------
if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5001))
    app.run(host='0.0.0.0', port=port)
