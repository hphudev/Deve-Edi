package com.example.RestaurantManagement;

public class User extends IdentifiableObject
{
    public String phoneNumber, email, username, password, collaboratorID;

    @Override public Obj GetInstance()
    {
        return this;
    }

    public User(String ID, String phoneNumber, String email, String username, String password,
                String collaboratorID)
    {
        super(ID);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.collaboratorID = collaboratorID;
    }

    public User()
    {

    }
}