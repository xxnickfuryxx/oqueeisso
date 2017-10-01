package com.br.oqueeisso.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.oqueeisso.R;
import com.br.oqueeisso.helper.PerfilDataBaseHelper;
import com.br.oqueeisso.model.Perfil;
import com.br.oqueeisso.util.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by xxnickfuryxx on 25/08/15.
 */
public class SplashActivity extends Activity {

    private Context context;
    private Activity activity;
    private static final long TIMESPLASH = 500;
    private Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        context = this;
        activity = this;

        System.out.println("teste_conflito");

        try {
            Utils.createFolder();
        } catch (IOException e) {
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }
        initialize();
        perfil = openDataBase();

    }

    private void initialize(){

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        AlphaAnimation animation = new AlphaAnimation(-3.0f, 2.0f);
        animation.setDuration(2000);

        ImageView img_logo = (ImageView)findViewById(R.id.img_logo);
        img_logo.setImageResource(R.drawable.oqueeisso_logo_peq);
        img_logo.startAnimation(animation);

        TextView txt_app = (TextView)findViewById(R.id.txt_app);
        txt_app.setText(getResources().getString(R.string.app_name));
        txt_app.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(perfil.getNome() != null){

                            Intent i = new Intent(context, DashboardActivity.class);
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setAction("android.intent.action.MAIN");

                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);                        SplashActivity.this.finish();

                        }else{
                            Intent i = new Intent(context, FirstAccessActivity.class);
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setAction("android.intent.action.MAIN");

                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            SplashActivity.this.finish();
                        }

                    }
                },TIMESPLASH);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private Perfil openDataBase(){
        PerfilDataBaseHelper perfilDataBaseHelper = new PerfilDataBaseHelper(this);
        SQLiteDatabase db = perfilDataBaseHelper.getReadableDatabase();
        perfilDataBaseHelper.onCreate(db);
        return perfilDataBaseHelper.buscaPerfil(db);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
