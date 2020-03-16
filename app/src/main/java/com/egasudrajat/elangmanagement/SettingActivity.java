package com.egasudrajat.elangmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.egasudrajat.elangmanagement.helper.Spref;

public class SettingActivity extends AppCompatActivity {
    private EditText edtTotalhrga;
    private Button btSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final Spref spref = new Spref(this);
        edtTotalhrga = findViewById(R.id.edt_total);
        edtTotalhrga.setText(String.valueOf(spref.getTotalHargaMember()));

        btSimpan = findViewById(R.id.btn_simpan);
        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               spref.setTotalHargaMember(Integer.valueOf(edtTotalhrga.getText().toString().trim()));
               finish();
            }
        });

    }
}
