package br.com.sql.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.sql.annotations.AutoIncrement;
import br.com.sql.annotations.Column;
import br.com.sql.annotations.ForeignKey;
import br.com.sql.annotations.Key;
import br.com.sql.annotations.Table;

public class SQL {
    public static String create(Object obj) {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        String columns = "";
        if (persistable != null) {
            String tabela = obj.getClass().getSimpleName();
            Field[] fields = obj.getClass().getDeclaredFields();
            int count = 0;
            List<Field> foreignKeys = new ArrayList<>();
            for (Field field : fields) {
                // como os atributos são private,
                // setamos ele como visible
                field.setAccessible(true);
                // Se o atributo tem a anotação
                Column column =
                        field.getAnnotation(Column.class);
                if (column != null) {
                    if (count == 0) {
                        columns += field.getName() + " " + column.type();
                    } else {
                        columns += " , " + field.getName() + " " + column.type();

                    }
                    columns += checkAnnotations(field, column);
                    if (field.isAnnotationPresent(ForeignKey.class))
                        foreignKeys.add(field);
                }
                count++;
            }
            String sql = "CREATE TABLE IF NOT EXISTS " + tabela + " ( "
                    + columns;
            for (Field key : foreignKeys) {
                ForeignKey foreignKey = key.getAnnotation(ForeignKey.class);
                sql += " , FOREIGN KEY (" + key.getName() + ")" +
                        " REFERENCES " + foreignKey.reference().getSimpleName() + "(" + key.getName() + ")";
            }
            sql += ");";
            return sql;
        }
        return "";
    }

    public static String insert(Object obj) throws Throwable {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        String value = "";
        String columns = "";
        if (persistable != null) {
            String tabela = obj.getClass().getSimpleName();
            Field[] fields = obj.getClass().getDeclaredFields();
            int count = 0;
            boolean comma = true;
            for (Field field : fields) {
                // como os atributos são private,
                // setamos ele como visible
                field.setAccessible(true);
                // Se o atributo tem a anotação
                if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(AutoIncrement.class)) {
                    if (!comma) {
                        columns += field.getName();
                        value += field.get(obj).toString();
                        comma = true;
                    } else {
                        columns += " , " + field.getName();
                        value += " , " + field.get(obj).toString();
                    }
                } else {
                    comma = false;
                }
            }
            String sql = "INSERT INTO " + tabela +
                    "(" + columns + ") " +
                    " VALUES ( " + value + " );";
            return sql;
        }
        return "";
    }

    private static String checkAnnotations(Field c, Column column) {
        String annotations = "";
            if (column.non_null() && !c.isAnnotationPresent(Key.class))
            annotations += " NOT NULL";
        if (c.isAnnotationPresent(Key.class))
            annotations += " PRIMARY KEY";
        if (c.isAnnotationPresent(AutoIncrement.class))
            annotations += " AUTOINCREMENT";

        return annotations;
    }
}
