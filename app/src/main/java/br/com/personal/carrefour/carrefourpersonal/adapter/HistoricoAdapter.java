package br.com.personal.carrefour.carrefourpersonal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.personal.carrefour.carrefourpersonal.R;
import br.com.personal.carrefour.carrefourpersonal.response.ArquivoNome;

public class HistoricoAdapter extends BaseAdapter implements Filterable {

    private List<ArquivoNome> arquivos;
    private List<ArquivoNome> mStringFilterList;
    private Context context;
    ValueFilter valueFilter;

    public HistoricoAdapter(Context context, List<ArquivoNome> arquivos) {
        this.arquivos = arquivos;
        this.context = context;
        mStringFilterList = arquivos;
    }

    @Override
    public int getCount() {
        return arquivos.size();
    }

    @Override
    public Object getItem(int position) {
        return arquivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arquivos.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.item_list_arquivos, parent, false);

        TextView tvDescricao = (TextView) rootView.findViewById(R.id.descricao);
        ImageView ivIcon = (ImageView) rootView.findViewById(R.id.item_foto);

        ArquivoNome arquivoNome = arquivos.get(position);
        String nome = arquivoNome.getNome();
        tvDescricao.setText(arquivoNome.getNome());

        if (nome.contains(".doc") || nome.contains(".docx")) {
            ivIcon.setBackgroundResource(R.drawable.icon_word);
        } else if(nome.contains(".pdf")) {
            ivIcon.setBackgroundResource(R.drawable.icon_pdf);
        } else if(nome.contains(".ppt") || nome.contains(".pptx")) {
            ivIcon.setBackgroundResource(R.drawable.icon_ppt);
        } else if(nome.contains(".xls") || nome.contains(".xlsx")) {
            ivIcon.setBackgroundResource(R.drawable.icon_xls);
        } else if(nome.contains(".zip") || nome.contains(".rar")) {
            ivIcon.setBackgroundResource(R.drawable.icon_zip);
        } else if(nome.contains(".jpg") || nome.contains(".jpeg") || nome.contains(".png")) {
            ivIcon.setBackgroundResource(R.drawable.icon_jpg);
        } else if(nome.contains(".txt")) {
            ivIcon.setBackgroundResource(R.drawable.icon_txt);
        } else {
            ivIcon.setBackgroundResource(R.drawable.icon_file);
        }

        return rootView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ArquivoNome> filterList = new ArrayList<ArquivoNome>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getNome().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        ArquivoNome chamadoResposta = new ArquivoNome(mStringFilterList.get(i).getNome());

                        filterList.add(chamadoResposta);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            arquivos = (ArrayList<ArquivoNome>) results.values;
            notifyDataSetChanged();
        }

    }

}
