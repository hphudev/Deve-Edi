package com.example.RestaurantManagement.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SellViewModel extends ViewModel {
    private List<Bill> mListBill;
    private MutableLiveData<List<Bill>> mListBillLiveData;

    public SellViewModel() {
        mListBillLiveData = new MutableLiveData<>();
        mListBill = new ArrayList<>();
    }

    public List<Bill> getListBill() {
        return mListBill;
    }

    public void setListBill(List<Bill> mListBill) {
        this.mListBill = mListBill;
        mListBillLiveData.setValue(mListBill);
    }

    public MutableLiveData<List<Bill>> getListBillLiveData() {
        return mListBillLiveData;
    }

    public void AddBill(Bill bill)
    {
        mListBill.add(bill);
        mListBillLiveData.setValue(mListBill);
    }
}
