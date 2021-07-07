package com.example.RestaurantManagement.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private List<Bill> bills;
    private MutableLiveData<List<Bill>> billMutableLiveData;
    public HomeViewModel() {
        bills = new ArrayList<>();
        billMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getmText() {
        return mText;
    }

    public void setmText(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills =  new ArrayList<>(bills) ;
        this.billMutableLiveData.setValue(this.bills);
    }

    public LiveData<List<Bill>> getBillMutableLiveData() {
        return billMutableLiveData;
    }

    public void setBillMutableLiveData(MutableLiveData<Bill> billMutableLiveData) {
        this.billMutableLiveData.setValue(bills);
    }

    public void AddBill(Bill bill)
    {
        bills.add(bill);
        this.billMutableLiveData.setValue(bills);
    }

    public void DelBill(String id_table)
    {
        for (int i = 0; i < bills.size(); i++)
        {
            if (bills.get(i).getId_table().equals(id_table))
            {
                bills.remove(i);
                break;
            }
        }
        billMutableLiveData.setValue(bills);
    }

    public void ClearBills()
    {
        bills.clear();
        billMutableLiveData.setValue(bills);
    }
    public LiveData<String> getText() {
        return mText;
    }
}