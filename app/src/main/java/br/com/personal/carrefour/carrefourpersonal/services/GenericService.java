package br.com.personal.carrefour.carrefourpersonal.services;

import br.com.personal.carrefour.carrefourpersonal.model.UsuarioLogin;
import br.com.personal.carrefour.carrefourpersonal.response.ArquivoByte;
import br.com.personal.carrefour.carrefourpersonal.response.ResponseLogin;
import br.com.personal.carrefour.carrefourpersonal.response.ListaNomesArquivosObjects;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ASUS on 23/02/2018.
 */
public interface GenericService {

    @POST("login")
    Call<ResponseLogin> loginAutenticacao(@Body UsuarioLogin usuarioLogin);

    @GET("listaArquivosObject")
    Call<ListaNomesArquivosObjects> listaNomesArquivosObject(@Header("token") Long idSessao);

    @POST("sendFiles")
    Call<ArquivoByte> enviarNomeArquivo(@Header("token") Long idSessao, @Body ArquivoByte file);
}
