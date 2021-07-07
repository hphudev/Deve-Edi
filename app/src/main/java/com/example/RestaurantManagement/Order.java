package com.example.RestaurantManagement;

public class Order {
    private String id;
    private String id_item;
    private int number_of;

    public Order(String id, String id_item, int number_of) {
        this.id = id;
        this.id_item = id_item;
        this.number_of = number_of;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public int getNumber_of() {
        return number_of;
    }

    public void setNumber_of(int number_of) {
        this.number_of = number_of;
    }
}
