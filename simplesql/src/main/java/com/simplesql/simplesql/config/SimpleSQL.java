package com.simplesql.simplesql.config;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.simplesql.simplesql.annotations.AutoIncrement;
import com.simplesql.simplesql.annotations.Column;
import com.simplesql.simplesql.annotations.ForeignKey;
import com.simplesql.simplesql.annotations.Key;
import com.simplesql.simplesql.annotations.Table;

public class SimpleSQL {
    private SQLiteOpenHelper helperBD;

    public SimpleSQL(SQLiteOpenHelper helperBD) {
        this.helperBD = helperBD;
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

    public DeleteColumn deleteColumn(Object objectType) {
        return new DeleteColumn(objectType);
    }


    public Update updateTable(Object typeObject) {
        return new Update(typeObject);
    }

    public Insert insert(Object o) {
        return new Insert(o);
    }

    /**
     * Developed by Lucas Nascimento
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
                    SQLString = "SELECT MAX(" + KEY_FUNCTION_PARAMETER + ")";
                    this.functionParameter = true;
                    break;
                case "MIN":
                    SQLString = "SELECT MIN(" + KEY_FUNCTION_PARAMETER + ")";
                    this.functionParameter = true;
                    break;
                default:
                    SQLString = "SELECT ";


            }
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

        public SimpleSQL.Select fields(String[] fields) {
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

        public List execute() {
            SQLiteDatabase read = helperBD.getReadableDatabase();
            if (functionParameter) {
                if (fields[0] == null || fields[0].equals(""))
                    columnFunction = "*";
                SQLString = SQLString.replace(KEY_FUNCTION_PARAMETER, fields[0]);
            }
            SQLString = SQLString + ";";
            List lstClasses = new ArrayList<>();
            Field[] fields = typeObject.getClass().getDeclaredFields();
            HashMap<String, Object> hashMap = new HashMap<>();
            try {
                Cursor cursor = read.rawQuery(SQLString, null);
                while (cursor.moveToNext()) {
                    for (Field f : fields) {
                        Object object = checkItem(f, cursor);
                        if (object != null)
                            hashMap.put(f.getName(), object);
                    }
                    String hashJson = new Gson().toJson(hashMap);
                    lstClasses.add(new Gson().fromJson(hashJson, (Type) typeObject.getClass()));
                }
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
    }

    /**
     * Developed by Lucas Nascimento
     * Method UPDATE
     */

    public class Update {
        private Object typeObject;
        private String tableName, field, writeSQL, column, table, stringSet;
        private String SQLString;
        private Object value;
        private String[] values, fields;

        /**
         * @param typeObject
         */
        public Update(Object typeObject) {
            this.tableName = typeObject.getClass().getSimpleName();
            this.typeObject = typeObject;
            SQLString = "UPDATE " + tableName;
        }

        /**
         * @param fields
         * @return
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

        public Update values(String[] values) {
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

        /**
         * @return
         */
        public boolean execute() {
            SQLiteDatabase write = helperBD.getReadableDatabase();
            int i = 0;
            for (String s : fields) {
                String replace = "%" + i;

                SQLString = SQLString.replace(replace, (CharSequence) getString(values[i]));
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

    public Object getString(String string) {
        String s = "A;B;C;D;E;F;G;H;I;J;K;L;M;N;O;P;Q;R;S;T;U;V;W;X;Y;Z;a;b;c;d;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z;\\;\\";
        String[] arrays = s.split(";");
        for (String array : arrays) {
            if (string.contains(array)) {
                return "\"" + string + "\"";
            }
        }
        return string;
    }

    private static String getFields(String[] args) {
        return Arrays.toString(args).replace("[", "").replace("]", "");
    }

    /**
     * Developed by Paulo Iury
     * Method CREATE TABLE
     */
    public String create(Object obj, SQLiteDatabase db) {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        String columns = "";
        try {
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
                        columns += checkAnnotations(field, column.non_null());
                        if (field.isAnnotationPresent(ForeignKey.class))
                            foreignKeys.add(field);
                    } else
                        throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");

                    count++;
                }
                String sql = "CREATE TABLE " + tabela + " ( "
                        + columns;
                for (Field key : foreignKeys) {
                    ForeignKey foreignKey = key.getAnnotation(ForeignKey.class);
                    sql += " , FOREIGN KEY (" + key.getName() + ")" +
                            " REFERENCES " + foreignKey.table().getSimpleName() + "(" + foreignKey.column() + ")";
                }
                sql += ");";
                db.execSQL(sql);
                return "Table create success";
            } else
                throw new SQLException("This class does not have the table annotation");
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Developed by Paulo Iury
     * Method INSERT
     */
    public class Insert {
        private Object obj;
        private ContentValues values;

        public Insert(Object obj) {
            this.obj = obj;
            values = new ContentValues();
        }

        public boolean execute() {

            try {
                SQLiteDatabase write = helperBD.getReadableDatabase();
                Table persistable =
                        obj.getClass().getAnnotation(Table.class);
                if (persistable != null) {
                    Field[] fields = obj.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        // como os atributos são private,
                        // setamos ele como visible
                        field.setAccessible(true);
                        // Se o atributo tem a anotação
                        Column column = field.getAnnotation(Column.class);
                        if (column != null) {
                            if (!field.isAnnotationPresent(AutoIncrement.class) && field.get(obj) != null)
                                checkObject(field, obj);
                        } else
                            throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");
                    }
                    long result = write.insert(obj.getClass().getSimpleName(), null, values);
                    return result > -1;
                } else
                    throw new SQLException("This class does not have the table annotation");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        private void checkObject(Field field, Object obj) {
            try {
                if (field.get(obj).getClass() == String.class)
                    values.put(field.getName(), (String) field.get(obj));
                else if (field.get(obj).getClass() == Long.class)
                    values.put(field.getName(), (long) field.get(obj));
                else if (field.get(obj).getClass() == Float.class)
                    values.put(field.getName(), (float) field.get(obj));
                else if (field.get(obj).getClass() == byte[].class)
                    values.put(field.getName(), (byte[]) field.get(obj));
                else if (field.get(obj).getClass() == Integer.class)
                    values.put(field.getName(), (int) field.get(obj));
                else if (field.get(obj).getClass() == Short.class)
                    values.put(field.getName(), (short) field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private String checkAnnotations(Field c, boolean not_null) {
        String annotations = "";
        if (c.isAnnotationPresent(Key.class))
            annotations += " PRIMARY KEY";
        if (c.isAnnotationPresent(AutoIncrement.class))
            annotations += " AUTOINCREMENT";
        if (not_null && !c.isAnnotationPresent(Key.class))
            annotations += " NOT NULL";

        return annotations;
    }

    /**
     * Developed by Paulo Iury
     * Method DELETE TABLE
     */
    public String deleteTable(Object obj, SQLiteDatabase db) {

        try {
            Table persistable =
                    obj.getClass().getAnnotation(Table.class);
            if (persistable != null) {
                db.execSQL("DROP TABLE IF EXISTS " + obj.getClass().getSimpleName());
                return "Table delete sucess";
            } else
                throw new SQLException("This class does not have the table annotation");
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Developed by Paulo Iury
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
}
