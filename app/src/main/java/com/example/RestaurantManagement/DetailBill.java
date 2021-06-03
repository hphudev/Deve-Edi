package com.example.RestaurantManagement;

public class DetailBill extends IdentifiableObject
{
    public String billID, foodID;
    public int quantity;

    @Override public Obj GetInstance()
    {
        return this;
    }

    public DetailBill(String ID, String billID, String foodID, int quantity)
    {
        super(ID);
        this.billID = billID;
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public DetailBill()
    {

    }
}