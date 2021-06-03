package com.example.RestaurantManagement;

public class DetailOrder extends IdentifiableObject
{
    public String orderID, foodID;
    public int quantity;

    @Override public Obj GetInstance()
    {
        return this;
    }

    public DetailOrder(String ID, String orderID, String foodID, int quantity)
    {
        super(ID);
        this.orderID = orderID;
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public DetailOrder()
    {

    }
}