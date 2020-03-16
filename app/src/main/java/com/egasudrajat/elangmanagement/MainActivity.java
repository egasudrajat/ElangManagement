package com.egasudrajat.elangmanagement;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.egasudrajat.elangmanagement.Debt.DebtActivity;
import com.egasudrajat.elangmanagement.dataPlayer.PlayerActivity;
import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityListBulan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MainActivity";
    private TextView menuHome, menuPemain, menuDebt;
    private SQLiteToExcel sqLiteToExcel;
    private String directory_path = Environment.getExternalStorageDirectory().getPath() + "/";
    private AdapterMainActivity adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Pembayaran Member Elang");
        File file = new File(directory_path);
        if (!file.exists()) {
            Log.v("File Created", String.valueOf(file.mkdirs()));
        }
        final RecyclerView rv = findViewById(R.id.recycler_bln);
        menuHome = findViewById(R.id.menu_home);
        menuDebt = findViewById(R.id.menu_debt);
        menuPemain = findViewById(R.id.menu_pemain);
        menuDebt.setOnClickListener(this);
        menuHome.setOnClickListener(this);
        menuPemain.setOnClickListener(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final VM_MainActivity vm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VM_MainActivity.class);
        vm.dataListPmbyrn(this);
        vm.getListBln().observe(this, new Observer<List<EntityListBulan>>() {
            @Override
            public void onChanged(List<EntityListBulan> entityListBulans) {
                adapter = new AdapterMainActivity(entityListBulans);
                rv.setAdapter(adapter);

                adapter.setCallback(new AdapterMainActivity.CallbackMainActv() {
                    @Override
                    public void onLongClick(final EntityListBulan entityListBulan) {
                        final CharSequence[] dialogitem = {"Hapus"};
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setCancelable(true);
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    AppDatabase db = AppDatabase.getDatabase(MainActivity.this);
                                    int tt = db.Dao().DeleteOneMonth(entityListBulan.getTgl());
                                    Log.d(TAG, "onClick: " + tt);
                                    vm.dataListPmbyrn(MainActivity.this);
                                    Toast.makeText(MainActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
                    }
                });

            }
        });

        FloatingActionButton fab = findViewById(R.id.fabMain);
        final InputPembayaran input = new InputPembayaran();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.show(getSupportFragmentManager(), MainActivity.class.getSimpleName());
            }
        });

        input.setCallback(new InputPembayaran.inputPembayaranCallback() {
            @Override
            public void onsaveCallback() {
                vm.dataListPmbyrn(MainActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.generate_xls) {
            permission();
        } else if (item.getItemId() == R.id.finance_app) {
            Intent launchIntent = null;
            try {
                launchIntent = getPackageManager().getLeanbackLaunchIntentForPackage("com.chad.financialrecord");
            } catch (java.lang.NoSuchMethodError e) {
            }

            if (launchIntent == null)
                launchIntent = getPackageManager().getLaunchIntentForPackage("com.chad.financialrecord");

            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
            } else {
                Toast.makeText(this, "Aplikasi Catatan Keuangan belum di instal", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_home:
                break;
            case R.id.menu_pemain:
                startActivity(new Intent(this, PlayerActivity.class));
                break;
            case R.id.menu_debt:
                startActivity(new Intent(this, DebtActivity.class));
                break;
        }
    }

    private void sql2xl() {
        sqLiteToExcel = new SQLiteToExcel(this, "db_elang", directory_path);
        sqLiteToExcel.exportAllTables("elang.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(MainActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();

//                File file = new File(filePath);
//                MimeTypeMap map = MimeTypeMap.getSingleton();
//                String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
//                String type = map.getMimeTypeFromExtension(ext);
//
//                if (type == null)
//                    type = "*/*";
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri data = Uri.fromFile(file);
//                intent.setDataAndType(data, type);
//                startActivity(intent);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void permission() {

//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                ).withListener(new MultiplePermissionsListener() {
//            @Override
//            public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//            }
//            @Override
//            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//            }
//        }).check();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        sql2xl();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "Anda tidak bisa generate excel", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


}
