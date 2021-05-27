package com.example.restaurantmanagement;

public class IdentifiableObject
{
    public String ID;

    public String GetID()
    {
        return this.ID;
    }

    public IdentifiableObject GetInstance()
    {
        return this;
    }

    public IdentifiableObject(String ID)
    {
        this.ID = ID;
    }

    public IdentifiableObject()
    {
        this.ID = null;
    }
}