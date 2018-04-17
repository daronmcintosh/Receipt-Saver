package com.example.daron.receiptsaver;

import java.util.Date;

public class Receipt {
    private int _id;
    private String name, category, description;
    private Date date;
    private double total;

    public int get_id() {
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

    public Date getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }
}
