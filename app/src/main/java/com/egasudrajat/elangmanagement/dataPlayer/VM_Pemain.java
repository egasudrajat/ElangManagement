package com.egasudrajat.elangmanagement.dataPlayer;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPemain;

import java.util.List;

public class VM_Pemain extends ViewModel {
    private MutableLiveData<List<EntityPemain>> listPemain = new MutableLiveData<>();

    LiveData<List<EntityPemain>> getListPemain() {
        return listPemain;
    }

    void dataListPemain(Context context, String nama) {
        AppDatabase db = AppDatabase.getDatabase(context);
        List<EntityPemain> list;
        if (nama == null || nama.matches("")) {
          list = db.Dao().getAllPmn();
        } else {
            list = db.Dao().getFilterPlayer(nama);
        }
        listPemain.postValue(list);

    }


}
