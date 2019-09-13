package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import br.com.sql.config.SampleSQL;
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
        SampleSQL sampleSql = new SampleSQL();

           List<Pessoa> pessoaa =  sampleSql.selectTable(Pessoa.class)
                    .fields(new String[]{"nome","idade"})
                    .execute();



        int size = pessoa.getClass().getDeclaredFields().length;
        Toast.makeText(this, sql, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(size), Toast.LENGTH_LONG).show();
    }
}
