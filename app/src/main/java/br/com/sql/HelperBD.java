package br.com.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.simplesql.simplesql.config.SimpleSQL;

import java.sql.SQLException;

/**
 * Created by Lucas Nascimento on 18/09/2019
 * Copyright (c) 2019 GFX Consultoria
 */
public class HelperBD extends SQLiteOpenHelper {
    private static final String NAME = "nome_banco.bd";
    private static final int VERSION = 1;
    public HelperBD(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SimpleSQL simpleSQL = new SimpleSQL(this);
        try {
            simpleSQL.create(new Pessoa());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SimpleSQL simpleSQL = new SimpleSQL(this);
        try {
            simpleSQL.deleteTable(new Pessoa());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
