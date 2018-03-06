package br.com.personal.carrefour.carrefourpersonal.retrofit;

import java.util.concurrent.TimeUnit;

import br.com.personal.carrefour.carrefourpersonal.services.GenericService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ASUS on 23/02/2018.
 */

public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        //http://192.168.0.108:8080/api/
        retrofit = new Retrofit.Builder().baseUrl("http://54.233.181.16:8080/api/").client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    public GenericService getGenericService(){
        return retrofit.create(GenericService.class);
    }
}
