package com.egasudrajat.elangmanagement.dataPlayer;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.AppDatabase;
import com.egasudrajat.elangmanagement.database.EntityPemain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogInputPemain extends DialogFragment {
    private boolean edit = false;
    private String TAG = "PEMAIN";
    private String rgValue = "non member";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap, decoded;
    private String pathfile_edit = "";
    private ImageView imgProfile;

    public DialogInputPemain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert);
        return inflater.inflate(R.layout.dialog_input_pemain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText edtNama = view.findViewById(R.id.edt_dl_nama);
        final EditText edtTelp = view.findViewById(R.id.edt_dl_telp);
        final EditText edtTtl = view.findViewById(R.id.edt_dl_ttl);
        imgProfile = view.findViewById(R.id.img_profil);
        final Dialog dialog = getDialog();
        RadioGroup rg = view.findViewById(R.id.radioGroup);
        edit = getTag().matches("edit") != edit;
        if (edit) {
            edtNama.setText(getArguments().getString("nama"));
            edtTelp.setText(getArguments().getString("telp"));
            edtTtl.setText(getArguments().getString("ttl"));
            if (getArguments().getString("status").matches("member")) {
                rg.check(R.id.rd_member);
            } else {
                rg.check(R.id.rd_nonmember);
            }
            String path = getArguments().getString("image_path");
            pathfile_edit = path.length() > 5 ? path : "no data";
            imgProfile.setImageURI(Uri.parse(getArguments().getString("image_path")));
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd_member:
                        rgValue = "member";
                        break;
                    case R.id.rd_nonmember:
                        rgValue = "non member";
                        break;
                }
            }
        });

        Button btBrowse = view.findViewById(R.id.btn_browse);
        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getDatabase(view.getContext());
                String nama = edtNama.getText().toString().trim();
                String telp = edtTelp.getText().toString().trim();
                String ttl = edtTtl.getText().toString().trim();
                if (TextUtils.isEmpty(nama)) {
                    edtNama.setError("di isi dulu coyy");
                } else if (TextUtils.isEmpty(telp)) {
                    edtTelp.setError("di isi dulu coyy");
                } else if (TextUtils.isEmpty(ttl)) {
                    edtTelp.setError("di isi dulu coyy");
                } else {
                    EntityPemain data = new EntityPemain();

                    data.setNama(nama);
                    data.setTelp(telp);
                    data.setTtl(ttl);
                    data.setStatus(rgValue);

                    if (pathfile_edit.matches("")) {
                        data.setImage_path(bitmapToFile(bitmap));
                    } else {
                        data.setId(getArguments().getInt("id"));
                        data.setImage_path(pathfile_edit);
                    }

                    long daoo = !edit ? db.Dao().insertPmn(data) : db.Dao().updatePmn(data);
                    VM_Pemain vm = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(VM_Pemain.class);
                    vm.dataListPemain(getActivity(), null);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imgProfile.setImageBitmap(decoded);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String bitmapToFile(Bitmap bm) {
        File myDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "img-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

}
