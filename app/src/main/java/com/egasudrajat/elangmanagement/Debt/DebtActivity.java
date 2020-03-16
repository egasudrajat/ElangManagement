package com.egasudrajat.elangmanagement.Debt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.Detil_MainActivity;
import com.egasudrajat.elangmanagement.DialogFragment.DialogCatatan;
import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityDebt;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DebtActivity extends AppCompatActivity {
    private RecyclerView rvDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        final TextView tvJumlah = findViewById(R.id.tvJumlah);
        Button btn_tbh = findViewById(R.id.btn_tbh);
        btn_tbh.setVisibility(View.GONE);
        setTitle("Menu Peminjaman");
        FloatingActionButton fab = findViewById(R.id.fab_debt);
        rvDebt = findViewById(R.id.rv_debt);
        rvDebt.setHasFixedSize(true);
        rvDebt.setLayoutManager(new LinearLayoutManager(this));
        final DialogInputDebt dialog = new DialogInputDebt();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "insert");
            }
        });

        final VM_debt vm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VM_debt.class);
        vm.dataListDebt(this, null);
        vm.getListDebt().observe(this, new Observer<List<EntityDebt>>() {
            @Override
            public void onChanged(List<EntityDebt> entityDebts) {
                AdapterDebt adp = new AdapterDebt(entityDebts);
                rvDebt.setAdapter(adp);
                adp.setCallback(new AdapterDebt.CallbackAdapterDebt() {
                    @Override
                    public void onBtnBayar(EntityDebt entityDebt) {
                        DialogBayarDebt dialog = new DialogBayarDebt();
                        Bundle b = new Bundle();
                        b.putInt("id", entityDebt.getId());
                        b.putInt("progres", entityDebt.getProgres_bayar());
                        b.putString("sejarah_byr", entityDebt.getSejarah_bayar());
                        dialog.setArguments(b);
                        dialog.show(getSupportFragmentManager(), "bayar");
                    }

                    @Override
                    public void onBtnSejarahPmbyrn(EntityDebt entityDebt) {
                        DialogDebtSejarahPbyr dlg = new DialogDebtSejarahPbyr();
                        Bundle b2 = new Bundle();
                        b2.putString("sejarah", entityDebt.getSejarah_bayar());
                        dlg.setArguments(b2);
                        dlg.show(getSupportFragmentManager(), "sejarah");
                    }

                    @Override
                    public void onBtnCttn(String string) {
                        DialogCatatan d = new DialogCatatan();
                        Bundle b3 = new Bundle();
                        b3.putString("catatan", string);
                        d.setArguments(b3);
                        d.show(getSupportFragmentManager(), "catatan");
                    }

                    @Override
                    public void onItemviewClick(final EntityDebt entityDebt) {
                        final CharSequence[] dialogitem = {"Edit", "Hapus"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DebtActivity.this);
                        dialog.setCancelable(true);
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        DialogInputDebt dlg = new DialogInputDebt();
                                        Bundle b = new Bundle();
                                        b.putParcelable("debt_data", entityDebt);
                                        dlg.setArguments(b);
                                        dlg.show(getSupportFragmentManager(), "edit");
                                        break;
                                    case 1:
                                        AppDatabase db = AppDatabase.getDatabase(DebtActivity.this);
                                        db.Dao().deleteDebt(entityDebt);
                                        vm.dataListDebt(DebtActivity.this, null);
                                        Toast.makeText(DebtActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }).show();
                    }
                });
            }
        });

        vm.loadDataRelease(this);
        vm.getTotalRelease().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    String total = "Pengeluaran "+integer;
                    tvJumlah.setText(total);
                }
            }
        });

        SearchView searchView = findViewById(R.id.searchview_toolbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vm.dataListDebt(DebtActivity.this, query);
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
                vm.dataListDebt(DebtActivity.this, null);
                return false;
            }
        });

    }
}
