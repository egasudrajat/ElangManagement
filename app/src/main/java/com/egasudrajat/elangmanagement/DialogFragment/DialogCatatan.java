package com.egasudrajat.elangmanagement.DialogFragment;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.egasudrajat.elangmanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogCatatan extends DialogFragment {
    private Dialog dialog;

    public DialogCatatan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_catatan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = getDialog();
        EditText edtCttn = view.findViewById(R.id.edt_cttn);
        assert getArguments() != null;
        String catatan = getArguments().getString("catatan");
        if (!TextUtils.isEmpty(catatan)){
            edtCttn.setText(catatan);
        } else {
            edtCttn.setText("Tidak Ada Catatan");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
    }
}
