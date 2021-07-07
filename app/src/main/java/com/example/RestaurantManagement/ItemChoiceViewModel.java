package com.example.RestaurantManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.RestaurantManagement.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemChoiceViewModel extends ViewModel {

    private MutableLiveData<List<Item>> mListItemLiveData;
    private List<Item> items;

    public ItemChoiceViewModel() {
        mListItemLiveData = new MutableLiveData<>();
        items = new ArrayList<>();
    }

    public LiveData<List<Item>> getmListItemLiveData() {
        return mListItemLiveData;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = new ArrayList<>(items);
        mListItemLiveData.setValue(items);
    }

    public void AddItem(Item item)
    {
        this.items.add(item);
        mListItemLiveData.setValue(items);
    }
    public void ClearItems()
    {
        this.items.clear();
        mListItemLiveData.setValue(items);
    }

}