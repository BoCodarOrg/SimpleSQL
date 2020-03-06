package com.simplesql.config;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simplesql.annotations.AutoIncrement;
import com.simplesql.annotations.Column;
import com.simplesql.annotations.ForeignKey;
import com.simplesql.annotations.Key;
import com.simplesql.annotations.Table;
import com.simplesql.annotations.Unique;
import com.simplesql.crud.DeleteColumn;
import com.simplesql.crud.Insert;
import com.simplesql.crud.Select;
import com.simplesql.crud.Update;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class SimpleSQL {
    public static SQLiteOpenHelper helperBD;

    public SimpleSQL(SQLiteOpenHelper newHelperBD) {
        helperBD = newHelperBD;
    }

    public Select selectTable(Object typeObject) {
        return new Select(typeObject, "SELECT");
    }

    public Select selectSingle(Object typeObject) {
        return new Select(typeObject, "SINGLE");
    }

    /**
     * COUNT(campo)
     */
    public Select selectCount(Object typeObject) {
        return new Select(typeObject, "COUNT");
    }

    /**
     * MAX(campo)
     */
    public Select selectMax(Object typeObject) {
        return new Select(typeObject, "MAX");
    }

    /**
     * MIN(campo)
     */
    public Select selectMin(Object typeObject) {
        return new Select(typeObject, "MIN");
    }

    public DeleteColumn deleteColumn(Object typeObject) {
        return new DeleteColumn(typeObject);
    }

    public Update updateTable(Object typeObject) {
        return new Update(typeObject);
    }

    public Insert insert(Object o) {
        return new Insert(o);
    }

    public static Object getString(String string) {
        if (string.contains("[a-zA-Z]")) {
            return "'" + string + "'";
        }
        return string;
    }

    /**
     * @author Paulo Iury
     * Method CREATE TABLE
     */
    public void create(Object obj, SQLiteDatabase db) throws SQLException {
        if (obj.getClass().getAnnotation(Table.class) != null) {
            StringBuilder columns = new StringBuilder();
            StringBuilder foreignKeys = new StringBuilder();
            String tabela = obj.getClass().getSimpleName();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // As the attributes are private, we set it to visible
                Column column =
                        field.getAnnotation(Column.class);
                if (column != null) {
                    if (field != fields[0])
                        columns.append(" , ");
                    columns.append(field.getName())
                            .append(' ')
                            .append(column.type().toUpperCase())
                            .append(checkAnnotations(field, column.non_null()));
                    if (field.isAnnotationPresent(ForeignKey.class)) {
                        ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                        foreignKeys.append(" , FOREIGN KEY (")
                                .append(field.getName())
                                .append(")")
                                .append(" REFERENCES ")
                                .append(foreignKey.table().getSimpleName())
                                .append("(")
                                .append(foreignKey.column())
                                .append(")");
                    }
                } else
                    throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");
            }
            db.execSQL("CREATE TABLE " + tabela + " ( " + columns + foreignKeys + ");");
        } else
            throw new SQLException("This class does not have the table annotation");
    }

    /**
     * @param field
     * @param not_null
     * @return annotations for field
     */

    private String checkAnnotations(Field field, boolean not_null) {
        StringBuilder annotations = new StringBuilder();
        if (field.isAnnotationPresent(Key.class))
            annotations.append(" PRIMARY KEY");
        if (field.isAnnotationPresent(AutoIncrement.class))
            annotations.append(" AUTOINCREMENT");
        if (not_null && !field.isAnnotationPresent(Key.class))
            annotations.append(" NOT NULL");
        if (field.isAnnotationPresent(Unique.class))
            annotations.append(" UNIQUE");
        return annotations.toString();
    }


    /**
     * @author Paulo Iury
     * Method DELETE TABLE
     */
    public void deleteTable(Object obj, SQLiteDatabase db) throws SQLException {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        if (persistable != null) {
            db.execSQL("DROP TABLE IF EXISTS " + obj.getClass().getSimpleName());
        } else
            throw new SQLException("This class does not have the table annotation");
    }
}
