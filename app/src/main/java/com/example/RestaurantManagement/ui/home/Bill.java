package com.example.RestaurantManagement.ui.home;

import java.util.List;

public class Bill {
    private int numCustomer;
    private int numTable;
    private long total;
    private List<ItemChoice> itemChoiceList;

    public Bill(int numCustomer, int numTable, long total, List<ItemChoice> itemChoiceList) {
        this.numCustomer = numCustomer;
        this.numTable = numTable;
        this.total = total;
        this.itemChoiceList = itemChoiceList;
    }

    public int getNumCustomer() {
        return numCustomer;
    }

    public void setNumCustomer(int numCustomer) {
        this.numCustomer = numCustomer;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<ItemChoice> getItemChoiceList() {
        return itemChoiceList;
    }

    public void setItemChoiceList(List<ItemChoice> itemChoiceList) {
        this.itemChoiceList = itemChoiceList;
    }
}
