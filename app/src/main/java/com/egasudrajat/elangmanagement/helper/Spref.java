package com.egasudrajat.elangmanagement.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Spref {
    private SharedPreferences prefs;
    private static String spref_name = "setting_prefs";

    private static final String totalHargaMember = "total_harga_member ";

    public Spref(Context context){
        prefs = context.getSharedPreferences(spref_name, Context.MODE_PRIVATE);
    }

    public void setTotalHargaMember(int input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(totalHargaMember, input);
        editor.apply();
    }

    public int getTotalHargaMember() {
        return prefs.getInt(totalHargaMember, 35000);
    }

}
