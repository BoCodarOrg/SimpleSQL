package br.com.sql;

import com.simplesql.simplesql.annotations.AutoIncrement;
import com.simplesql.simplesql.annotations.Column;
import com.simplesql.simplesql.annotations.ForeignKey;
import com.simplesql.simplesql.annotations.Key;
import com.simplesql.simplesql.annotations.Table;

import java.io.Serializable;

@Table
public class Categoria implements Serializable {

    @Column(type = "INTEGER")
    @AutoIncrement()
    @Key
    private int id;

    @Column(type = "INTEGER",non_null = true)
    private long idCnpj;

    @Column(type = "TEXT",non_null = true)
    private String nome;

    @Column(type = "BLOB",non_null = true)
    private byte[] img;

    @Column(type = "DECIMAL",non_null = true)
    private float valor;

    @Column(type = "INTEGER",non_null = true)
    private short value;

    public Categoria() {
    }

    public long getIdCnpj() {
        return idCnpj;
    }

    public void setIdCnpj(long idCnpj) {
        this.idCnpj = idCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}