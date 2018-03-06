package br.com.personal.carrefour.carrefourpersonal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.Serializable;

import br.com.personal.carrefour.carrefourpersonal.dao.UsuarioDAO;
import br.com.personal.carrefour.carrefourpersonal.helper.UsuarioLoginHelper;
import br.com.personal.carrefour.carrefourpersonal.model.UsuarioLogin;
import br.com.personal.carrefour.carrefourpersonal.response.ResponseLogin;
import br.com.personal.carrefour.carrefourpersonal.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String PREF_NAME = "LoginActivityPreferences";
    private UsuarioLoginHelper helper;
    private CheckBox saveLogin;
    private UsuarioLogin usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setIcon(R.drawable.actionbar_carrefour);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        helper = new UsuarioLoginHelper(this);

        //helper.limpaCampos();
    }

    public void signIn(View view) {

        usuarioLogin = helper.pegaLogin();

        if(usuarioLogin.getNome().equals("") || usuarioLogin.getSenha().equals("") ) {
            Snackbar.make(findViewById(R.id.activity_login),"Preencha os campos",Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.button_message_network), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }else{

            final ProgressDialog dialog = iniciarProgressDialog();

            Call<ResponseLogin> call = new RetrofitInicializador().getGenericService().loginAutenticacao(usuarioLogin);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    dialog.dismiss();

                    String msg;

                    int code = response.raw().code();

                    if(code == 200){

                        ResponseLogin responseLogin = response.body();
                        msg= response.body().getDs_msg().toString();

                        saveLogin = (CheckBox) findViewById(R.id.saveLogin);
                        // VERIFY CHECKBOX FOR SHAREDPREFERENCES
                        if(saveLogin.isChecked()){
                            SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("login", usuarioLogin.getNome());
                            editor.putString("password", usuarioLogin.getSenha());
                            editor.commit();
                        }

                        UsuarioDAO dao = new UsuarioDAO(LoginActivity.this);
                        dao.sincronizaIdSessao(responseLogin);

                        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("nomeLogin", (Serializable) helper.pegaLogin().getNome().toString());
                        startActivity(intent);
                        finish();

                        Log.i("RESPOSTA OK ===== ", responseLogin.toString());
                    }else{
                        msg = "Usuário não existe ou senha incorreta";
                        Snackbar.make(findViewById(R.id.activity_login),msg,Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.button_message_network), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    dialog.dismiss();
                    Snackbar.make(findViewById(R.id.activity_login),"Problema de Conexão",Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.button_message_network), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                    Log.e("RESPOSTA====", t.getMessage());
                }
            });}
    }

    @NonNull
    private ProgressDialog iniciarProgressDialog() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Entrando...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
