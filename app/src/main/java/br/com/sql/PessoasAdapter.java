package br.com.sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.sql.tables.Pessoa;

/**
 * Created by Lucas Nascimento on 17/09/2019
 * Copyright (c) 2019 GFX Consultoria
 */
public class PessoasAdapter extends RecyclerView.Adapter<PessoasAdapter.PessoasHolder> {
    List<Pessoa> lstPessoas;
    Context context;

    public PessoasAdapter(List<Pessoa> lstPessoas, Context context) {
        this.lstPessoas = lstPessoas;
        this.context = context;
    }

    @NonNull
    @Override
    public PessoasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pessoa,null,true);
        return new PessoasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PessoasHolder holder, int position) {
        Pessoa p = lstPessoas.get(position);
        holder.nome.setText(p.getNome());
        holder.idade.setText(String.valueOf(p.getIdade()));
        holder.id.setText(String.valueOf(p.getId()));
    }

    @Override
    public int getItemCount() {
        return lstPessoas.size();
    }

    class PessoasHolder extends RecyclerView.ViewHolder{
        private TextView nome,idade,id;
        public PessoasHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome);
            idade = itemView.findViewById(R.id.idade);
            id = itemView.findViewById(R.id.id);
        }
    }
}
