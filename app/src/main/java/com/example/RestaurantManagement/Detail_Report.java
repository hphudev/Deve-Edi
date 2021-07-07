package com.example.RestaurantManagement;

public class Detail_Report {
    private String id;
    private String id_rep;
    private String number_of;
    private int price;

    public Detail_Report(String id, String id_rep, String number_of, int price) {
        this.id = id;
        this.id_rep = id_rep;
        this.number_of = number_of;
        this.price = price;
    }

    public Detail_Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_rep() {
        return id_rep;
    }

    public void setId_rep(String id_rep) {
        this.id_rep = id_rep;
    }

    public String getNumber_of() {
        return number_of;
    }

    public void setNumber_of(String number_of) {
        this.number_of = number_of;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
