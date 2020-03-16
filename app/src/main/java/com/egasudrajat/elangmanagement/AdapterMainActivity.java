package com.egasudrajat.elangmanagement;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egasudrajat.elangmanagement.database.EntityListBulan;

import java.util.List;

public class AdapterMainActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_1 = 1;
    private List<EntityListBulan> list;
    private CallbackMainActv callback;

    public void setCallback(CallbackMainActv callback) {
        this.callback = callback;
    }

    public AdapterMainActivity(List<EntityListBulan> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bulan, parent, false);
            return new BulanHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_1){
            ((BulanHolder) holder).bulanBinding(list.get(position));
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

    class BulanHolder extends RecyclerView.ViewHolder {
        private TextView tvBulan, tvTotalPms;

        public BulanHolder(@NonNull View itemView) {
            super(itemView);
            tvBulan = itemView.findViewById(R.id.tv_bln);
            tvTotalPms = itemView.findViewById(R.id.tv_total_pms);
        }

        void bulanBinding(final EntityListBulan entityListBulan){
            tvBulan.setText(entityListBulan.getTgl());
            tvTotalPms.setText("Total Rp."+entityListBulan.getJumlah_bayar());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Detil_MainActivity.class);
                    intent.putExtra("data", entityListBulan.getTgl());
                    intent.putExtra("data2", entityListBulan.getTgl_lengkap());
                    itemView.getContext().startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callback.onLongClick(entityListBulan);
                    return false;
                }
            });
        }
    }

    public interface CallbackMainActv{
        void onLongClick(EntityListBulan entityListBulan);
    }
}
