package br.com.personal.carrefour.carrefourpersonal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import br.com.personal.carrefour.carrefourpersonal.response.ResponseLogin;

/**
 * Created by ASUS on 23/02/2018.
 */

public class UsuarioDAO {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public UsuarioDAO(Context context){
        banco = new CriaBanco(context);
    }

    @NonNull
    private ContentValues pegaDadosConjunto(ResponseLogin responseLogin) {
        ContentValues dados = new ContentValues();
        dados.put("idSessao",responseLogin.getIdSessao());
        return dados;
    }

    public void insereIdSessao(ResponseLogin responseLogin) {
        db = banco.getWritableDatabase();
        ContentValues dados = pegaDadosConjunto(responseLogin);
        db.insert("Usuario", null, dados);
        db.close();
    }

    private void alteraIdSessao(ResponseLogin responseLogin) {
        db = banco.getWritableDatabase();
        Long data = Long.valueOf(1);
        ContentValues dados = pegaDadosConjunto(responseLogin);
        String[] params = {data.toString()};
        db.update("Usuario", dados, "id = ?",params);
        db.close();
    }

    public ResponseLogin buscaIdSessao() {
        String sql = "SELECT * FROM Usuario";
        db = banco.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();

        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setIdSessao(c.getLong(c.getColumnIndex("idSessao")));

        c.close();
        db.close();

        return responseLogin;
    }

    public void sincronizaIdSessao(ResponseLogin responseLogin) {
        if (existe(responseLogin)){
            alteraIdSessao(responseLogin);
        }else{
            insereIdSessao(responseLogin);
        }

        db.close();
    }

    private boolean existe(ResponseLogin responseLogin) {
        db = banco.getReadableDatabase();
        String existe = "SELECT id FROM Usuario WHERE id=? LIMIT 1";
        Cursor c = db.rawQuery(existe, new String[]{String.valueOf(1)});
        int quantidade = c.getCount();

        db.close();
        return quantidade > 0;
    }
}
