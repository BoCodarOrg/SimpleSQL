package br.com.sql;

import java.util.Arrays;

import br.com.sql.annotations.Table;

public class SampleSql {

    static class Select {
        private String tableName, field;
        private String[] fields;
        private boolean where, equals;
        private Object value;

        private String SQLString;
        public Select select() {
            return this;
        }

        public Fields table(String name) {
            this.tableName = name;
            return new Fields(this);
        }


        public Select where() {
            this.where = true;
            return this;
        }

        public Select fieldString(String field) {
            this.field = field;
            return this;
        }

        public Select fieldInt(int value){
            this.value = value;
            return this;
        }

        public Select fieldLong(long value){
            this.value = value;
            return this;
        }

        public Select fieldFloat(int value){
            this.value = value;
            return this;
        }

        public Select fieldBoolean(int value){
            this.value = value;
            return this;
        }


        public Select equals() {
            this.equals = true;
            return this;
        }

        public boolean getWhere(){
            return this.where;
        }

        public boolean getEquals(){
            return this.equals;
        }

        public SampleSql execute() {
            this.SQLString = "SELECT "+getFields(fields)+" FROM "+tableName+" ";
            return new SampleSql(this);
        }
    }

    private static String getFields(String[] args){
        return Arrays.toString(args).replace ("[","").replace("]","");
    }



    public SampleSql(Select select) {

    }
}
