package br.com.sql;

import com.simplesql.annotations.Column;
import com.simplesql.annotations.Table;

@Table
public class Teste {
    @Column(type = "TEXT")
    private String teste;
}
