package com.egasudrajat.elangmanagement.dataPlayer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.Detil_MainActivity;
import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPemain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        final TextView tvJumlah = findViewById(R.id.tvJumlah);
        Button btTbh = findViewById(R.id.btn_tbh);
        btTbh.setVisibility(View.GONE);
        setTitle("Data Pemain");
        db = AppDatabase.getDatabase(this);
        final RecyclerView rv = findViewById(R.id.recycler_player);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final VM_Pemain vm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VM_Pemain.class);
        vm.dataListPemain(this, null);
        vm.getListPemain().observe(this, new Observer<List<EntityPemain>>() {
            @Override
            public void onChanged(List<EntityPemain> entityPemains) {
                String jmlh = "Jumlah " + entityPemains.size();
                tvJumlah.setText(jmlh);

                AdapterPlayer adapterPlayer = new AdapterPlayer(entityPemains);
                rv.setAdapter(adapterPlayer);
                adapterPlayer.setCallback(new AdapterPlayer.CallbackAdapterPlayer() {
                    @Override
                    public void onclickCallback(final EntityPemain entityPemain) {
                        final CharSequence[] dialogitem = {"Edit", "Hapus",};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PlayerActivity.this);
                        dialog.setCancelable(true);
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        DialogInputPemain dpemain = new DialogInputPemain();
                                        Bundle b = new Bundle();
                                        b.putInt("id", entityPemain.getId());
                                        b.putString("nama", entityPemain.getNama());
                                        b.putString("status", entityPemain.getStatus());
                                        b.putString("telp", entityPemain.getTelp());
                                        b.putString("ttl", entityPemain.getTtl());
                                        b.putString("image_path", entityPemain.getImage_path());
                                        dpemain.setArguments(b);
                                        dpemain.show(getSupportFragmentManager(), "edit");
                                        break;
                                    case 1:
                                        db.Dao().deletePmn(entityPemain);
                                        vm.dataListPemain(PlayerActivity.this, null);
                                        if (!entityPemain.getImage_path().matches("")) {
                                            File file = new File(entityPemain.getImage_path());
                                            if (file.exists()) {
                                                file.delete();
                                            }
                                        }
                                        break;
                                }
                            }
                        }).show();
                    }
                });
            }
        });

        SearchView searchView = findViewById(R.id.searchview_toolbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vm.dataListPemain(PlayerActivity.this, query);
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
                vm.dataListPemain(PlayerActivity.this, null);
                return false;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInputPemain dialog = new DialogInputPemain();
                FragmentManager fr = getSupportFragmentManager();
                dialog.show(fr, "insert");
            }
        });
    }
}
