package com.simplesql.simplesql.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.simplesql.simplesql.annotations.AutoIncrement;
import com.simplesql.simplesql.annotations.Column;
import com.simplesql.simplesql.annotations.Key;
import com.simplesql.simplesql.annotations.Table;
import com.simplesql.simplesql.annotations.Unique;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static com.simplesql.simplesql.config.SimpleSQL.helperBD;

/**
 * @author Paulo Iury
 * Method INSERT
 */
public class Insert {
    private Object obj;
    private ContentValues values;
    private SQLiteDatabase write;
    private SQLiteDatabase read;
    private String table;

    public Insert(Object obj) {
        this.obj = obj;
        table = obj.getClass().getSimpleName();
        values = new ContentValues();
        write = helperBD.getWritableDatabase();
        read = helperBD.getReadableDatabase();
    }

    public boolean execute() {
        try {
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
                        if (field.get(obj) == null && column.non_null())
                            throw new SQLException("This " + field.getName() + " is not null but is empty");
                    } else
                        throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");
                }
                long result = write.insert(table, null, values);
                return result > -1;
            } else
                throw new SQLException("This class does not have the table annotation");
        } catch (SQLException e) {
            Log.e("Insert", e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Log.e("Insert", e.getMessage());
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

       /* private void getCount(String f) {
            String sql = "SELECT IFNULL(MAX(" + f + "),0) + 1 AS id  FROM " + table + ";";
            Cursor cursor = read.rawQuery(sql, null);
            if (cursor.moveToFirst())
                values.put(f, cursor.getInt(cursor.getColumnIndex("id")));
            else
                values.put(f, 1);
        }*/


}
