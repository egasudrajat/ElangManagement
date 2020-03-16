package com.egasudrajat.elangmanagement.dataPlayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.egasudrajat.elangmanagement.R;
import com.egasudrajat.elangmanagement.database.EntityPemain;

import java.util.List;

public class AdapterPlayer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_PLAYER = 1;
    private List<EntityPemain> listPmn;
    private CallbackAdapterPlayer callback;

    public void setCallback(CallbackAdapterPlayer callback) {
        this.callback = callback;
    }



    public AdapterPlayer(List<EntityPemain> listPmn) {
        this.listPmn = listPmn;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PLAYER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
            return new PlayerHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PLAYER) {
            ((PlayerHolder) holder).playerBinding(listPmn.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listPmn.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PLAYER;
    }

    class PlayerHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvTelp, tvStatus, tvTtl;
        private ImageView imgProfil;

        public PlayerHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTelp = itemView.findViewById(R.id.tv_notelp);
            tvTtl = itemView.findViewById(R.id.tv_ttl);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imgProfil = itemView.findViewById(R.id.img_profil);

        }

        void playerBinding(final EntityPemain entityPemain) {
            tvNama.setText(entityPemain.getNama());
            tvTelp.setText(entityPemain.getTelp());
            tvTtl.setText(entityPemain.getTtl());
            tvStatus.setText(entityPemain.getStatus());
            Glide.with(itemView.getContext())
                    .load(entityPemain.getImage_path().length() < 3 ? R.drawable.elang : entityPemain.getImage_path())
                    .apply(new RequestOptions().override(100, 100))
                    .into(imgProfil);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onclickCallback(entityPemain);
                    Log.d("adapter", "onClick: ");
                }
            });
        }
    }

    public interface CallbackAdapterPlayer{
        void onclickCallback(EntityPemain entityPemain);
    }
}
