package com.egasudrajat.elangmanagement.Debt;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityDebt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogInputDebt extends DialogFragment {
    private EditText edtnama, edtJmlPnjmn, edtNotelp, edtCatatan;
    private TextView tvtglpinjam;
    private DatePickerDialog datePickerDialog;
    private Dialog dialog;
    private EntityDebt mdebt;

    public DialogInputDebt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_input_debt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dialog = getDialog();
        edtnama = view.findViewById(R.id.edt_nama);
        tvtglpinjam = view.findViewById(R.id.tv_date_debt);
        edtJmlPnjmn = view.findViewById(R.id.edt_jmlhtng);
        edtNotelp = view.findViewById(R.id.edt_telp);
        edtCatatan = view.findViewById(R.id.edt_catatan_debt);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mdebt = getArguments().getParcelable("debt_data");

            edtCatatan.setText(mdebt.getCatatan());
            tvtglpinjam.setText(mdebt.getTanggal_pinjam());
            edtNotelp.setText(mdebt.getTelp());
            edtnama.setText(mdebt.getNama_peminjam());
            edtJmlPnjmn.setText(String.valueOf(mdebt.getTotal_pinjam()));
        }

        Button btnSimpan = view.findViewById(R.id.bt_savedebt);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityDebt data = new EntityDebt();
                String nama = edtnama.getText().toString().trim();
                String jmlpnjm = edtJmlPnjmn.getText().toString().trim();
                String notelp = edtNotelp.getText().toString().trim();
                String cttn = edtCatatan.getText().toString().trim();
                String tglpinjam = tvtglpinjam.getText().toString();
                if (nama.matches("")) {
                    edtnama.setError("di isi dulu");
                } else if (jmlpnjm.matches("")) {
                    edtJmlPnjmn.setError("di isi dulu");
                } else if (tvtglpinjam.getText().toString().matches("0000:00:00")) {
                    Toast.makeText(getContext(), "Tanggal Harus di isi", Toast.LENGTH_SHORT).show();
                } else {
                    AppDatabase db = AppDatabase.getDatabase(getContext());

                    data.setNama_peminjam(nama);
                    data.setTotal_pinjam(Integer.valueOf(jmlpnjm));
                    data.setTelp(notelp.matches("") ? "xxx" : notelp);
                    data.setCatatan(cttn.matches("") ? "Tidak ada catatan" : cttn);
                    data.setTanggal_pinjam(tglpinjam);
                    VM_debt vm = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(VM_debt.class);

                    if (getTag().matches("edit")) {
                        data.setId(mdebt.getId());
                        data.setSejarah_bayar(mdebt.getSejarah_bayar());
                        data.setProgres_bayar(mdebt.getProgres_bayar());
                        db.Dao().editDebt(data);
                        vm.dataListDebt(getActivity(), null);
                        vm.loadDataRelease(getActivity());
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Berhasil diubah", Toast.LENGTH_SHORT).show();
                    } else {
                        data.setProgres_bayar(0);
                        data.setSejarah_bayar(tvtglpinjam.getText().toString() + " awal");

                        db.Dao().insertDebt(data);
                        vm.dataListDebt(getActivity(), null);
                        vm.loadDataRelease(getActivity());
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Berhasil tambah", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        Button btDatePicker = view.findViewById(R.id.bt_datepicker_debt);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        tvtglpinjam.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
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
