package com.egasudrajat.elangmanagement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;

import java.util.List;

public class Detil_MainActivity extends AppCompatActivity {
    String TAG = Detil_MainActivity.class.getSimpleName();
    private RecyclerView rv;
    private AdapterDetilMainActivity adapter;
    private AppDatabase db;
    private TextView tvJumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil__main);
        final String getIntent = getIntent().getStringExtra("data");
        setTitle("Detil Pembayaran " + getIntent);

        tvJumlah = findViewById(R.id.tvJumlah);
        rv = findViewById(R.id.rv_detilmain);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getDatabase(this);
        final VM_Detil_MainActivity vm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VM_Detil_MainActivity.class);
        vm.dataListPmbyrn(this, getIntent, "");
        vm.getListPbyr().observe(this, new Observer<List<EntityPembayaran>>() {
            @Override
            public void onChanged(List<EntityPembayaran> entityPembayarans) {
                String tvJmlh = "Jumlah "+ entityPembayarans.size();
                tvJumlah.setText(tvJmlh);
                adapter = new AdapterDetilMainActivity(entityPembayarans);
                rv.setAdapter(adapter);

                adapter.setCallbackAdpDetil(new AdapterDetilMainActivity.CallbackAdpDetil() {
                    @Override
                    public void onclickCallback(final EntityPembayaran entityPembayaran) {
                        final CharSequence[] dialogitem = {"Edit", "Delete"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Detil_MainActivity.this);
                        dialog.setCancelable(true);
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        InputPembayaran input = new InputPembayaran();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("id", entityPembayaran.getId());
                                        bundle.putString("nama", entityPembayaran.getNama());
                                        bundle.putString("tgl", entityPembayaran.getTgl());
                                        bundle.putString("nominal", entityPembayaran.getNominal());
                                        bundle.putString("catatan", entityPembayaran.getCatatan());
                                        bundle.putString("hutang", entityPembayaran.getHutang());
                                        input.setArguments(bundle);
                                        input.show(getSupportFragmentManager(), Detil_MainActivity.class.getSimpleName());
                                        break;
                                    case 1:
                                        db.Dao().deletePbyr(entityPembayaran);
                                        vm.dataListPmbyrn(Detil_MainActivity.this, getIntent, "");
                                        break;
                                }
                            }
                        }).show();
                    }
                });
            }
        });

        final InputPembayaran inputPembayaran = new InputPembayaran();
        Button btn_tbh = findViewById(R.id.btn_tbh);
        btn_tbh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", getIntent().getStringExtra("data2"));
                inputPembayaran.setArguments(bundle);
                inputPembayaran.show(getSupportFragmentManager(), "tambahdaridetil");
            }
        });

        SearchView searchView = findViewById(R.id.searchview_toolbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vm.dataListPmbyrn(Detil_MainActivity.this, getIntent, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                vm.dataListPmbyrn(Detil_MainActivity.this, getIntent, "");
                return false;
            }
        });

        inputPembayaran.setCallback(new InputPembayaran.inputPembayaranCallback() {
            @Override
            public void onsaveCallback() {
                vm.dataListPmbyrn(Detil_MainActivity.this, getIntent().getStringExtra("data"), "");
            }
        });

        inputPembayaran.setCallbackUpdate(new InputPembayaran.inputPembayaranUpdateCallback() {
            @Override
            public void onUpdateCallback() {
                vm.dataListPmbyrn(Detil_MainActivity.this, getIntent().getStringExtra("data"), "");
            }
        });

    }

}
