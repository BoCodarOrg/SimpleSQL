package com.simplesql.crud;

import android.database.sqlite.SQLiteDatabase;

import static com.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Paulo Iury
 * Method DELETE COLUMN
 */
public class DeleteColumn {
    private StringBuilder SQLString = new StringBuilder();

    public DeleteColumn(Object objectType) {
        this.SQLString.append("DELETE FROM ")
                .append(objectType.getClass().getSimpleName()); //Name table
    }

    public DeleteColumn equals() {
        SQLString.append(" = ");
        return this;
    }

    public DeleteColumn where() {
        SQLString.append(" WHERE ");
        return this;
    }

    public DeleteColumn and() {
        SQLString.append(" AND ");
        return this;
    }

    public DeleteColumn or() {
        SQLString.append(" OR ");
        return this;
    }

    public DeleteColumn like(String s) {
        SQLString.append("LIKE ")
                .append("\"")
                .append(s)
                .append("\"");
        return this;
    }

    public DeleteColumn fieldString(String value) {
        SQLString.append("\"")
                .append(value)
                .append("\"");
        return this;
    }

    public DeleteColumn fieldInt(int value) {
        SQLString.append(value);
        return this;
    }

    public DeleteColumn fieldLong(long value) {
        SQLString.append(value);
        return this;
    }

    public DeleteColumn fieldFloat(float value) {
        SQLString.append(value);
        return this;
    }

    public DeleteColumn fieldBoolean(boolean value) {
        SQLString.append(value);
        return this;
    }

    public DeleteColumn column(String field) {
        SQLString.append(field);
        return this;
    }

    public DeleteColumn writeSQL(String sql) {
        SQLString.append(sql);
        return this;
    }

    public boolean execute() {
        SQLiteDatabase write = helperBD.getWritableDatabase();
        try {
            write.execSQL(SQLString.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}