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
        Pessoa pessoa = new Pessoa();
        pessoa.setName("paulo");
        try {
            simpleSQL.insert(pessoa);
            List<Pessoa> list = simpleSQL.selectTable(new Pessoa())
                    .fields(new String[]{"*"})
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
