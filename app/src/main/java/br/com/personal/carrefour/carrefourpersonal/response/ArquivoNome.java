package br.com.personal.carrefour.carrefourpersonal.response;

/**
 * Created by ASUS on 25/02/2018.
 */
public class ArquivoNome {

    private String nome;

    public ArquivoNome(){

    }

    public ArquivoNome(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
