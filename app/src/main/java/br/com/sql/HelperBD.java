package br.com.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.simplesql.simplesql.config.SimpleSQL;

import java.sql.SQLException;

/**
 * Created by Lucas Nascimento on 18/09/2019
 * Copyright (c) 2019 P2J
 */
public class HelperBD extends SQLiteOpenHelper {
    private static final String NAME = "nome_banco.bd";
    private static final int VERSION = 13;
    private SimpleSQL simpleSQL;

    public HelperBD(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        simpleSQL = new SimpleSQL(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String _return = simpleSQL.create(new Pessoa(), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String _retorn = simpleSQL.deleteTable(new Pessoa(), db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

}
