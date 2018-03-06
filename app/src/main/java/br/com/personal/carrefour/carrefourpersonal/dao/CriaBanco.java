package br.com.personal.carrefour.carrefourpersonal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 23/02/2018.
 */

public class CriaBanco extends SQLiteOpenHelper {

    private static final String sqlTabelaUsuario = "CREATE TABLE Usuario( id INTEGER PRIMARY KEY, idSessao INTEGER)";


    public CriaBanco(Context context) {
        super(context, "CarrefourDocuments", null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTabelaUsuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Usuario";
        db.execSQL(sql);
        onCreate(db);
    }
}
