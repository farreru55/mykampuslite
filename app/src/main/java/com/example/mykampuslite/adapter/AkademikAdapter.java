package com.example.mykampuslite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykampuslite.R;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.EvaluasiAkademik;
import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.model.Person;
import com.google.android.material.button.MaterialButton;

// Adapter satu layout untuk mahasiswa dan dosen
public class AkademikAdapter extends ListAdapter<Person, AkademikAdapter.AkademikViewHolder> {

    private static final int TYPE_MAHASISWA = 0;
    private static final int TYPE_DOSEN = 1;

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Person> DIFF_CALLBACK = new DiffUtil.ItemCallback<Person>() {
        @Override
        public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem.getClass().equals(newItem.getClass()) &&
                    oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem.equals(newItem);
        }
    };

    public AkademikAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public int getItemViewType(int position) {
        Person item = getItem(position);
        if (item instanceof Mahasiswa) {
            return TYPE_MAHASISWA;
        } else {
            return TYPE_DOSEN;
        }
    }

    @NonNull
    @Override
    public AkademikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_akademik, parent, false);
        return new AkademikViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AkademikViewHolder holder, int position) {
        Person person = getItem(position);

        String role = person.getRole() != null ? person.getRole().toLowerCase() : "";
        holder.tvNamaRole.setText(person.getNama() + " - " + role);
        holder.tvEmail.setText(person.getEmail());

        if (person instanceof EvaluasiAkademik) {
            String status = ((EvaluasiAkademik) person).getStatusAkademik();
            holder.tvStatus.setText("Status: " + status);
            holder.tvStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
        }

        if (person instanceof Mahasiswa) {
            Mahasiswa m = (Mahasiswa) person;
            holder.tvDetail1.setText("NIM: " + m.getNim());
            holder.tvDetail2.setText("Jurusan: " + m.getJurusan());
            holder.tvDetail3.setText(String.format("IPK: %.2f", m.getIpk()));
            holder.tvDetail3.setVisibility(View.VISIBLE);
        } else if (person instanceof Dosen) {
            Dosen d = (Dosen) person;
            holder.tvDetail1.setText("NIDN: " + d.getNidn());
            holder.tvDetail2.setText("Mata Kuliah: " + d.getMataKuliahDiampu());
            holder.tvDetail3.setText("Jumlah MK: " + d.getJumlahMataKuliah());
            holder.tvDetail3.setVisibility(View.VISIBLE);
        }

        holder.tvTimestamp.setText("Dibuat: " + person.getCreatedAt());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(person);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(person);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class AkademikViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNamaRole, tvEmail, tvDetail1, tvDetail2, tvDetail3, tvStatus, tvTimestamp;
        final MaterialButton btnEdit, btnDelete;

        AkademikViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaRole = itemView.findViewById(R.id.tvNamaRole);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDetail1 = itemView.findViewById(R.id.tvDetail1);
            tvDetail2 = itemView.findViewById(R.id.tvDetail2);
            tvDetail3 = itemView.findViewById(R.id.tvDetail3);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnItemClickListener {
        void onEditClick(Person person);
        void onDeleteClick(Person person);
    }
}
