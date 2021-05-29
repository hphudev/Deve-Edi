package com.example.RestaurantManagement.ui.home;

public class ItemChoice {
    private String Name;
    private int numChoice;

    public ItemChoice(String name, int numChoice) {
        Name = name;
        this.numChoice = numChoice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNumChoice() {
        return numChoice;
    }

    public void setNumChoice(int numChoice) {
        this.numChoice = numChoice;
    }
}
