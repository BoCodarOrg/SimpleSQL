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
        int size = pessoa.getClass().getDeclaredFields().length;
        Toast.makeText(this, sql, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(size), Toast.LENGTH_LONG).show();
    }
}
