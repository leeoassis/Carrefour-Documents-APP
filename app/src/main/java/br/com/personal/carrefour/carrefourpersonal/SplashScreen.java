package br.com.personal.carrefour.carrefourpersonal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.Serializable;

import br.com.personal.carrefour.carrefourpersonal.model.UsuarioLogin;
import br.com.personal.carrefour.carrefourpersonal.response.ResponseLogin;
import br.com.personal.carrefour.carrefourpersonal.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static final String PREF_NAME = "LoginActivityPreferences";
    private UsuarioLogin usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startAnimations();
    }

    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        if (l != null) {
            l.clearAnimation();
            l.startAnimation(anim);
        }


        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }

        int SPLASH_DISPLAY_LENGTH = 4500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // VERIFY SHAREDPREFERENCES
                SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String login = sp.getString("login", "");
                String password = sp.getString("password", "");

                usuarioLogin = new UsuarioLogin();

                usuarioLogin.setNome(login);
                usuarioLogin.setSenha(password);

                Call<ResponseLogin> call = new RetrofitInicializador().getGenericService().loginAutenticacao(usuarioLogin);
                call.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                        if (response.code() == 200){

                            ResponseLogin responseLogin = response.body();
                            String msg = response.body().getDs_msg().toString();

                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            intent.putExtra("nomeLogin", (Serializable) msg.substring(msg.indexOf(",") + 2 ,msg.length()));
                            intent.putExtra("emailLogin",(Serializable) usuarioLogin.getNome().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            SplashScreen.this.finish();

                            Log.i("RESPOSTA OK ===== ", responseLogin.toString());

                        }else{


                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            SplashScreen.this.finish();

                            Log.i("RESPOSTA FALHA ===== ", "ERROR 400");
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }
                });
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
