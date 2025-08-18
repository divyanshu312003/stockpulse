import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import warnings
warnings.filterwarnings('ignore')

import requests
from transformers import pipeline
from urllib.parse import quote  # NEW: to safely encode stock names

print("🔄 Loading FinBERT model...")
try:
    sentiment_pipeline = pipeline(
        "sentiment-analysis",
        model="yiyanghkust/finbert-tone",
        tokenizer="yiyanghkust/finbert-tone"
    )
    print("✅ FinBERT model loaded.")
except Exception as e:
    print(f"❌ Error loading FinBERT: {e}")
    sentiment_pipeline = None

GNEWS_API_KEY = "da14b81af9b41d245d2390264c7e2cb9"
GNEWS_API_URL = "https://gnews.io/api/v4/search"

def fetch_news(stock_name):
    try:
        print(f"📡 Fetching news for: {stock_name}")
        
        # Clean query: remove special chars, extra spaces
        clean_query = ''.join(e for e in stock_name if e.isalnum() or e.isspace()).strip()
        clean_query = clean_query.replace(' ', ' AND ')  # GNews supports Boolean queries
        
        params = {
            "q": clean_query,
            "lang": "en",
            "max": 5,
            "token": GNEWS_API_KEY
        }
        response = requests.get(GNEWS_API_URL, params=params)
        print(f"🌐 Status code from GNews: {response.status_code}")
        if response.status_code != 200:
            print(f"❌ GNews error: {response.text}")
            return []

        data = response.json()
        articles = data.get("articles", [])
        print(f"📰 Fetched {len(articles)} articles.")
        return [f"{a['title']}. {a.get('description', '')}" for a in articles]
    except Exception as e:
        print(f"❌ Error in fetch_news: {e}")
        return []

def get_sentiment(stock_name: str):
    if sentiment_pipeline is None:
        return "Model not loaded", []

    try:
        news_list = fetch_news(stock_name)
        if not news_list:
            return "No news found", []

        sentiments = []
        for article in news_list:
            print(f"📰 Analyzing: {article[:60]}...")
            result = sentiment_pipeline(article)[0]
            label = result['label'].lower()
            if 'positive' in label:
                sentiments.append('bullish')
            elif 'negative' in label:
                sentiments.append('bearish')
            else:
                sentiments.append('neutral')

        final_sentiment = (
            max(set(sentiments), key=sentiments.count)
            if sentiments else "neutral"
        )

        return final_sentiment, news_list

    except Exception as e:
        print(f"❌ Error in get_sentiment: {e}")
        return "error", []
