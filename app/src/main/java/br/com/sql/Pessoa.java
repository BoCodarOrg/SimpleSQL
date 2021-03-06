package br.com.sql;

import com.simplesql.annotations.AutoIncrement;
import com.simplesql.annotations.Column;
import com.simplesql.annotations.ForeignKey;
import com.simplesql.annotations.Key;
import com.simplesql.annotations.Table;
import com.simplesql.annotations.Unique;

@Table
public class Pessoa {
    @AutoIncrement
    @Key
    @Column(type = "INTEGER")
    private int id;

    @Column(type = "TEXT")
    private String name;

    @Unique
    @Column(type = "TEXT",non_null = true)
    private String email;

    @Unique
    @Column(type = "TEXT",non_null = true)
    private String phone;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
