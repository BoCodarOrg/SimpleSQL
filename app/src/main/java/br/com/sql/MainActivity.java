package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Field;

import br.com.sql.annotations.AutoIncrement;
import br.com.sql.annotations.Column;
import br.com.sql.annotations.Key;
import br.com.sql.annotations.Table;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pessoa pessoa = new Pessoa();
        pessoa.setId(0);
        pessoa.setNome("Paulo Iury");
        pessoa.setIdade(18);
        String sql = "";

        try {
            sql = SQL.create(pessoa);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        new SampleSql.Select()
                .table("Pessoa")
                .fields(new String[]{"*"})
                .where()
                .fieldString("id")
                .equals()
                .fieldInt(1)
                .execute();

        SampleSql.mountFields(new String[]{"1","2","3"});

        int size = pessoa.getClass().getDeclaredFields().length;
        Toast.makeText(this, sql, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(size), Toast.LENGTH_LONG).show();
    }


    public static class SQL {

        public static String create(Object obj) throws Throwable {
            Table persistable =
                    obj.getClass().getAnnotation(Table.class);
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
                    Column column =
                            field.getAnnotation(Column.class);
                    if (column != null) {
                        columns += checkAnnotations(field,column.non_null());
                        if (count == 0) {
                            columns += field.getName();
                        } else {
                            columns += " , " + field.getName();
                        }
                    }
                    count++;
                }

                return "CREATE TABLE " + tabela + "("
                        + columns +
                        ");";
            }
            return "";
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
                    if (field.isAnnotationPresent(Column.class)) {
                        if (count == 0) {
                            columns += field.getName();
                            value += field.get(obj).toString();
                        } else {
                            columns += " , " + field.getName();
                            value += " , " + field.get(obj).toString();
                        }
                    }
                    count++;
                }

                return "INSERT INTO " + tabela + " (" + columns + ") " +
                        " VALUES ( " + value + " );";
            }
            return "";
        }

        private static String checkAnnotations(Field c,boolean not_null) {
            String annotations = "";
            if (c.isAnnotationPresent(AutoIncrement.class))
                annotations += "AUTO_INCREMENT";
            if (c.isAnnotationPresent(Key.class))
                annotations += "PRIMARY KEY";
            if (not_null)
                annotations += "NOT_NULL";
            return annotations;
        }
    }
}
