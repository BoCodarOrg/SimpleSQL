package com.simplesql.crud;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.simplesql.config.SimpleSQL.getString;
import static com.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Lucas Nascimento
 * Method UPDATE
 */
public class Update {
    private Object typeObject;
    private String tableName, field, writeSQL, column, table;
    private StringBuilder SQLString = new StringBuilder();

    private Object value;
    private Object[] values, fields;

    /**
     * @param typeObject
     */
    public Update(Object typeObject) {
        this.tableName = typeObject.getClass().getSimpleName();
        this.typeObject = typeObject;
//        SQLString = new StringBuilder();
        SQLString.append("UPDATE ")
                .append(tableName);
    }

    /**
     * @param fields to be changed
     */
    public Update set(String... fields) {
        this.fields = fields;
        int i = 0;
        StringBuilder stringSet = new StringBuilder();
        for (String s : fields) {
            stringSet.append(s)
                    .append(" = %")
                    .append(i)
                    .append(",");
            i++;
        }
        SQLString.append(" SET ").append(stringSet);
        return this;
    }

    /**
     * @param values new values ​​to be changed for fields
     */
    public Update values(Object... values) {
        this.values = values;
        return this;
    }

    public Update where() {
        SQLString.append(" WHERE ");
        return this;
    }

    public Update equals() {
        SQLString.append(" = ");
        return this;
    }

    public Update or() {
        SQLString.append(" OR ");
        return this;
    }

    public Update and() {
        SQLString.append(" AND ");
        return this;
    }

    public Update column(String name) {
        this.column = name;
        SQLString.append(' ').append(name).append(' ');
        return this;
    }

    public Update fieldString(String value) {
        this.field = field;
        SQLString.append("\"").append(value).append("\"");
        return this;
    }

    public Update fieldInt(int value) {
        this.value = value;
        SQLString.append(value);
        return this;
    }

    public Update fiedlByteArray(byte[] value) {
        this.value = value;
        SQLString.append(value);
        return this;
    }

    public Update fieldLong(long value) {
        this.value = value;
        SQLString.append(value);
        return this;
    }

    public Update fieldFloat(float value) {
        this.value = value;
        SQLString.append(value);
        return this;
    }

    public Update fieldBoolean(boolean value) {
        this.value = value;
        SQLString.append(value);
        return this;
    }

    public Update writeSQL(String operator) {
        this.writeSQL = operator;
        SQLString.append(' ').append(operator).append(' ');
        return this;
    }

    public void execute() throws SQLException {
        SQLiteDatabase write = helperBD.getReadableDatabase();
        int i = 0;
        for (Object s : fields) {
            String replace = "%" + i;
            String aux = SQLString.toString().replace(replace, (CharSequence) getString((String) values[i]));
            SQLString = new StringBuilder(aux);
            i++;
        }
        SQLString.append(";");

        write.execSQL(SQLString.toString());
    }
}

