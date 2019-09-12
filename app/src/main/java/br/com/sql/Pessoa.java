package br.com.sql;

import br.com.sql.annotations.AutoIncrement;
import br.com.sql.annotations.Column;
import br.com.sql.annotations.Key;
import br.com.sql.annotations.Table;

@Table
public class Pessoa {
    @Column
    @AutoIncrement()
    @Key
    private int id;
    @Column(non_null = true)
    private String nome;
    @Column(non_null = true)
    private int idade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
