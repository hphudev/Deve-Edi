package com.example.RestaurantManagement;

public class Report {
    private String id;
    private String total;

    public Report(String id, String total) {
        this.id = id;
        this.total = total;
    }

    public Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
