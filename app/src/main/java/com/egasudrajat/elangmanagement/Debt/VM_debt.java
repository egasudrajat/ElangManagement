package com.egasudrajat.elangmanagement.Debt;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityDebt;

import java.util.List;

public class VM_debt extends ViewModel {
    private MutableLiveData<List<EntityDebt>> listDebt = new MutableLiveData<>();

    LiveData<List<EntityDebt>> getListDebt() {
        return listDebt;
    }

    private MutableLiveData<Integer> totalRealese = new MutableLiveData<>();

    LiveData<Integer> getTotalRelease() {
        return totalRealese;
    }

    void dataListDebt(Context context, String nama) {
        AppDatabase db = AppDatabase.getDatabase(context);
        List<EntityDebt> list;
        if (nama == null){
       list = db.Dao().getAllDebt();} else {
            list = db.Dao().getFilterDebt(nama);
        }
        listDebt.postValue(list);
    }

    void loadDataRelease(Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        int total = db.Dao().getTotalRelease();
        totalRealese.postValue(total);
    }
}
