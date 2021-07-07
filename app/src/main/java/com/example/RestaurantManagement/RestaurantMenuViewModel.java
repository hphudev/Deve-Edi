package com.example.RestaurantManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.RestaurantManagement.Item;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuViewModel extends ViewModel {

    private MutableLiveData<String> mComment;
    private MutableLiveData<String> mTurial;
    private MutableLiveData<List<Item>> mListItemLiveData;
    private List<Item> items;

    public RestaurantMenuViewModel() {
        mComment = new MutableLiveData<>();
        mTurial = new MutableLiveData<>();
        mListItemLiveData = new MutableLiveData<>();
        items = new ArrayList<>();
//        mComment.setValue("Chưa có món nào trong thực đơn");
//        mTurial.setValue("Bấm vào đây hoặc dấu + để thêm món");
    }

    public LiveData<String> getTextComment() {
        return mComment;
    }
    public LiveData<String> getTextTurial() {
        return mTurial;
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