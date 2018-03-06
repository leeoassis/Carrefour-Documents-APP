package br.com.personal.carrefour.carrefourpersonal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.personal.carrefour.carrefourpersonal.adapter.MyAdapter;
import br.com.personal.carrefour.carrefourpersonal.model.Item;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREF_NAME = "LoginActivityPreferences";
    private ListView simpleList;
    private ArrayList<Item> list;
    private Bitmap bitMap;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.actionbar_carrefour);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        Intent intent = getIntent();
        String nome_login = (String) intent.getSerializableExtra("nomeLogin");

        list = new ArrayList<>();

        simpleList = (ListView) findViewById(R.id.lista_menu);
        list.add(new Item("Meus Arquivos", R.drawable.ic_arquivos));

        MyAdapter myAdapter = new MyAdapter(this, R.layout.item_list_menu, list);
        simpleList.setAdapter(myAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, ListaArquivosActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    //Intent intent = new Intent(MainActivity.this, RegistrarSugestaoActivity.class);
                    //startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView txt_nome = (TextView) hView.findViewById(R.id.nome_menu);
        TextView txt_email = (TextView) hView.findViewById(R.id.email_menu);
        txt_nome.setText(nome_login);
        txt_email.setText(nome_login + "@hotmail.com");
        //CircleImageView iv = (CircleImageView) hView.findViewById(R.id.imagem_menu);
        //Picasso.with(this).load("https://pbs.twimg.com/profile_images/465403189/webcam1_400x400.jpg").resize(80,80).centerCrop().into(iv);

    }

    @Override
    protected void onResume() {
        if(getIntent().getBooleanExtra("SAIR", false)){
            finish();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lista_arquivos) {
            Intent intent = new Intent(MainActivity.this, ListaArquivosActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deseja Sair?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear().commit();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alerta.dismiss();
                }
            });
            alerta = builder.create();
            alerta.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
