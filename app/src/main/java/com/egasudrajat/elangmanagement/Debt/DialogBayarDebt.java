package com.egasudrajat.elangmanagement.Debt;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogBayarDebt extends DialogFragment {
    private Dialog dialog;
    private EditText edtBayar;


    public DialogBayarDebt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_bayar_debt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = getDialog();
        edtBayar = view.findViewById(R.id.edt_bayar_debt);
        Button btnSave = view.findViewById(R.id.btn_save_debt);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bayar = edtBayar.getText().toString().trim();
                if (bayar.matches("")) {
                    edtBayar.setError("harus isi");
                } else {
                    SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date currentDate = Calendar.getInstance().getTime();
                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    int id = getArguments().getInt("id");
                    int currentProgress = getArguments().getInt("progres");
                    String curSjrhbyr = getArguments().getString("sejarah_byr");
                    db.Dao().updateBayarDebt(id, Integer.valueOf(bayar) + currentProgress, curSjrhbyr+", #" + dformat.format(currentDate)+ " Rp."+bayar);
                    Toast.makeText(getContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    VM_debt vm = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(VM_debt.class);
                    vm.dataListDebt(getActivity(), null);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}
