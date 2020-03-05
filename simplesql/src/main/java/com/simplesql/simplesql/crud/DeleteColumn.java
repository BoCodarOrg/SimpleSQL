package com.simplesql.simplesql.crud;

import android.database.sqlite.SQLiteDatabase;

import static com.simplesql.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Paulo Iury
 * Method DELETE COLUMN
 */
public class DeleteColumn {
    private String table, SQLString;

    public DeleteColumn(Object objectType) {
        this.table = objectType.getClass().getSimpleName();
        this.SQLString = "DELETE FROM " + table;
    }

    public DeleteColumn equals() {
        SQLString += " = ";
        return this;
    }

    public DeleteColumn where() {
        SQLString += " WHERE ";
        return this;
    }

    public DeleteColumn and() {
        SQLString += " AND ";
        return this;
    }

    public DeleteColumn or() {
        SQLString += " OR ";
        return this;
    }

    public DeleteColumn like(String s) {
        SQLString += "LIKE " + "\"s\"";
        return this;
    }

    public DeleteColumn fieldString(String value) {
        SQLString += "\"" + value + "\"";
        return this;
    }

    public DeleteColumn fieldInt(int value) {
        SQLString += value;
        return this;
    }

    public DeleteColumn fieldLong(long value) {
        SQLString += value;
        return this;
    }

    public DeleteColumn fieldFloat(float value) {
        SQLString += value;
        return this;
    }

    public DeleteColumn fieldBoolean(boolean value) {
        SQLString += value;
        return this;
    }

    public DeleteColumn column(String field) {
        SQLString += field;
        return this;
    }

    public DeleteColumn writeSQL(String sql) {
        SQLString += sql;
        return this;
    }

    public boolean execute() {
        SQLiteDatabase write = helperBD.getWritableDatabase();
        try {
            write.execSQL(SQLString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}