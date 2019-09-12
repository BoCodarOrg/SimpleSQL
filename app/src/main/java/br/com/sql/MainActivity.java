package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import br.com.sql.config.SQL;
import br.com.sql.tables.Pessoa;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pessoa pessoa = new Pessoa();
        pessoa.setId(0);
        pessoa.setNome("Paulo Iury");
        pessoa.setIdade(18);
        String sql = SQL.create(pessoa);

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
}
