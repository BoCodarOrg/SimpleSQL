package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sql.config.HelperBD;
import br.com.sql.config.SQL;
import br.com.sql.config.SampleSQL;
import br.com.sql.tables.Pessoa;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pessoa p = new Pessoa();
        p.setNome("Alow");
        p.setIdade(12);
        boolean result = false;
        try {
            result = new SampleSQL(this).insert(p);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        SampleSQL sampleSql = new SampleSQL(this);
        List<Pessoa> pessoa2 = sampleSql.selectTable(new Pessoa())
                .where()
                .equals()
                .fields(new String[]{"*"})
                .execute();

        sampleSql.deleteColumn(new Pessoa())
                .where()
                .field("id")
                .equals()
                .fieldInt(1)
                .execute();
    }
}
