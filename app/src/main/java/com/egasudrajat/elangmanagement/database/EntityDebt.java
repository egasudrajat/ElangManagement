package com.egasudrajat.elangmanagement.database;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import static com.egasudrajat.elangmanagement.database.EntityDebt.TABLE_NAME;


@androidx.room.Entity(tableName = TABLE_NAME)
public class EntityDebt implements Parcelable {
    public final static String TABLE_NAME = "tb_debt";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "nama_peminjam")
    private String nama_peminjam;

    @Nullable
    @ColumnInfo(name = "telp")
    private String telp;

    @ColumnInfo(name = "tanggal_pinjam")
    private String tanggal_pinjam;

    @ColumnInfo(name = "total_pinjam")
    private int total_pinjam;

    @ColumnInfo(name = "progres_bayar")
    private int progres_bayar;

    @Nullable
    @ColumnInfo(name = "catatan")
    private String catatan;

    @Nullable
    @ColumnInfo(name = "sejarah_bayar")
    private String sejarah_bayar;

    @Nullable
    public String getSejarah_bayar() {
        return sejarah_bayar;
    }

    public void setSejarah_bayar(@Nullable String sejarah_bayar) {
        this.sejarah_bayar = sejarah_bayar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_peminjam() {
        return nama_peminjam;
    }

    public void setNama_peminjam(String nama_peminjam) {
        this.nama_peminjam = nama_peminjam;
    }

    @Nullable
    public String getTelp() {
        return telp;
    }

    public void setTelp(@Nullable String telp) {
        this.telp = telp;
    }

    public String getTanggal_pinjam() {
        return tanggal_pinjam;
    }

    public void setTanggal_pinjam(String tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public int getTotal_pinjam() {
        return total_pinjam;
    }

    public void setTotal_pinjam(int total_pinjam) {
        this.total_pinjam = total_pinjam;
    }

    public int getProgres_bayar() {
        return progres_bayar;
    }

    public void setProgres_bayar(int progres_bayar) {
        this.progres_bayar = progres_bayar;
    }

    @Nullable
    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(@Nullable String catatan) {
        this.catatan = catatan;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nama_peminjam);
        dest.writeString(this.telp);
        dest.writeString(this.tanggal_pinjam);
        dest.writeInt(this.total_pinjam);
        dest.writeInt(this.progres_bayar);
        dest.writeString(this.catatan);
        dest.writeString(this.sejarah_bayar);
    }

    public EntityDebt() {
    }

    protected EntityDebt(Parcel in) {
        this.id = in.readInt();
        this.nama_peminjam = in.readString();
        this.telp = in.readString();
        this.tanggal_pinjam = in.readString();
        this.total_pinjam = in.readInt();
        this.progres_bayar = in.readInt();
        this.catatan = in.readString();
        this.sejarah_bayar = in.readString();
    }

    public static final Parcelable.Creator<EntityDebt> CREATOR = new Parcelable.Creator<EntityDebt>() {
        @Override
        public EntityDebt createFromParcel(Parcel source) {
            return new EntityDebt(source);
        }

        @Override
        public EntityDebt[] newArray(int size) {
            return new EntityDebt[size];
        }
    };
}