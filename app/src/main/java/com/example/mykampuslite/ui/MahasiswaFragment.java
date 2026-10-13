package com.example.mykampuslite.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykampuslite.R;
import com.example.mykampuslite.adapter.AkademikAdapter;
import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.model.Person;
import com.example.mykampuslite.viewmodel.MahasiswaViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

// Tab mahasiswa: list + form tambah/edit/hapus
public class MahasiswaFragment extends Fragment {

    private MahasiswaViewModel viewModel;
    private AkademikAdapter adapter;
    private AlertDialog addDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mahasiswa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ExtendedFloatingActionButton fabTambah = view.findViewById(R.id.fabTambah);

        adapter = new AkademikAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MahasiswaViewModel.class);

        viewModel.getAllMahasiswa().observe(getViewLifecycleOwner(), mahasiswaList -> {
            if (mahasiswaList != null) {
                adapter.submitList(new ArrayList<>(mahasiswaList));
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }
        });

        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                if (addDialog != null && addDialog.isShowing()) {
                    addDialog.dismiss();
                    addDialog = null;
                }
                Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                viewModel.clearSaveSuccess();
            }
        });

        fabTambah.setOnClickListener(v -> showAddDialog());

        adapter.setOnItemClickListener(new AkademikAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Person person) {
                if (person instanceof Mahasiswa) {
                    showEditDialog((Mahasiswa) person);
                }
            }

            @Override
            public void onDeleteClick(Person person) {
                if (person instanceof Mahasiswa) {
                    showDeleteConfirmation((Mahasiswa) person);
                }
            }
        });
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit, null);

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        tvTitle.setText("Tambah Mahasiswa");

        dialogView.findViewById(R.id.layoutDialogNim).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.layoutDialogJurusan).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.layoutDialogIpk).setVisibility(View.VISIBLE);

        TextInputEditText etDialogNama = dialogView.findViewById(R.id.etDialogNama);
        TextInputEditText etDialogEmail = dialogView.findViewById(R.id.etDialogEmail);
        TextInputEditText etDialogNim = dialogView.findViewById(R.id.etDialogNim);
        TextInputEditText etDialogJurusan = dialogView.findViewById(R.id.etDialogJurusan);
        TextInputEditText etDialogIpk = dialogView.findViewById(R.id.etDialogIpk);

        addDialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal", null)
                .create();

        addDialog.show();
        // Tutup form hanya jika data valid
        addDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v ->
                viewModel.insert(
                        getText(etDialogNama),
                        getText(etDialogEmail),
                        getText(etDialogNim),
                        getText(etDialogJurusan),
                        getText(etDialogIpk)
                )
        );
    }

    private void showEditDialog(Mahasiswa mahasiswa) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit, null);

        dialogView.findViewById(R.id.layoutDialogNim).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.layoutDialogJurusan).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.layoutDialogIpk).setVisibility(View.VISIBLE);

        TextInputEditText etDialogNama = dialogView.findViewById(R.id.etDialogNama);
        TextInputEditText etDialogEmail = dialogView.findViewById(R.id.etDialogEmail);
        TextInputEditText etDialogNim = dialogView.findViewById(R.id.etDialogNim);
        TextInputEditText etDialogJurusan = dialogView.findViewById(R.id.etDialogJurusan);
        TextInputEditText etDialogIpk = dialogView.findViewById(R.id.etDialogIpk);

        etDialogNama.setText(mahasiswa.getNama());
        etDialogEmail.setText(mahasiswa.getEmail());
        etDialogNim.setText(mahasiswa.getNim());
        etDialogJurusan.setText(mahasiswa.getJurusan());
        etDialogIpk.setText(String.valueOf(mahasiswa.getIpk()));

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> viewModel.update(
                        mahasiswa,
                        getText(etDialogNama),
                        getText(etDialogEmail),
                        getText(etDialogNim),
                        getText(etDialogJurusan),
                        getText(etDialogIpk)
                ))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void showDeleteConfirmation(Mahasiswa mahasiswa) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus Data")
                .setMessage("Yakin ingin menghapus data " + mahasiswa.getNama() + "?")
                .setPositiveButton("Hapus", (dialog, which) -> viewModel.delete(mahasiswa))
                .setNegativeButton("Batal", null)
                .show();
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        if (addDialog != null && addDialog.isShowing()) {
            addDialog.dismiss();
        }
        addDialog = null;
        super.onDestroyView();
    }
}
