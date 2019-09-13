package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

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
        p.setNome("\"Jiselle\"");
        p.setId(1);
        p.setIdade(11);
        SQLiteDatabase write = new HelperBD(this).getWritableDatabase();

        try {
            write.execSQL(SQL.insert(p));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        SampleSQL sampleSql = new SampleSQL(this);
        List<Pessoa> pessoa2 = sampleSql.selectTable(new Pessoa())
                .fields(new String[]{"nome", "idade"})
                .execute();

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
}
