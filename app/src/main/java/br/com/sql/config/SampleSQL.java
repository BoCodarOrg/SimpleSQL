package br.com.sql.config;

import android.content.Context;
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

import br.com.sql.tables.Pessoa;

/**
 * Developed by Lucas Nascimento
 */
public class SampleSQL {
    HelperBD helperBD;

    public SampleSQL(Context context){
        helperBD = new HelperBD(context);
    }

    public Select selectTable(Object typeObject) {
        return new Select(typeObject);
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
            SQLString = SQLString + field;
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
            List  lstClasses = new ArrayList<>();
            Field[] fields = typeObject.getClass().getDeclaredFields();
            HashMap<String,Object> hashMap = new HashMap<>();

            try {
                Cursor cursor = read.rawQuery(SQLString, null);
                while (cursor.moveToNext()) {
                    for(Field f:fields){
                        Object object = checkItem(cursor,f.getName());
                        hashMap.put(f.getName(),object);
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

        private Object checkItem(Cursor cursor,String name){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if(cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_INTEGER ){
                    return cursor.getInt(cursor.getColumnIndex(name));
                }else if(cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_FLOAT){
                    return cursor.getFloat(cursor.getColumnIndex(name));
                }else if(cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_STRING){
                    return cursor.getString(cursor.getColumnIndex(name));
                }else if(cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_BLOB){
                    return cursor.getBlob(cursor.getColumnIndex(name));
                }else if(cursor.getType(cursor.getColumnIndex(name)) == Cursor.FIELD_TYPE_NULL){
                    return null;
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }
    }

    private static String getFields(String[] args) {
        return Arrays.toString(args).replace("[", "").replace("]", "");
    }


}
