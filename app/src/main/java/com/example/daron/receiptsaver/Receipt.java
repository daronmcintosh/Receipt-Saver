package com.example.daron.receiptsaver;

import java.util.Date;

public class Receipt {
    private int _id;
    private String name, category, description;
    private String date;
    private double total;

    public Receipt(int id, String name, String category, String date, double total, String description) {
        this._id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.total = total;
        this.description = description;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }


}
