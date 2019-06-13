package com.example.cinema.po;

public class RefundStrategy {
    /**
     * id
     */
    private int id;

    /**
     * 退票策略
     */
    private int strategy;

    public RefundStrategy() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }
}
