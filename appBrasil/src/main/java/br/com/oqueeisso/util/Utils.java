package br.com.oqueeisso.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import br.com.oqueeisso.R;
import br.com.oqueeisso.activity.DashboardActivity;
import br.com.oqueeisso.activity.PerguntaActivity;
import br.com.oqueeisso.dialog.PerfilDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xxnickfuryxx on 24/07/15.
 */
public class Utils {
    public static SlidingMenu createMenuLateral(Activity activity){
        SlidingMenu slidingPaneLayout = new SlidingMenu(activity);

        //Agora para abrir o menu, o dedo tem que partir da argin
        slidingPaneLayout.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        slidingPaneLayout.setShadowWidthRes(R.dimen.shadow_width);
        slidingPaneLayout.setShadowDrawable(R.drawable.shadow);
        slidingPaneLayout.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingPaneLayout.setFadeDegree(0.35f);
        slidingPaneLayout.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        slidingPaneLayout.setMenu(R.layout.menu_frame);

        return slidingPaneLayout;
    }

    public static void menuLateral(Activity activity, View.OnClickListener onClickListener){

        TextView txt_perfil = (TextView)activity.findViewById(R.id.txt_perfil);
        txt_perfil.setOnClickListener(onClickListener);

        TextView txt_menu_duvida = (TextView)activity.findViewById(R.id.txt_menu_duvida);
        txt_menu_duvida.setOnClickListener(onClickListener);

        TextView txt_nova_duvida = (TextView)activity.findViewById(R.id.txt_nova_duvida);
        txt_nova_duvida.setOnClickListener(onClickListener);

        TextView txt_respostas = (TextView)activity.findViewById(R.id.txt_respostas);
        txt_respostas.setOnClickListener(onClickListener);

        //TextView txt_config = (TextView)activity.findViewById(R.id.txt_config);
        //txt_config.setOnClickListener(onClickListener);

        TextView txt_sobre = (TextView)activity.findViewById(R.id.txt_sobre);
        txt_sobre.setOnClickListener(onClickListener);
    }

    public static void genericOnClick(Activity activity, View view){

        if(view.getId() == R.id.txt_perfil){

            PerfilDialog perfilDialog = new PerfilDialog(activity, view.getContext());
            perfilDialog.show();

        }else if (view.getId() == R.id.txt_menu_duvida) {

            Intent i = new Intent(activity, DashboardActivity.class);
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }else if(view.getId() == R.id.txt_sobre){

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(activity.getResources().getString(R.string.url_sobre)));
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }else if(view.getId() == R.id.txt_nova_duvida){

            Intent i = new Intent(activity, PerguntaActivity.class);
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }


    public static boolean isConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo netInfos[] = cm.getAllNetworkInfo();
            if(netInfos != null){
                for(NetworkInfo networkInfo:netInfos){
                    if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        }
        return false;
    }


    public static void createAnimation(View[] views, int id){
        for(View view:views){
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), id);
            view.startAnimation(animation);
        }
    }

    public static void playCameraCapture(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.camera_sound);
        mp.start();
    }

    public static int getQtdeImages(){

        File file = new File(Environment.getExternalStorageDirectory()+"/oqueeisso");

        return file.list().length - 1;
    }

    public static void createFolder() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory()+"/oqueeisso");
        File fileNomedia = new File(Environment.getExternalStorageDirectory()+"/oqueeisso/.nomedia");
        if(!file.exists()){
            file.mkdir();
            fileNomedia.createNewFile();
        }else{
            for(File files : file.listFiles()){
                if(!files.getName().contains("nomedia")){
                    files.delete();
                }
            }
        }
    }

    public static byte[] getBytes(Uri uri, Context context){
        byte[] data = null;
        try {

            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            data = baos.toByteArray();

        } catch (FileNotFoundException e) {
            Log.e(Utils.class.getName(), e.getMessage());
        }
        return data;
    }

    public static Bitmap createImageCircle(Bitmap bitmap){
        Bitmap targetBitmap = null;

        if (bitmap != null) {

            int targetWidth = 100;
            int targetHeight = 95;
            targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            BitmapShader shader;
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);
            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(
                    ((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                    Path.Direction.CCW);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setStyle(Paint.Style.STROKE);
            canvas.clipPath(path);
            Bitmap sourceBitmap = bitmap;
            canvas.drawBitmap(
                    sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap
                            .getHeight()), new Rect(0, 0, targetWidth,
                            targetHeight), null);

        }
        return targetBitmap;
    }

}
