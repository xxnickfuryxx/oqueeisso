package br.com.oqueeisso.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.oqueeisso.R;
import br.com.oqueeisso.adapter.DuvidasAdapter;
import br.com.oqueeisso.dialog.FilterDialog;
import br.com.oqueeisso.dialog.PerfilDialog;
import br.com.oqueeisso.helper.PerfilDataBaseHelper;
import br.com.oqueeisso.model.Duvida;
import br.com.oqueeisso.model.Perfil;
import br.com.oqueeisso.util.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Calendar;

public class DashboardActivity extends Activity implements SlidingMenu.OnOpenListener,
        SlidingMenu.OnCloseListener,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        GridView.OnItemClickListener {

    private SlidingMenu menu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle savedInstanceState;
    private PerfilDataBaseHelper perfilDataBaseHelper;
    private SQLiteDatabase db;
    private Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;


        perfil = openDataBase();

        if(Utils.isConnected(this)){
            setContentView(R.layout.dashboard);
            initialize(true);
        }else{
            setContentView(R.layout.no_connection_layout);
            initialize(false);
        }

        Utils.menuLateral(this, this);

    }


    //MENU
    private void initialize(boolean isConnected) {

        menu = Utils.createMenuLateral(this);
        menu.setOnOpenListener(this);
        menu.setOnCloseListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);


        View header =(View) findViewById(R.id.header);
        Utils.createAnimation(new View[]{header}, R.anim.zoom_in);

        ImageView btn_menu = (ImageView) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

        ImageView btn_filter = (ImageView) findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(this);

        if(isConnected){
            GridView lv_duvidas = (GridView) this.findViewById(R.id.lv_duvidas);
            lv_duvidas.setOnItemClickListener(this);
            DuvidasAdapter duvidasAdapter = new DuvidasAdapter(this, R.layout.adapter_duvida_1, mockDuvidas());

            lv_duvidas.setAdapter(duvidasAdapter);
            duvidasAdapter.notifyDataSetChanged();
        }

        TextView txt_perfil = (TextView)findViewById(R.id.txt_perfil);
        txt_perfil.setText(perfil.getNome());

        if(perfil.getImgPerfil() != null){
            ImageView img_perfil = (ImageView)findViewById(R.id.img_perfil);
            byte[] b = perfil.getImgPerfil();
            img_perfil.setImageBitmap(Utils.createImageCircle(BitmapFactory.decodeByteArray(b,0,b.length)));

        }

    }

    private Perfil openDataBase(){
        perfilDataBaseHelper = new PerfilDataBaseHelper(this);
        db = perfilDataBaseHelper.getReadableDatabase();
        return perfilDataBaseHelper.buscaPerfil(db);
    }

    private ArrayList<Duvida> mockDuvidas() {
        ArrayList<Duvida> list = new ArrayList<Duvida>();
        Duvida duvida = new Duvida();
        duvida.setId(1);
        duvida.setDuvida("Qual o tamanho do planeta terra?");
        duvida.setUsrCad("George Washington");
        duvida.setDataDuvida(Calendar.getInstance().getTime());

        Duvida duvida2 = new Duvida();
        duvida2.setId(2);
        duvida2.setDuvida("E normal ter 2 pais?");
        duvida2.setUsrCad("Anonimo?");
        duvida2.setDataDuvida(Calendar.getInstance().getTime());

        Duvida duvida3 = new Duvida();
        duvida3.setId(3);
        duvida3.setDuvida("O que e crista de galo?");
        duvida3.setUsrCad("Anonimo");
        duvida3.setDataDuvida(Calendar.getInstance().getTime());

        Duvida duvida4 = new Duvida();
        duvida4.setId(4);
        duvida4.setDuvida("O que cai em pe e corre deitado?");
        duvida4.setUsrCad("Nelson Mandela");
        duvida4.setDataDuvida(Calendar.getInstance().getTime());

        Duvida duvida5 = new Duvida();
        duvida5.setId(5);
        duvida5.setDuvida("O que pesa mais, 1kg de algodao ou de chumbo?");
        duvida5.setUsrCad("Gorbatchov");
        duvida5.setDataDuvida(Calendar.getInstance().getTime());

        list.add(duvida);
        list.add(duvida2);
        list.add(duvida3);
        list.add(duvida4);
        list.add(duvida5);

        return list;
    }


    @Override
    public void onOpen() {
        ImageView btn_menu = (ImageView) findViewById(R.id.btn_menu);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, -50, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(500);

        btn_menu.startAnimation(translateAnimation);
    }

    @Override
    public void onClose() {
        ImageView btn_menu = (ImageView) findViewById(R.id.btn_menu);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(500);

        btn_menu.startAnimation(translateAnimation);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_menu) {
            menu.showMenu(true);

        } else if (view.getId() == R.id.btn_filter) {
            FilterDialog filterDialog = new FilterDialog(this);
            filterDialog.show();
        }

        Utils.genericOnClick(this, view);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Duvida d = (Duvida) parent.getItemAtPosition(position);

        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra("duvida", d);
        this.startActivity(i);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

    @Override
    public void onRefresh() {

        onCreate(this.savedInstanceState);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = imageReturnedIntent.getData();
                    perfil.setImgPerfil(Utils.getBytes(selectedImage, this));
                    perfilDataBaseHelper.updatePerfil(db, perfil);

                    TextView txt_perfil = (TextView)findViewById(R.id.txt_perfil);
                    txt_perfil.setText(perfil.getNome());
                    
                    ImageView img_perfil = (ImageView)findViewById(R.id.img_perfil);
                    byte[] b = perfil.getImgPerfil();
                    img_perfil.setImageBitmap(Utils.createImageCircle(BitmapFactory.decodeByteArray(b,0,b.length)));

                    new PerfilDialog(this, this).show();

                }
        }
    }
}
