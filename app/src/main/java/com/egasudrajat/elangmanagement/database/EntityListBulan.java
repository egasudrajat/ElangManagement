package com.egasudrajat.elangmanagement.database;


import androidx.room.ColumnInfo;


public class EntityListBulan {

    @ColumnInfo(name = "bulan_taun")
    private String tgl;

    @ColumnInfo(name = "tgl")
    private String tgl_lengkap;

    @ColumnInfo(name = "jumlah_bayar")
    private String jumlah_bayar;

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJumlah_bayar() {
        return jumlah_bayar;
    }

    public void setJumlah_bayar(String jumlah_bayar) {
        this.jumlah_bayar = jumlah_bayar;
    }

    public String getTgl_lengkap() {
        return tgl_lengkap;
    }

    public void setTgl_lengkap(String tgl_lengkap) {
        this.tgl_lengkap = tgl_lengkap;
    }
}

