package com.egasudrajat.elangmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.DialogFragment.DialogCatatan;
import com.egasudrajat.elangmanagement.database.EntityPembayaran;

import java.util.List;

public class AdapterDetilMainActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CallbackAdpDetil callbackAdpDetil;

    public void setCallbackAdpDetil(CallbackAdpDetil callbackAdpDetil) {
        this.callbackAdpDetil = callbackAdpDetil;
    }

    private int TYPE_1 = 1;
    private List<EntityPembayaran> list;

    public AdapterDetilMainActivity(List<EntityPembayaran> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detil_mainactivity, parent, false);
            return new DetilHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_1) {
            ((DetilHolder) holder).bindingDetil(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_1;
    }

    class DetilHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvJmlByr, tvSisaHutang, tv_tgl;
        private Button btnCttn;
        private ConstraintLayout itemdetil;

        public DetilHolder(@NonNull final View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_dtl);
            tvJmlByr = itemView.findViewById(R.id.tv_jumlahbyr);
            tvSisaHutang = itemView.findViewById(R.id.tv_sisahutang);
            tv_tgl = itemView.findViewById(R.id.tv_tglbyr);
            btnCttn = itemView.findViewById(R.id.bt_cttn);
            itemdetil = itemView.findViewById(R.id.item_detil);


        }

        void bindingDetil(final EntityPembayaran entityPembayaran) {
            tvNama.setText(entityPembayaran.getNama());
            tvJmlByr.setText("Rp." + entityPembayaran.getNominal());
            if (entityPembayaran.getHutang().equals("0")){
                tvSisaHutang.setTextColor(itemView.getContext().getColor(R.color.green));
                tvSisaHutang.setText("Lunas");
            } else {
                tvSisaHutang.setText("Rp." + entityPembayaran.getHutang());
            }
            tv_tgl.setText(entityPembayaran.getTgl());
            btnCttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogCatatan dialog = new DialogCatatan();
                    Bundle bundle = new Bundle();
                    bundle.putString("catatan", entityPembayaran.getCatatan());
                    dialog.setArguments(bundle);
                    dialog.show(((AppCompatActivity) itemView.getContext()).getSupportFragmentManager(), "catatan");
                }
            });
          itemdetil.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  callbackAdpDetil.onclickCallback(entityPembayaran);
                  return false;
              }
          });
        }
    }

    public interface CallbackAdpDetil {
        void onclickCallback(EntityPembayaran entityPembayaran);
    }
}
