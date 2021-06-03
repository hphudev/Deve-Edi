package com.example.RestaurantManagement;

public class IdentifiableObject extends Obj
{
    public String ID;

    public String GetID()
    {
        return this.ID;
    }

    @Override public Obj GetInstance()
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