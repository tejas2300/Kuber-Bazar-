package com.example.sampleprice;

public class Items {
    private	int	id;
    private	String name;
    private	String price;
    private	String date;
    private	String time;

    public Items(String name, String price,String date,String time) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public Items(int id, String name, String price,String date,String time) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}