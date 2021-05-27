package com.example.restaurantmanagement;

public class DetailBill extends IdentifiableObject
{
    public String billID, foodID;
    public int quantity;

    @Override public IdentifiableObject GetInstance()
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