package com.egasudrajat.elangmanagement;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;
import com.egasudrajat.elangmanagement.helper.Spref;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputPembayaran extends DialogFragment implements View.OnClickListener {
    private EditText edt_nmpmn, edt_jmlByr, edt_htng, edt_catatan;
    private TextView tv_date;
    TextInputLayout tx1;
    private boolean generate_all;
    private Dialog dialog;
    private String bulanTaun;
    String TAG = "cobaaa";
    private SimpleDateFormat dateFormatter;
    private inputPembayaranCallback callback;
    private static inputPembayaranUpdateCallback callbackUpdate;
    private Spref spref;

    private int idUpdate;
    private boolean is_detilActivity;

    public void setCallback(inputPembayaranCallback callback) {
        this.callback = callback;
    }

    public static void setCallbackUpdate(inputPembayaranUpdateCallback callbackUpdat) {
        callbackUpdate = callbackUpdat;
    }

    public InputPembayaran() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_input_pembayaran, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generate_all = false;
        dialog = getDialog();
        spref = new Spref(getContext());
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
       tx1 = view.findViewById(R.id.tx1);
        edt_nmpmn = view.findViewById(R.id.edt_nmpmn);
        edt_catatan = view.findViewById(R.id.edt_catatan);
        edt_jmlByr = view.findViewById(R.id.edt_jumlah_byr);
        edt_htng = view.findViewById(R.id.edt_hutang);
        Button btn_datepicker = view.findViewById(R.id.bt_datepicker);
        Button btn_save = view.findViewById(R.id.bt_save_pmbyrn);
        Button btnGenerateAll = view.findViewById(R.id.bt_generate_all);
        tv_date = view.findViewById(R.id.tv_date);
        Calendar date = Calendar.getInstance();
        String resultDate = dateFormatter.format(date.getTime());
//        tv_date.setText(dateFormatter.format(date.getTime()));
        TextView hrgamember = view.findViewById(R.id.tv_hrgamember);
        hrgamember.setText(String.valueOf(spref.getTotalHargaMember()));

        if (getTag().equals(Detil_MainActivity.class.getSimpleName())) {
            is_detilActivity = true;
        } else {
            is_detilActivity = false;
        }

        if (is_detilActivity) {
            try {
                assert getArguments() != null;
                Bundle data = getArguments();
                if (data != null) {
                    idUpdate = data.getInt("id", 0);
                    edt_nmpmn.setText(data.getString("nama"));
                    edt_catatan.setText(data.getString("catatan"));
                    edt_jmlByr.setText(data.getString("nominal"));
                    edt_htng.setText(data.getString("hutang"));
                    tv_date.setText(data.getString("tgl"));

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        edt_jmlByr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edt_jmlByr.getText().toString().matches("")) {
                    int pbyr = Integer.valueOf(edt_jmlByr.getText().toString().trim());
                    int total = spref.getTotalHargaMember() - pbyr;
                    edt_htng.setText(String.valueOf(total));
                } else {
                    edt_htng.setText(String.valueOf(spref.getTotalHargaMember()));
                }
            }
        });

        btn_datepicker.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btnGenerateAll.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        final SimpleDateFormat formatBlnTaun = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

        switch (v.getId()) {
            case R.id.bt_datepicker:
                final Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        tv_date.setText(dateFormatter.format(newDate.getTime()));
                        bulanTaun = formatBlnTaun.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

                break;
            case R.id.bt_save_pmbyrn:
                AppDatabase db = AppDatabase.getDatabase(getContext());
                EntityPembayaran data = new EntityPembayaran();
                Boolean isFill = true;

                if (tv_date.getText().toString().matches("0000:00:00")) {
                    Toast.makeText(getContext(),  "Mohon isi tanggal", Toast.LENGTH_SHORT).show();
                    isFill = false;
                }
                if (edt_jmlByr.getText().toString().trim().matches("")) {
                    edt_jmlByr.setError("Mohon untuk di isi");
                    isFill = false;
                }
                if (isFill) {
                    data.setNama(edt_nmpmn.getText().toString().trim());
                    data.setNominal(edt_jmlByr.getText().toString().trim());
                    data.setHutang(edt_htng.getText().toString());
                    data.setCatatan(edt_catatan.getText().toString());

                    long stts = 0;
                    if (is_detilActivity) {
                        stts = db.Dao().updateQuery(idUpdate, data.getNama(), data.getNominal(), data.getHutang(), data.getCatatan());

                    } else {
                        data.setBulan_taun(bulanTaun);
                        data.setTgl(tv_date.getText().toString());
                        if (generate_all) {
                            VM_DialogInputPembayaran vm = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(VM_DialogInputPembayaran.class);
                            vm.dataListPmbyrn(getActivity(), data);
                            vm.getListPMN().observe(getActivity(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    callback.onsaveCallback();
                                    clearText();
                                }
                            });
                        } else {
                            stts = db.Dao().insertPbyr(data);
                        }
                    }

                    if (stts != 0) {
                        if (is_detilActivity) {
                            callbackUpdate.onUpdateCallback();
                        } else {
                            callback.onsaveCallback();
                        }
                        clearText();
                    }
                }
                break;
            case R.id.bt_generate_all:
                generate_all = !generate_all;
                if (generate_all) {
                    tx1.setVisibility(View.GONE);
                    edt_nmpmn.setVisibility(View.GONE);
                } else {
                    tx1.setVisibility(View.VISIBLE);
                    edt_nmpmn.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    public interface inputPembayaranCallback {
        void onsaveCallback();
    }

    public interface inputPembayaranUpdateCallback {
        void onUpdateCallback();
    }

    private void clearText() {
        edt_nmpmn.setText("");
        edt_jmlByr.setText("");
        edt_htng.setText("");
        edt_catatan.setText("");
        dialog.dismiss();
        Toast.makeText(getContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();
    }
}
