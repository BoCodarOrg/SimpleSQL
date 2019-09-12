package br.com.sql;

/**
 * Created by Lucas Nascimento on 12/09/2019
 * Copyright (c) 2019 GFX Consultoria
 */
public class Fields {
    private String[] fields;
    private SampleSql.Select select;
    public Fields(SampleSql.Select select){
        this.select = select;
    }
    public SampleSql.Select fields(String[] fields) {
        this.fields = fields;
        return select;
    }
}
