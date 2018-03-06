package br.com.personal.carrefour.carrefourpersonal.helper;

import android.widget.EditText;

import br.com.personal.carrefour.carrefourpersonal.LoginActivity;
import br.com.personal.carrefour.carrefourpersonal.R;
import br.com.personal.carrefour.carrefourpersonal.model.UsuarioLogin;

/**
 * Created by ASUS on 23/02/2018.
 */

public class UsuarioLoginHelper {

    private final EditText campoUsuario;
    private final EditText campoSenha;
    private UsuarioLogin usuario;

    public UsuarioLoginHelper(LoginActivity activity){
        campoUsuario = (EditText)activity.findViewById(R.id.campo_login);
        campoSenha = (EditText)activity.findViewById(R.id.campo_senha);
        usuario = new UsuarioLogin();
    }

    public UsuarioLogin pegaLogin(){

        usuario.setNome(campoUsuario.getText().toString());
        usuario.setSenha(campoSenha.getText().toString());

        return usuario;
    }

    public void limpaCampos() {
        campoUsuario.setText("");
        campoSenha.setText("");
    }
}

