package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simplesql.simplesql.config.SimpleSQL;

import java.util.List;

public class Example extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        SimpleSQL simpleSQL = new SimpleSQL(new HelperBD(this));
        Pessoa pessoa = new Pessoa();
        pessoa.setName("paulo");
        pessoa.setEmail("pauloiury1@gmail.com");
        pessoa.setPhone("1231789201");
        boolean result = simpleSQL.insert(pessoa).execute();

        List<Pessoa> list = (List<Pessoa>) simpleSQL.selectTable(new Pessoa())
                .fields(new String[]{"*"})
                .execute();
    }
}
