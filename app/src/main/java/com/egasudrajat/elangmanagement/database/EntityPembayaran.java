package com.egasudrajat.elangmanagement.database;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import static com.egasudrajat.elangmanagement.database.EntityPembayaran.TABLE_NAME;

@androidx.room.Entity(tableName = TABLE_NAME )
public class EntityPembayaran {
    public final static String TABLE_NAME = "tb_pembayaran";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "tgl")
    private String tgl;

    @ColumnInfo(name = "nominal")
    private String nominal;

    @ColumnInfo(name = "hutang")
    private String hutang;

    @ColumnInfo(name = "bulan_taun")
    private String bulan_taun;

    @Nullable
    @ColumnInfo(name = "catatan")
    private String catatan;

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getBulan_taun() {
        return bulan_taun;
    }

    public void setBulan_taun(String bulan_taun) {
        this.bulan_taun = bulan_taun;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getHutang() {
        return hutang;
    }

    public void setHutang(String hutang) {
        this.hutang = hutang;
    }

}

