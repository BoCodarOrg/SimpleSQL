package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simplesql.simplesql.config.SimpleSQL;

import java.sql.SQLException;
import java.util.List;

public class Example extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        SimpleSQL simpleSQL = new SimpleSQL(new HelperBD(this));
//        Pessoa pessoa = new Pessoa();
//        pessoa.setName("paulo");
//        simpleSQL.insert(pessoa).execute();
//
        Categoria categoria = new Categoria();
        categoria.setIdCnpj(999999999);
        categoria.setNome("Teste");
        categoria.setImg(new byte[]{21,31,31});
        categoria.setValor(1f);
        categoria.setValue((short) 100);
        simpleSQL.insert(categoria).execute();


        List<Pessoa> list = simpleSQL.selectTable(new Pessoa())
                .fields(new String[]{"name"})
                .execute();

    }
}
