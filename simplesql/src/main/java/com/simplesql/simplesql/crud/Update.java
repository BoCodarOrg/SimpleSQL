package com.simplesql.simplesql.crud;

import android.database.sqlite.SQLiteDatabase;

import static com.simplesql.simplesql.config.SimpleSQL.getString;
import static com.simplesql.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Lucas Nascimento
 * Method UPDATE
 */
public class Update {
    private Object typeObject;
    private String tableName, field, writeSQL, column, table, stringSet;
    private String SQLString;
    private Object value;
    private Object[] values, fields;

    /**
     * @param typeObject
     */
    public Update(Object typeObject) {
        this.tableName = typeObject.getClass().getSimpleName();
        this.typeObject = typeObject;
        SQLString = "UPDATE " + tableName;
    }

    /**
     * @param fields to be changed
     */
    public Update set(String[] fields) {
        this.fields = fields;
        stringSet = "";
        int i = 0;
        for (String s : fields) {
            stringSet += s + " = %" + i + ",";
            i++;
        }
        stringSet = stringSet.substring(0, stringSet.length() - 1);
        SQLString = SQLString + " SET " + stringSet;
        return this;
    }

    /**
     * @param values new values ​​to be changed for fields
     */
    public Update values(Object[] values) {
        this.values = values;
        return this;
    }

    public Update where() {
        SQLString = SQLString + " WHERE ";
        return this;
    }

    public Update equals() {
        SQLString = SQLString + " = ";
        return this;
    }

    public Update or() {
        SQLString = SQLString + " OR ";
        return this;
    }

    public Update and() {
        SQLString = SQLString + " AND ";
        return this;
    }

    public Update column(String name) {
        this.column = name;
        SQLString = SQLString + " " + name + " ";
        return this;
    }

    public Update fieldString(String value) {
        this.field = field;
        SQLString = SQLString + "\"" + value + "\"";
        return this;
    }

    public Update fieldInt(int value) {
        this.value = value;
        SQLString = SQLString + value;
        return this;
    }

    public Update fiedlByteArray(byte[] value) {
        this.value = value;
        SQLString = SQLString + value;
        return this;
    }

    public Update fieldLong(long value) {
        this.value = value;
        SQLString = SQLString + value;
        return this;
    }

    public Update fieldFloat(float value) {
        this.value = value;
        SQLString = SQLString + value;

        return this;
    }

    public Update fieldBoolean(boolean value) {
        this.value = value;
        SQLString = SQLString + value;

        return this;
    }

    public Update writeSQL(String operator) {
        this.writeSQL = operator;
        SQLString = SQLString + " " + operator + " ";
        return this;
    }

    public boolean execute() {
        SQLiteDatabase write = helperBD.getReadableDatabase();
        int i = 0;
        for (Object s : fields) {
            String replace = "%" + i;

            SQLString = SQLString.replace(replace, (CharSequence) getString((String) values[i]));
            i++;
        }
        SQLString = SQLString + ";";
        try {
            write.execSQL(SQLString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

