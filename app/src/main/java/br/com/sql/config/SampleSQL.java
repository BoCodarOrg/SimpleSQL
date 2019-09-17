package br.com.sql.config;

import android.content.ContentValues;
import android.content.Context;
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

import br.com.sql.annotations.AutoIncrement;
import br.com.sql.annotations.Column;
import br.com.sql.annotations.ForeignKey;
import br.com.sql.annotations.Key;
import br.com.sql.annotations.Table;

/**
 * Developed by Lucas Nascimento
 */
public class SampleSQL {
    private static SQLiteOpenHelper helperBD;

    public SampleSQL(SQLiteOpenHelper helperBD) {
        this.helperBD = helperBD;
    }

    public Select selectTable(Object typeObject) {
        return new Select(typeObject);
    }

    public DeleteColumn deleteColumn(Object o) {
        return new DeleteColumn(o);
    }


    /**
     * Developed by Lucas Nascimento
     * Method SELECT
     */
    public class Select {
        private String tableName, field, writeSQL, collumn;
        private String[] fields;
        private boolean where, equals, between, or, and, like;
        private Object value;
        private String SQLString;
        private Object typeObject;


        public Select(Object typeObject) {
            this.tableName = typeObject.getClass().getSimpleName();
            this.typeObject = typeObject;
            SQLString = "SELECT ";
        }


        public Select collumn(String collumn) {
            this.collumn = collumn;
            SQLString = SQLString + collumn;
            return this;
        }

        public SampleSQL.Select fields(String[] fields) {
            this.fields = fields;
            SQLString = SQLString + getFields(fields) + " FROM " + tableName;
            return this;
        }

        public Select fieldString(String value) {
            this.field = field;
            SQLString = SQLString + value;
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

        public Select fieldFloat(int value) {
            this.value = value;
            SQLString = SQLString + value;

            return this;
        }

        public Select fieldBoolean(int value) {
            this.value = value;
            SQLString = SQLString + value;

            return this;
        }

        public Select writeSQL(String operator) {
            this.writeSQL = operator;
            SQLString = SQLString + " " + operator + " ";
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

        public Select like() {
            this.like = true;
            SQLString = SQLString + " LIKE ";
            return this;
        }

        public List execute() {
            SQLiteDatabase read = helperBD.getReadableDatabase();
            SQLString = SQLString + ";";
            List lstClasses = new ArrayList<>();
            Field[] fields = typeObject.getClass().getDeclaredFields();
            HashMap<String, Object> hashMap = new HashMap<>();

            try {
                Cursor cursor = read.rawQuery(SQLString, null);
                while (cursor.moveToNext()) {
                    for (Field f : fields) {
                        Object object = checkItem(cursor, f.getName());
                        hashMap.put(f.getName(), object);
                    }
                    String hashJson = new Gson().toJson(hashMap);
                    lstClasses.add(new Gson().fromJson(hashJson, (Type) typeObject.getClass()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * Busca no banco de dados
             * */

            return lstClasses;
        }

        private Object checkItem(Cursor cursor, String name) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_INTEGER) {
                    return cursor.getInt(cursor.getColumnIndex(name));
                } else if (cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_FLOAT) {
                    return cursor.getFloat(cursor.getColumnIndex(name));
                } else if (cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_STRING) {
                    return cursor.getString(cursor.getColumnIndex(name));
                } else if (cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_BLOB) {
                    return cursor.getBlob(cursor.getColumnIndex(name));
                } else if (cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_NULL) {
                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static String getFields(String[] args) {
        return Arrays.toString(args).replace("[", "").replace("]", "");
    }

    /**
     * Developed by Paulo Iury
     * Method CREATE TABLE
     */
    public static String create(Object obj) throws SQLException {
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
                        " REFERENCES " + foreignKey.reference().getSimpleName() + "(" + key.getName() + ")";
            }
            sql += ");";
            return sql;
        } else
            throw new SQLException("This class does not have the table annotation");
    }
    /**
     * Developed by Paulo Iury
     * Method INSERT
     */
    public static boolean insert(Object obj) throws Throwable {
        SQLiteDatabase write = helperBD.getReadableDatabase();
        ContentValues values = new ContentValues();
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        if (persistable != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                // como os atributos são private,
                // setamos ele como visible
                field.setAccessible(true);
                // Se o atributo tem a anotação
                if (field.isAnnotationPresent(Column.class)) {
                    if (!field.isAnnotationPresent(AutoIncrement.class)) {
                        String value = field.get(obj).toString();
                        values.put(field.getName(), value);
                    }
                } else
                    throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");
            }
            long result = write.insert(obj.getClass().getSimpleName(), null, values);
            return result != 1;
        } else
            throw new SQLException("This class does not have the table annotation");
    }
    private static String checkAnnotations(Field c, boolean not_null) {
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
    public static String deleteTable(Object obj) throws SQLException {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        if (persistable != null) {
            return "DROP TABLE IF EXISTS " + obj.getClass().getSimpleName();
        } else
            throw new SQLException("This class does not have the table annotation");

    }

    /**
     * Developed by Paulo Iury
     * Method DELETE COLUMN
     */
    public class DeleteColumn {
        private String table, SQLString;

        public DeleteColumn(Object o) {
            this.table = o.getClass().getSimpleName();
            this.SQLString = "";
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

        public DeleteColumn like(String s){
            SQLString += "LIKE " + "\"s\"";
            return this;
        }

        public DeleteColumn fieldString(String value) {
            SQLString += value;
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

        public DeleteColumn fieldFloat(int value) {
            SQLString += value;
            return this;
        }

        public DeleteColumn fieldBoolean(int value) {
            SQLString += value;
            return this;
        }

        public DeleteColumn field(String field) {
            SQLString += field;
            return this;
        }

        public DeleteColumn writeSQL(String sql){
            SQLString += sql;
            return this;
        }

        public boolean execute() {
            SQLiteDatabase escrever = helperBD.getWritableDatabase();
            try {
                escrever.execSQL("DELETE FROM " + table + " " + SQLString);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
