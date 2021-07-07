package com.example.RestaurantManagement;

public interface Unit_interface {
    void onAddUnit(boolean isExist, Unit unit);
    void onEditUnit(boolean isSuccess, int position, String content);
    void onGetUnit(boolean isSuccess, String content);
}
