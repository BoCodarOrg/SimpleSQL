package br.com.sql;

import com.simplesql.simplesql.annotations.Column;
import com.simplesql.simplesql.annotations.Table;

@Table
public class Teste {
    @Column(type = "TEXT")
    private String teste;
}
