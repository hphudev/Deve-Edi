package com.example.RestaurantManagement;

public class Bill extends IdentifiableObject
{
    public String userID, date;
    public int value, totalAmount;

    @Override public Obj GetInstance()
    {
        return this;
    }

    public Bill(String ID, String userID, String date, int value, int totalAmount)
    {
        super(ID);
        this.userID = userID;
        this.date = date;
        this.value = value;
        this.totalAmount = totalAmount;
    }

    public Bill()
    {

    }
}
