package com.example.restaurantmanagement;

public class Food extends IdentifiableObject
{
    public String name;
    public int price;

    @Override public IdentifiableObject GetInstance()
    {
        return this;
    }

    public Food(String ID, String name, int price)
    {
        super(ID);
        this.name = name;
        this.price = price;
    }

    public Food()
    {

    }
}