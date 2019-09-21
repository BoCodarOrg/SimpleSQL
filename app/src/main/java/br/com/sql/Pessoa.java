package br.com.sql;

import com.simplesql.simplesql.annotations.AutoIncrement;
import com.simplesql.simplesql.annotations.Column;
import com.simplesql.simplesql.annotations.Key;
import com.simplesql.simplesql.annotations.Table;

@Table
public class Pessoa {
    @AutoIncrement
    @Key
    @Column(type = "INTEGER")
    private int id;

    @Column(type = "TEXT")
    private String name;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
