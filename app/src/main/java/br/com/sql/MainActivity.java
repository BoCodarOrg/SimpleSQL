package br.com.sql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sql.config.HelperBD;
import br.com.sql.config.SQL;
import br.com.sql.config.SampleSQL;
import br.com.sql.tables.Pessoa;

public class MainActivity extends AppCompatActivity {
    private EditText etNome,etIdade;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNome = findViewById(R.id.etNome);
        etIdade = findViewById(R.id.etIdade);
        recyclerView = findViewById(R.id.rvPessoas);


        SampleSQL sampleSql = new SampleSQL(this);
        List<Pessoa> lstPessoa = sampleSql.selectTable(new Pessoa())
                .fields(new String[]{"nome", "idade","id"})
                .execute();

        sampleSql.updateTable(new Pessoa())
                .set(new String[]{"nome","idade"})
                .values(new String[]{"Raimundo","12"})
        .execute();

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new PessoasAdapter(lstPessoa,this));
    }


    public void onRegister(View view){
        Pessoa p = new Pessoa();
        p.setNome(etNome.getText().toString());
        p.setIdade(Integer.parseInt(etIdade.getText().toString()));
        boolean result = false;
        try {
            result = new SampleSQL(this).insert(p);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        SampleSQL sampleSql = new SampleSQL(this);

        List<Pessoa> lstPessoa = sampleSql.selectTable(new Pessoa())
                .fields(new String[]{"nome", "idade","id"})
                .execute();


        for(Pessoa pe:lstPessoa){
            Log.i("select",pe.getNome()+" - "+pe.getIdade());
        }
    }
}
