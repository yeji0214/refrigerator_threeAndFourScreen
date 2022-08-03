package com.example.textrecognitionex;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;

public class ItemViewModel extends ViewModel implements Parcelable {
    public MutableLiveData<ArrayList<String>> itemsLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Date>> datesLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> memosLiveData = new MutableLiveData<>();
    public ArrayList<String> items = new ArrayList<>();
    public ArrayList<Date> dates = new ArrayList<>(); // 유통기한 저장
    public ArrayList<String> memos = new ArrayList<>(); // 메모 저장

    public int longClickPosition;

    public ItemViewModel() {}
    protected ItemViewModel(Parcel in) {
        items = in.createStringArrayList();
        longClickPosition = in.readInt();
    }

    public static final Creator<ItemViewModel> CREATOR = new Creator<ItemViewModel>() {
        @Override
        public ItemViewModel createFromParcel(Parcel in) {
            return new ItemViewModel(in);
        }

        @Override
        public ItemViewModel[] newArray(int size) {
            return new ItemViewModel[size];
        }
    };

    public void addItem(String item) {
        if(items.contains(item))
            return;
        items.add(item);
        itemsLiveData.setValue(items);
    }

    public void addExpirationDate(Date date) {
//        if (dates.contains(date))
//            return;
        dates.add(date);
        datesLiveData.setValue(dates);
    }

    public void addMemo(String memo) {
        memos.add(memo);
        memosLiveData.setValue(memos);
    }

    public int getItemSize() { return items.size(); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(items);
        parcel.writeInt(longClickPosition);
    }
}
