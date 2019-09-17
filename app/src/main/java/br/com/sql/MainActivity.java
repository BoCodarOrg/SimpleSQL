package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.sql.config.HelperBD;
import br.com.sql.config.SQL;
import br.com.sql.config.SampleSQL;
import br.com.sql.tables.Pessoa;

public class MainActivity extends AppCompatActivity {
    private EditText etNome,etIdade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNome = findViewById(R.id.etNome);
        etIdade = findViewById(R.id.etIdade);
        SampleSQL sampleSql = new SampleSQL(this);
        List<Pessoa> lstPessoa = sampleSql.selectTable(new Pessoa())
                .fields(new String[]{"nome", "idade"})
                .execute();

        sampleSql.updateTable(new Pessoa())
                .set(new String[]{"nome,idade"})
                .values(new String[]{"Lucas","10"})
                .where()
                .collumn("id")
                .equals()
                .fieldInt(1);

        for(Pessoa p:lstPessoa){
            Log.i("select",p.getNome()+" - "+p.getIdade());
        }
    }


    public void onRegister(View view){
        Pessoa p = new Pessoa();
        p.setNome("\""+etNome.getText().toString()+"\"");
        p.setIdade(Integer.parseInt(etIdade.getText().toString()));
        SQLiteDatabase write = new HelperBD(this).getWritableDatabase();

        try {
            write.execSQL(SQL.insert(p));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        SampleSQL sampleSql = new SampleSQL(this);
        List<Pessoa> lstPessoa = sampleSql.selectTable(new Pessoa())
                .fields(new String[]{"nome", "idade"})
                .execute();

        for(Pessoa pe:lstPessoa){
            Log.i("select",pe.getNome()+" - "+pe.getIdade());
        }
    }
}
