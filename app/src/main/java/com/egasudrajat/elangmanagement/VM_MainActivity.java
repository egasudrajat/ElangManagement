package com.egasudrajat.elangmanagement;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityListBulan;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;

import java.util.List;

public class VM_MainActivity extends ViewModel {
    private MutableLiveData<List<EntityListBulan>> listBln = new MutableLiveData<>();

    LiveData<List<EntityListBulan>> getListBln(){
        return listBln;
    }

    void dataListPmbyrn(Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        List<EntityListBulan> list = db.Dao().getAllBln();
        listBln.postValue(list);
    }
}
