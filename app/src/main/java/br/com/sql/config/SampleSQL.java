package br.com.sql.config;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.sql.annotations.AutoIncrement;
import br.com.sql.annotations.Column;
import br.com.sql.annotations.ForeignKey;
import br.com.sql.annotations.Key;
import br.com.sql.annotations.Table;
import br.com.sql.tables.Pessoa;

/**
 * Developed by Lucas Nascimento
 */
public class SampleSQL {
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
            this.tableName = ((Class) typeObject).getSimpleName();
            this.typeObject = typeObject.getClass().getDeclaredFields();
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

        public <typeObject> List<typeObject> execute() {
            SQLString = SQLString + ";";
            /**
             * Busca no banco de dados
             * */
            List<typeObject> lstClasses = new ArrayList<>();
            lstClasses.add((typeObject) new Pessoa());

            return lstClasses;
        }

    }

    private static String getFields(String[] args) {
        return Arrays.toString(args).replace("[", "").replace("]", "");
    }

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

    public static String insert(Object obj) throws Throwable {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        String value = "";
        String columns = "";
        if (persistable != null) {
            String tabela = obj.getClass().getSimpleName();
            Field[] fields = obj.getClass().getDeclaredFields();
            int count = 0;
            for (Field field : fields) {
                // como os atributos são private,
                // setamos ele como visible
                field.setAccessible(true);
                // Se o atributo tem a anotação
                if (field.isAnnotationPresent(Column.class))
                    if (count == 0) {
                        columns += field.getName();
                        value += field.get(obj).toString();
                    } else {
                        columns += " , " + field.getName();
                        value += " , " + field.get(obj).toString();
                    }
                else
                    throw new SQLException("The " + field.getName() + "attribute did not have the column annotation");
                count++;
            }

            return "INSERT INTO " + tabela + " (" + columns + ") " +
                    " VALUES ( " + value + " );";
        } else
            throw new SQLException("This class does not have the table annotation");
    }


    private static String checkAnnotations(Field c, boolean not_null) {
        String annotations = "";
        if (c.isAnnotationPresent(AutoIncrement.class))
            annotations += " AUTOINCREMENT";
        if (c.isAnnotationPresent(Key.class))
            annotations += " PRIMARY KEY";
        if (not_null)
            annotations += " NOT_NULL";

        return annotations;
    }

    public static String deleteTable(Object obj) throws SQLException {
        Table persistable =
                obj.getClass().getAnnotation(Table.class);
        String columns = "";
        if (persistable != null) {
            return "DROP TABLE IF EXISTS " + obj.getClass().getSimpleName();
        } else
            throw new SQLException("This class does not have the table annotation");

    }
}
