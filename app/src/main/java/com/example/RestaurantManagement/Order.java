package com.example.RestaurantManagement;

public class Order extends IdentifiableObject
{
    public String tableID;
    public int numberOfPeoples, totalAmount;

    @Override public Obj GetInstance()
    {
        return this;
    }

    public Order(String ID, String tableID, int numberOfPeoples, int totalAmount)
    {
        super(ID);
        this.tableID = tableID;
        this.numberOfPeoples = numberOfPeoples;
        this.totalAmount = totalAmount;
    }

    public Order()
    {

    }
}