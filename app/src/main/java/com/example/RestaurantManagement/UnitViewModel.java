package com.example.RestaurantManagement;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class UnitViewModel extends ViewModel {
    private MutableLiveData<List<Unit>> mListUnitLiveData;
    private List<Unit> mListUnit;
    private int position = -1;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public UnitViewModel() {
        this.mListUnitLiveData = new MutableLiveData<>();
        this.mListUnit = new ArrayList<>();
        //AddUnit(new Unit(false, "Phu"));
    }

    public MutableLiveData<List<Unit>> getmListUnitLiveData() {
        return mListUnitLiveData;
    }

    public Unit getUnit(int postion)
    {
        if (mListUnit.size() != 0)
            return mListUnit.get(postion);
        return null;
    }

    public void AddUnit(Unit unit)
    {
        mListUnit.add(unit);
        mListUnitLiveData.setValue(mListUnit);
    }

    public void SetUnit(int postion, Unit unit)
    {
        mListUnit.set(postion, unit);
        mListUnitLiveData.setValue(mListUnit);
    }

    public int getSize()
    {
        if (mListUnit != null)
            return mListUnit.size();
        return 0;
    }

    public void ClearListUnit()
    {
        mListUnit.clear();
        mListUnitLiveData.setValue(mListUnit);
        position = -1;
    }

}
