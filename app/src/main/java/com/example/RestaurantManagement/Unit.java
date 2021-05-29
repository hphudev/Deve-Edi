package com.example.RestaurantManagement;

public class Unit {
    private boolean Checked;
    private String Name;

    public Unit(boolean checked, String name) {
        Checked = checked;
        Name = name;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
