# app.py
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 
import warnings
warnings.filterwarnings('ignore')

from flask import Flask, request, jsonify
from news_sentiment import get_sentiment

app = Flask(__name__)

@app.route('/sentiment', methods=['GET'])
def analyze_sentiment():
    stock = request.args.get('stock', '').strip()
    if not stock:
        return jsonify({"error": "Stock parameter is required"}), 400

    sentiment, news = get_sentiment(stock)

    return jsonify({
        "stock": stock,
        "market_sentiment": sentiment,
        "top_news": news
    })

if __name__ == '__main__':
    app.run(debug=True)
