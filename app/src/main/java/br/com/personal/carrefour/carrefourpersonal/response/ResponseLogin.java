package br.com.personal.carrefour.carrefourpersonal.response;


public class ResponseLogin {

    private Long id;
    private Long idSessao;
    private String ds_status;
    private String ds_msg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(Long idSessao) {
        this.idSessao = idSessao;
    }

    public String getDs_status() {
        return ds_status;
    }

    public void setDs_status(String ds_status) {
        this.ds_status = ds_status;
    }

    public String getDs_msg() {
        return ds_msg;
    }

    public void setDs_msg(String ds_msg) {
        this.ds_msg = ds_msg;
    }

    @Override
    public String toString() {
        return "{\"idSessao\":" + getIdSessao() + ",\"ds_status\":\""+ getDs_status() + "\", \"ds_msg\":\""+ getDs_msg() +"\"}";
    }
}
