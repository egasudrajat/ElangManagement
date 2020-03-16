package com.egasudrajat.elangmanagement.Debt;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.EntityDebt;

import java.util.List;

public class AdapterDebt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CallbackAdapterDebt callback;
    private int TYPE1 = 1;
    List<EntityDebt> list;

    public AdapterDebt(List<EntityDebt> list) {
        this.list = list;
    }

    public void setCallback(CallbackAdapterDebt callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt, parent, false);
            return new debtHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE1) {
            ((debtHolder) holder).bindingDebt(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE1;
    }

    class debtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNama, tvTotalpnjm, tvSisaHtng, tvProgresByr, tvTglPnjm;
        private Button btnBayar, btnSejarahByr, btnCatatan;
        private CardView cdDebt;

        public debtHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTglPnjm = itemView.findViewById(R.id.tv_tgl_pinjam);
            tvTotalpnjm = itemView.findViewById(R.id.tv_total_pnjmn);
            tvSisaHtng = itemView.findViewById(R.id.tv_sisa);
            tvProgresByr = itemView.findViewById(R.id.tv_progres_bayar);
            cdDebt = itemView.findViewById(R.id.cddebt);


            btnBayar = itemView.findViewById(R.id.bt_bayar);
            btnSejarahByr = itemView.findViewById(R.id.bt_sejarah_bayar);
            btnCatatan = itemView.findViewById(R.id.bt_cttn);
            btnCatatan.setOnClickListener(this);
            btnSejarahByr.setOnClickListener(this);
            btnBayar.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                callback.onItemviewClick(list.get(getAdapterPosition()));
                }
            });
        }

        void bindingDebt(final EntityDebt entityDebt) {
            tvNama.setText(entityDebt.getNama_peminjam());
            tvTglPnjm.setText(entityDebt.getTanggal_pinjam());
            tvTotalpnjm.setText("Rp." + entityDebt.getTotal_pinjam());

            if (entityDebt.getTotal_pinjam() == entityDebt.getProgres_bayar()) {
                tvSisaHtng.setText("Lunas");
                tvSisaHtng.setTextColor(itemView.getResources().getColor(R.color.green));
            } else {
                tvSisaHtng.setText("Rp." + (entityDebt.getTotal_pinjam() - entityDebt.getProgres_bayar()));
            }
            tvProgresByr.setText("Rp." + entityDebt.getProgres_bayar());

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_bayar:
                    callback.onBtnBayar(list.get(getAdapterPosition()));
                    break;
                case R.id.bt_sejarah_bayar:
                    callback.onBtnSejarahPmbyrn(list.get(getAdapterPosition()));
                    break;
                case R.id.bt_cttn:
                    callback.onBtnCttn(list.get(getAdapterPosition()).getCatatan());
                    break;

            }
        }
    }

    public interface CallbackAdapterDebt {
        void onBtnBayar(EntityDebt entityDebt);
        void onBtnSejarahPmbyrn(EntityDebt entityDebt);
        void onBtnCttn(String string);
        void onItemviewClick(EntityDebt entityDebt);
    }
}
