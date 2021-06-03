package com.example.RestaurantManagement;

public class Parameter extends Obj
{
    public String name;
    public int value;

    String GetName()
    {
        return this.name;
    }

    @Override public Obj GetInstance()
    {
        return this;
    }

    public Parameter(Parameter parameter)
    {
        this.name = parameter.name;
        this.value = parameter.value;
    }

    public Parameter(String name, int value)
    {
        this.name = name;
        this.value = value;
    }

    public Parameter()
    {

    }
}