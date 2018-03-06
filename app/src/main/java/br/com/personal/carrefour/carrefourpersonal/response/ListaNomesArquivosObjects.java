package br.com.personal.carrefour.carrefourpersonal.response;

import java.util.List;

/**
 * Created by ASUS on 25/02/2018.
 */

public class ListaNomesArquivosObjects {

    private List<ArquivoNome> arquivos;

    public ListaNomesArquivosObjects(List<ArquivoNome> arquivos) {
        this.arquivos = arquivos;
    }

    public ListaNomesArquivosObjects(){}

    public List<ArquivoNome> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoNome> arquivos) {
        this.arquivos = arquivos;
    }
}
