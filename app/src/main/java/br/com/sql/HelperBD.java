package br.com.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.simplesql.config.SimpleSQL;

import java.sql.SQLException;

/**
 * Created by Lucas Nascimento on 18/09/2019
 * Copyright (c) 2019 P2J
 */
public class HelperBD extends SQLiteOpenHelper {
    private static final String NAME = "nome_banco.bd";
    private static final int VERSION = 1;
    private SimpleSQL simpleSQL;

    public HelperBD(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        simpleSQL = new SimpleSQL(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            simpleSQL.create(new Pessoa(), db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            simpleSQL.deleteTable(new Pessoa(), db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

}
