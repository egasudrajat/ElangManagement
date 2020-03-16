package com.egasudrajat.elangmanagement.database;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import static com.egasudrajat.elangmanagement.database.EntityPemain.TABLE_NAME;

@androidx.room.Entity(tableName = TABLE_NAME )
public class EntityPemain {
    public final static String TABLE_NAME = "tb_pemain";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "ttl")
    private String ttl;

    @Nullable
    @ColumnInfo(name = "telp")
    private String telp;

    @Nullable
    @ColumnInfo(name = "status")
    private String status;

    @Nullable
    @ColumnInfo(name = "image_path")
    private String image_path;

    @Nullable
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(@Nullable String image_path) {
        this.image_path = image_path;
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

    @Nullable
    public String getTelp() {
        return telp;
    }

    public void setTelp(@Nullable String telp) {
        this.telp = telp;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
