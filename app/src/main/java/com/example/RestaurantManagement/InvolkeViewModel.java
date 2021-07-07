package com.example.RestaurantManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class InvolkeViewModel extends ViewModel {

    private List<Item> items;
    private MutableLiveData<List<Item>> mutableLiveDataItem;
    private int sumTotal;

    public InvolkeViewModel() {
        items = new ArrayList<>();
        mutableLiveDataItem = new MutableLiveData<>();
        sumTotal = 0;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        for (Item item : items)
        {
            sumTotal += item.getNumChoice() * item.getPrice();
        }
        this.mutableLiveDataItem.setValue(this.items);
    }

    public int getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(int sumTotal) {
        this.sumTotal = sumTotal;
    }

    public LiveData<List<Item>> getMutableLiveDataItem() {
        return mutableLiveDataItem;
    }

    public void setMutableLiveDataItem(MutableLiveData<List<Item>> mutableLiveDataItem) {
        this.mutableLiveDataItem = mutableLiveDataItem;
    }

    public void ClearItem()
    {
        this.items.clear();
        this.mutableLiveDataItem.setValue(items);
    }
}
