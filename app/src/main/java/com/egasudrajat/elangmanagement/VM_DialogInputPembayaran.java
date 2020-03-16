package com.egasudrajat.elangmanagement;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPemain;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;

import java.util.List;

public class VM_DialogInputPembayaran extends ViewModel {
    private MutableLiveData<Boolean> listPmn = new MutableLiveData<>();

    LiveData<Boolean> getListPMN() {
        return listPmn;
    }

    void dataListPmbyrn(Context context, EntityPembayaran Ebyr) {
        AppDatabase db = AppDatabase.getDatabase(context);
        List<EntityPemain> list = db.Dao().getAllPmn();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().matches("member")) {
                EntityPembayaran data = new EntityPembayaran();
                data.setNama(list.get(i).getNama());
                data.setNominal(Ebyr.getNominal());
                data.setHutang(Ebyr.getHutang());
                data.setCatatan(Ebyr.getCatatan());
                data.setBulan_taun(Ebyr.getBulan_taun());
                data.setTgl(Ebyr.getTgl());
                db.Dao().insertPbyr(data);
                Log.d("VM", "dataListPmbyrn: "+Ebyr.getTgl());
                Log.d("VM2", "dataListPmbyrn2: "+list.get(i).getNama());
            }

        }

        listPmn.postValue(true);
    }
}
