package com.simplesql.crud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.simplesql.config.SimpleSQL.getString;
import static com.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Lucas Nascimento
 * Method SELECT
 */
public class Select {
    private String tableName, field, writeSQL, column, table, columnFunction;
    private String[] fields;
    private boolean where, equals, between, or, on, and, like, innerJoin, leftJoin, rightJoin, fullJoin, functionParameter;
    private Object value;
    private String SQLString;
    private Object typeObject;
    private static final String KEY_FUNCTION_PARAMETER = "%column";
    private static final String KEY_COLIMN_NAME_MAX = "value_function";
    private String type;

    /**
     * @param typeObject
     * @param type
     */
    public Select(Object typeObject, String type) {
        this.tableName = typeObject.getClass().getSimpleName();
        this.typeObject = typeObject;
        switch (type) {
            case "COUNT":
                SQLString = "SELECT COUNT(" + KEY_FUNCTION_PARAMETER + ")";
                this.functionParameter = true;
                break;
            case "SINGLE":
                SQLString = "SELECT SINGLE ";
                this.functionParameter = true;
                break;
            case "MAX":
                SQLString = "SELECT MAX(" + KEY_FUNCTION_PARAMETER + ") INTO " + KEY_COLIMN_NAME_MAX;
                this.functionParameter = true;
                break;
            case "MIN":
                SQLString = "SELECT MIN(" + KEY_FUNCTION_PARAMETER + ")";
                this.functionParameter = true;
                break;
            default:
                SQLString = "SELECT ";
                break;
        }
        this.type = type;
    }


    public Select column(String column) {
        this.column = column;
        SQLString = SQLString + column;
        return this;
    }

    public Select limit(int number) {
        SQLString = SQLString + " LIMIT " + number;
        return this;
    }

    public Select fields(String[] fields) {
        this.fields = fields;
        SQLString = SQLString + getFields(fields) + " FROM " + tableName;
        return this;
    }

    public Select fieldString(String value) {
        this.field = field;
        SQLString = SQLString + "'" + value + "'";
        return this;
    }

    public Select fieldInt(int value) {
        this.value = value;
        SQLString = SQLString + value;
        return this;
    }

    public Select fieldLong(long value) {
        this.value = value;
        SQLString = SQLString + value;
        return this;
    }

    public Select fieldFloat(float value) {
        this.value = value;
        SQLString = SQLString + value;

        return this;
    }

    public Select fieldBoolean(boolean value) {
        this.value = value;
        SQLString = SQLString + value;

        return this;
    }

    public Select writeSQL(String operator) {
        this.writeSQL = operator;
        SQLString = SQLString + " " + operator + " ";
        return this;
    }

    public Select table(String table) {
        this.table = table;
        SQLString = SQLString + " " + table + " ";
        return this;
    }


    public Select equals() {
        this.equals = true;
        SQLString = SQLString + " = ";
        return this;
    }

    public Select where() {
        this.where = true;
        SQLString = SQLString + " WHERE ";
        return this;
    }

    public Select between() {
        this.between = true;
        SQLString = SQLString + " BETWEEN ";
        return this;
    }

    public Select and() {
        this.and = true;
        SQLString = SQLString + " AND ";
        return this;
    }

    public Select or() {
        this.or = true;
        SQLString = SQLString + " OR ";
        return this;
    }

    public Select innerJoin() {
        this.innerJoin = true;
        SQLString = SQLString + " INNER JOIN ";
        return this;
    }

    public Select leftJoin() {
        this.leftJoin = true;
        SQLString = SQLString + " LEFT JOIN ";
        return this;
    }

    public Select rigthJoin() {
        this.rightJoin = true;
        SQLString = SQLString + " RIGHT JOIN ";
        return this;
    }

    public Select fullJoin() {
        this.innerJoin = true;
        SQLString = SQLString + " FULL JOIN ";
        return this;
    }

    public Select on() {
        this.on = true;
        SQLString = SQLString + " ON ";
        return this;
    }

    public Select like() {
        this.like = true;
        SQLString = SQLString + " LIKE ";
        return this;
    }

    public Object execute() {
        SQLiteDatabase read = helperBD.getReadableDatabase();
        if (functionParameter) {
            if (fields[0] == null || fields[0].equals(""))
                columnFunction = "*";
            SQLString = SQLString.replace(fields[0], "").replace(KEY_FUNCTION_PARAMETER, (CharSequence) getString(fields[0]));
        }
        SQLString = SQLString + ";";
        List lstClasses = new ArrayList<>();
        Field[] fields = typeObject.getClass().getDeclaredFields();
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            Cursor cursor = read.rawQuery(SQLString, null);
            if (type.equals("COUNT")) {
                cursor.moveToFirst();
                return cursor.getInt(0);
            } else if (type.equals("MAX") || type.equals("MIN")) {
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        for (Field f : fields) {
                            Object object = checkItem(f, cursor);
                            if (object != null)
                                hashMap.put(f.getName(), object);
                        }
                        String hashJson = new Gson().toJson(hashMap);
                        lstClasses.add(new Gson().fromJson(hashJson, (Type) typeObject.getClass()));
                    }
                }
                cursor.close();
                return lstClasses;
            }
            while (cursor.moveToNext()) {
                for (Field f : fields) {
                    Object object = checkItem(f, cursor);
                    if (object != null)
                        hashMap.put(f.getName(), object);
                }
                String hashJson = new Gson().toJson(hashMap);
                lstClasses.add(new Gson().fromJson(hashJson, (Type) typeObject.getClass()));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }


        return lstClasses;
    }

    /**
     * @param field
     * @param cursor
     * @return object
     */
    private Object checkItem(Field field, Cursor cursor) {
        String name = field.getName();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (field.getType() == String.class)
                    return cursor.getString(cursor.getColumnIndex(name));
                else if (field.getType() == long.class)
                    return cursor.getLong(cursor.getColumnIndex(name));
                else if (field.getType() == float.class)
                    return cursor.getFloat(cursor.getColumnIndex(name));
                else if (field.getType() == byte[].class)
                    return cursor.getBlob(cursor.getColumnIndex(name));
                else if (field.getType() == int.class)
                    return cursor.getInt(cursor.getColumnIndex(name));
                else if (field.getType() == short.class)
                    return cursor.getShort(cursor.getColumnIndex(name));

                switch (cursor.getType(cursor.getColumnIndex(name))) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        return cursor.getInt(cursor.getColumnIndex(name));
                    case Cursor.FIELD_TYPE_FLOAT:
                        return cursor.getFloat(cursor.getColumnIndex(name));
                    case Cursor.FIELD_TYPE_STRING:
                        return cursor.getString(cursor.getColumnIndex(name));
                    case Cursor.FIELD_TYPE_BLOB:
                        return cursor.getBlob(cursor.getColumnIndex(name));
                    default:
                        return null;
                }
            } else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getFields(String[] args) {
        return Arrays.toString(args).replace("[", "").replace("]", "");
    }
}