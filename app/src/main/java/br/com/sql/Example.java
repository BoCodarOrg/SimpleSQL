package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simplesql.config.SimpleSQL;

import java.util.List;

public class Example extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        SimpleSQL simpleSQL = new SimpleSQL(new HelperBD(this));
        Pessoa pessoa = new Pessoa();
        pessoa.setName("paulo");
        pessoa.setEmail("pauloury1@gmail.com");
        pessoa.setPhone("12317820");
        boolean result = simpleSQL.insert(pessoa).execute();

        List<Pessoa> list = simpleSQL.selectTable(new Pessoa())
                .fields("*")
                .execute();

       simpleSQL.updateTable(new Pessoa())
                .set("name","email")
                .values("Novo Nome","Novo Email")
                .where()
                .column("id")
                .equals()
                .fieldInt(1)
                .execute();

        list.clear();
    }
}
