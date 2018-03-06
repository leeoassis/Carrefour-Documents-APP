package br.com.personal.carrefour.carrefourpersonal.model;

/**
 * Created by ASUS on 23/02/2018.
 */

public class Item {

    String itemName;
    int itemImagem;

    public Item(String Name, int Image)
    {
        this.itemImagem=Image;
        this.itemName=Name;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemImagem() {
        return itemImagem;
    }

}
