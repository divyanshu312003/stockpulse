package com.stockpulse.stockpulse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist")
public class watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockSymbol;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private user user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public com.stockpulse.stockpulse.model.user getUser() {
        return user;
    }

    public void setUser(com.stockpulse.stockpulse.model.user user) {
        this.user = user;
    }
}