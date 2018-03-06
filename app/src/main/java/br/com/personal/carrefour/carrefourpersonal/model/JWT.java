package br.com.personal.carrefour.carrefourpersonal.model;

/**
 * Created by ASUS on 24/02/2018.
 */

public class JWT {

    private String jwt;

    public JWT (String jwt){
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
