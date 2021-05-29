package com.example.RestaurantManagement.ui.restaurant_menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RestaurantMenuViewModel extends ViewModel {

    private MutableLiveData<String> mComment;
    private MutableLiveData<String> mTurial;

    public RestaurantMenuViewModel() {
        mComment = new MutableLiveData<>();
        mTurial = new MutableLiveData<>();
        mComment.setValue("Chưa có món nào trong thực đơn");
        mTurial.setValue("Bấm vào đây hoặc dấu + để thêm món");
    }

    public LiveData<String> getTextComment() {
        return mComment;
    }
    public LiveData<String> getTextTurial() {
        return mTurial;
    }
}