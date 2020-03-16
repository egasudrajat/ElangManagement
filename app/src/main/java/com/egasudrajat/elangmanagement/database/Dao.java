package com.egasudrajat.elangmanagement.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT * FROM tb_pembayaran WHERE bulan_taun = :bln_taun")
    List<EntityPembayaran> getFilterPbyr(String bln_taun);

    @Query("SELECT * FROM tb_pembayaran WHERE bulan_taun LIKE :bln_taun AND nama LIKE :nama")
    List<EntityPembayaran> getFilterPbyrDAnNama(String bln_taun, String nama);

    @Query("SELECT bulan_taun, tgl , SUM(nominal) AS jumlah_bayar FROM tb_pembayaran GROUP BY bulan_taun ORDER BY tgl DESC")
    List<EntityListBulan> getAllBln();

    @Query("UPDATE tb_pembayaran SET nama = :nama, nominal = :nomi, hutang = :hutg, catatan = :cttn WHERE id = :id ")
    int updateQuery(int id, String nama, String nomi, String hutg, String cttn);

    @Query("DELETE FROM tb_pembayaran WHERE bulan_taun = :bln_taun")
    int DeleteOneMonth(String bln_taun);

    @Insert
    long insertPbyr(EntityPembayaran entityPembayarans);

    @Delete
    int deletePbyr(EntityPembayaran entityPembayaran);


//=================== pemabayaran ======================


    @Query("SELECT * FROM tb_pemain")
    List<EntityPemain> getAllPmn();

    @Insert
    long insertPmn(EntityPemain entityPemains);

    @Delete
    int deletePmn(EntityPemain entityPemain);

    @Update
    int updatePmn(EntityPemain entityPemain);


    @Query("SELECT * FROM tb_pemain WHERE nama LIKE :nama ")
    List<EntityPemain> getFilterPlayer(String nama);

//=================== pemain ======================


    @Insert
    long insertDebt(EntityDebt entityDebt);

    @Update
    void editDebt(EntityDebt entityDebt);

    @Delete
    void deleteDebt(EntityDebt entityDebt);

    @Query("SELECT * FROM tb_debt")
    List<EntityDebt> getAllDebt();

    @Query("SELECT SUM(total_pinjam) AS total_release FROM tb_debt")
    int getTotalRelease();

    @Query("SELECT * FROM tb_debt WHERE nama_peminjam = :nama")
    List<EntityDebt> getFilterDebt(String nama);

    @Query("UPDATE tb_debt SET progres_bayar = :progres, sejarah_bayar =:historybyr WHERE id = :id ")
    void updateBayarDebt(int id, int progres, String historybyr);

    //=================== debt======================

}
