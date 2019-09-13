package br.com.sql.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import br.com.sql.tables.Pessoa;

public class Helper extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "example.db";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SampleSQL.create(Pessoa.class));
            Log.i("create","Deu certo");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("create","Deu errado");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(SampleSQL.deleteTable(Pessoa.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}