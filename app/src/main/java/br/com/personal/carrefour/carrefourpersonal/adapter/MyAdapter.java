package br.com.personal.carrefour.carrefourpersonal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.personal.carrefour.carrefourpersonal.R;
import br.com.personal.carrefour.carrefourpersonal.model.Item;

/**
 * Created by ASUS on 23/02/2018.
 */

public class MyAdapter  extends ArrayAdapter<Item> {

    ArrayList<Item> itemList = new ArrayList<>();

    //
    public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        itemList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_list_menu, null);
        TextView textView = (TextView) v.findViewById(R.id.txt_item_list);
        ImageView imageView = (ImageView) v.findViewById(R.id.img_item_list);
        textView.setText(itemList.get(position).getItemName());
        imageView.setImageResource(itemList.get(position).getItemImagem());
        return v;

    }
}
