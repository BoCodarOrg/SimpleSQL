package br.com.sql;

import br.com.sql.annotations.Table;

public class SampleSql {

    public static void insert(Object o) {
        String alow="";
        if (o.getClass().isAnnotationPresent(Table.class)) {
            String teste = "";
        } else {
            String teste = "";
        }
    }
}
