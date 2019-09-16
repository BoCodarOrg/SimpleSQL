package br.com.sql.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import br.com.sql.tables.Pessoa;

/**
 * Created by Lucas Nascimento on 13/09/2019
 * Copyright (c) 2019 GFX Consultoria
 */
public class HelperBD extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 5;
    static final String DATABASE_NAME = "example.db";
    Context context;

    public HelperBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SampleSQL.create(new Pessoa()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        SQL.deleleTable(Pessoa.class);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}
