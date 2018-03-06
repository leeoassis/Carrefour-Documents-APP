package br.com.personal.carrefour.carrefourpersonal;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.personal.carrefour.carrefourpersonal.adapter.HistoricoAdapter;
import br.com.personal.carrefour.carrefourpersonal.dao.UsuarioDAO;
import br.com.personal.carrefour.carrefourpersonal.response.ArquivoByte;
import br.com.personal.carrefour.carrefourpersonal.response.ArquivoNome;
import br.com.personal.carrefour.carrefourpersonal.response.ResponseLogin;
import br.com.personal.carrefour.carrefourpersonal.response.ListaNomesArquivosObjects;
import br.com.personal.carrefour.carrefourpersonal.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListaArquivosActivity extends AppCompatActivity {

    private ListView listaArquivos;
    private HistoricoAdapter adapter;
    private SwipeRefreshLayout swipe;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_arquivos);

        getSupportActionBar().setIcon(R.drawable.actionbar_carrefour);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermissionREAD_EXTERNAL_STORAGE(ListaArquivosActivity.this);

        listaArquivos = (ListView) findViewById(R.id.lista_arquivos);
        chamadaListaNomesArquivos();

        //Carregar listar ao puxar para baixo
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_arquivo);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chamadaListaNomesArquivos();
            }
        });

        registerForContextMenu(listaArquivos);

        listaArquivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArquivoNome arquivoNome = (ArquivoNome) listaArquivos.getItemAtPosition(position);
                final String nomeArquivo = arquivoNome.getNome();


                File mydir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File lister = mydir.getAbsoluteFile();
                ArrayList<String> lista = new ArrayList<String>(Arrays.asList(lister.list()));
                if (lista.contains(nomeArquivo)) {
                    //for (String list : lister.list()){
                    //if(list.equals(nomeArquivo)){
                    File file = new File(mydir, nomeArquivo);
                    fileOpen(file);
                } else {
                    String msg = "Arquivo \"" + nomeArquivo + "\" não encontrado!";
                    Snackbar.make(findViewById(R.id.activity_lista_arquivos), msg, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.button_message_baixar), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callDownloadArquivo(nomeArquivo);
                                }
                            }).show();
                    ;

                }
            }
        //}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chamadaListaNomesArquivos();
    }

    private void fileOpen(File file){
        //Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(ListaArquivosActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
            intent.setDataAndType(uri, "application/msword");
        } else if(file.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if(file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(file.toString().contains(".zip") || file.toString().contains(".rar")) {
            intent.setDataAndType(uri, "application/x-wav");
        } else if(file.toString().contains(".jpg") || file.toString().contains(".jpeg") || file.toString().contains(".png")) {
            intent.setDataAndType(uri, "image/jpeg");
        } else if(file.toString().contains(".txt")) {
            intent.setDataAndType(uri, "text/plain");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        startActivity(intent);
    }

    private void chamadaListaNomesArquivos() {

        UsuarioDAO dao = new UsuarioDAO(ListaArquivosActivity.this);
        ResponseLogin responseLogin = dao.buscaIdSessao();

        Long idSessao = responseLogin.getIdSessao();

        Call<ListaNomesArquivosObjects> call = new RetrofitInicializador().getGenericService().listaNomesArquivosObject(idSessao);
        call.enqueue(new Callback<ListaNomesArquivosObjects>() {
            @Override
            public void onResponse(Call<ListaNomesArquivosObjects> call, Response<ListaNomesArquivosObjects> response) {
                Log.i("RESPONSE ====== ", response.body().toString());
                ListaNomesArquivosObjects arquivosSync = response.body();
                List<ArquivoNome> arquivos = arquivosSync.getArquivos();
                adapter = new HistoricoAdapter(ListaArquivosActivity.this, arquivos);
                listaArquivos.setAdapter(adapter);
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ListaNomesArquivosObjects> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_lista_arquivos),"Problema de Conexão",Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.button_message_network), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                swipe.setRefreshing(false);
            }
        });
    }

    public void callDownloadArquivo(final String nomeArquivo){

        UsuarioDAO dao = new UsuarioDAO(ListaArquivosActivity.this);
        ResponseLogin responseLogin = dao.buscaIdSessao();

        Long idSessao = responseLogin.getIdSessao();

        ArquivoByte file = new ArquivoByte();
        file.setFile(nomeArquivo);

        final ProgressDialog dialog = iniciarProgressDialog(nomeArquivo);

        Call<ArquivoByte> call = new RetrofitInicializador().getGenericService().enviarNomeArquivo(idSessao, file);
        call.enqueue(new Callback<ArquivoByte>() {
            @Override
            public void onResponse(Call<ArquivoByte> call, Response<ArquivoByte> response) {
                dialog.dismiss();
                ArquivoByte arquivoByte = response.body();
                String byteArray = arquivoByte.getFile();
                byte[] decode = android.util.Base64.decode(byteArray, 0);

                File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                final String directory = externalStoragePublicDirectory.toString() + "/" + nomeArquivo;

                try {
                    FileOutputStream fos = new FileOutputStream(new File(directory));
                    fos.write(decode);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Snackbar.make(findViewById(R.id.activity_lista_arquivos),"Download Concluído",Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.button_message_file), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File(directory);
                                fileOpen(file);
                            }
                        }).show();

            }

            @Override
            public void onFailure(Call<ArquivoByte> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_lista_arquivos),"Problema de Conexão",Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.button_message_network), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                Log.e("RESPOSTA====", t.getMessage());
            }
        });
    }

    @NonNull
    private ProgressDialog iniciarProgressDialog(String nomeArquivo) {
        final ProgressDialog dialog = new ProgressDialog(ListaArquivosActivity.this);
        dialog.setMessage("Baixando Arquivo \""+ nomeArquivo +  "\"...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Acessar fotos", context, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permissão necessária");
        alertBuilder.setMessage(msg + " Permissão é necessária");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    // Criação e ação de filtro do Search View no topo da tela
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView sv = (SearchView) item.getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem baixar = menu.add("Download");
        baixar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                ArquivoNome arquivoNome = (ArquivoNome) listaArquivos.getItemAtPosition(info.position);
                callDownloadArquivo(arquivoNome.getNome());
                return false;
            }
        });
    }
}
