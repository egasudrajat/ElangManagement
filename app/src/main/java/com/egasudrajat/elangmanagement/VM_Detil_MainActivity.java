package com.egasudrajat.elangmanagement;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;

import java.util.List;

public class VM_Detil_MainActivity extends ViewModel {
    private MutableLiveData<List<EntityPembayaran>> listBln = new MutableLiveData<>();

    LiveData<List<EntityPembayaran>> getListPbyr() {
        return listBln;
    }

    void dataListPmbyrn(Context context, String key, String nama) {
        AppDatabase db = AppDatabase.getDatabase(context);
        List<EntityPembayaran> list;
        if (nama.length()<1) {
            list = db.Dao().getFilterPbyr(key);
        } else {
            list = db.Dao().getFilterPbyrDAnNama(key,"%"+ nama);
        }
        listBln.postValue(list);
    }
}
