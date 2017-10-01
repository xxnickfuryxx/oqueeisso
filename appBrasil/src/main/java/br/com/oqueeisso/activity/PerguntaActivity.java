package br.com.oqueeisso.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import br.com.oqueeisso.R;
import br.com.oqueeisso.model.Duvida;
import br.com.oqueeisso.util.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by xxnickfuryxx on 07/08/15.
 */
public class PerguntaActivity extends Activity implements SlidingMenu.OnOpenListener,
                                                        SlidingMenu.OnCloseListener,
                                                        View.OnClickListener,
                                                        SurfaceHolder.Callback,
                                                        ImageSwitcher.OnTouchListener{


    private SlidingMenu menu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Duvida duvida;
    private Camera camera;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private byte[] picture;
    float initialX = 0;
    int photoInital = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pergunta_layout);

        try {
            Utils.createFolder();
        } catch (IOException e) {
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }

        initialize();
        Utils.menuLateral(this, this);
    }


    //MENU
    private void initialize() {
        menu = Utils.createMenuLateral(this);
        menu.setOnOpenListener(this);
        menu.setOnCloseListener(this);

        ImageView btn_menu = (ImageView) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

        final ImageView img_save = (ImageView) findViewById(R.id.img_save);
        img_save.setOnClickListener(this);

        final ImageButton img_photo = (ImageButton) findViewById(R.id.img_photo);
        img_photo.setOnClickListener(this);

        final ImageButton btn_responder = (ImageButton) findViewById(R.id.btn_responder);
        btn_responder.setOnClickListener(this);

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surf_resource);
        SurfaceHolder sh = surfaceView.getHolder();
        sh.addCallback(this);

        ImageView btn_filter = (ImageView)findViewById(R.id.btn_filter);
        btn_filter.setVisibility(ImageView.INVISIBLE);

        ImageSwitcher img_resource = (ImageSwitcher)findViewById(R.id.img_resource);

        img_resource.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                myView.setScaleType(ImageView.ScaleType.FIT_XY);

                return myView;
            }
        });
        img_resource.setOnTouchListener(this);

        Utils.createAnimation(new View[]{img_photo, btn_responder}, R.anim.button_right_left_appear);



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
        }else if(view.getId() == R.id.img_photo) {

            Utils.playCameraCapture(this);
            camera.takePicture(null, null, new CameraCallback());

            ImageView img_save = (ImageView) findViewById(R.id.img_save);
            img_save.setVisibility(ImageView.VISIBLE);
            Utils.createAnimation(new View[]{img_save}, R.anim.button_right_left_appear);


        }else if(view.getId() == R.id.img_save){

            RelativeLayout rel_surface = (RelativeLayout)findViewById(R.id.rel_surface);
            rel_surface.setVisibility(RelativeLayout.GONE);

            RelativeLayout rel_image = (RelativeLayout)findViewById(R.id.rel_image);
            rel_image.setVisibility(ImageView.VISIBLE);

            ImageView btn_remove = (ImageView)findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(PerguntaActivity.this);

            ImageSwitcher img_resource = (ImageSwitcher)findViewById(R.id.img_resource);
            img_resource.setImageURI(Uri.parse(Environment.getExternalStorageDirectory()+"/oqueeisso/"+photoInital+".jpg"));

            Utils.createAnimation(new View[]{rel_surface}, R.anim.slide_out_right);
            Utils.createAnimation(new View[]{rel_image}, R.anim.slide_in_left);

        }else if(view.getId() == R.id.btn_remove){

            ImageView btn_remove = (ImageView)findViewById(R.id.btn_remove);
            btn_remove.setOnClickListener(PerguntaActivity.this);

            RelativeLayout rel_surface = (RelativeLayout)findViewById(R.id.rel_surface);
            rel_surface.setVisibility(RelativeLayout.VISIBLE);

            RelativeLayout rel_image = (RelativeLayout)findViewById(R.id.rel_image);
            rel_image.setVisibility(RelativeLayout.GONE);

            ImageButton img_save = (ImageButton) findViewById(R.id.img_save);
            img_save.setVisibility(ImageView.INVISIBLE);

            Utils.createAnimation(new View[]{rel_surface}, R.anim.slide_in_left);
            Utils.createAnimation(new View[]{rel_image}, R.anim.slide_out_right);
            Utils.createAnimation(new View[]{img_save}, R.anim.button_right_left_disappear);

            try {
                Utils.createFolder();
            } catch (IOException e) {
                Log.e(PerguntaActivity.class.getName(), e.getMessage());
            }

            camera.startPreview();

            System.gc();

        }

        Utils.genericOnClick(this, view);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(camera == null){
            camera = Camera.open();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(camera == null){
            camera = Camera.open();
        }
        Parameters p = camera.getParameters();
        p.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        p.setFlashMode(Parameters.FLASH_MODE_AUTO);
        p.setJpegQuality(25);

        camera.setParameters(p);

        try {

            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e) {
            Log.e(PerguntaActivity.class.getName(), "" + e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        // finish();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageSwitcher img_resource = (ImageSwitcher)findViewById(R.id.img_resource);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX > finalX) {
                    if(photoInital < Utils.getQtdeImages()){
                        photoInital++;
                        img_resource.setImageURI(Uri.parse(Environment.getExternalStorageDirectory()+"/oqueeisso/"+(photoInital)+".jpg"));
                        Utils.createAnimation(new View[]{img_resource}, R.anim.slide_in_right);
                    }
                }else{
                    if(photoInital > 1) {
                        photoInital--;
                        img_resource.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/oqueeisso/" + (photoInital) + ".jpg"));
                        Utils.createAnimation(new View[]{img_resource}, R.anim.slide_in_left);
                    }
                }
                break;
        }



        return true;
    }

    public class CameraCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            Matrix mat = new Matrix();
            mat.postRotate(90);
            mat.postScale(.3f, .3f);

            Bitmap correctBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), mat, true);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            correctBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            String fileName = (Utils.getQtdeImages()+1)+".jpg";

            File file = new File(Environment.getExternalStorageDirectory()+"/oqueeisso/"+fileName);

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(stream.toByteArray());
                bos.flush();
                bos.close();

            } catch (FileNotFoundException e) {
                Log.e(PerguntaActivity.class.getName(), e.getMessage());


            } catch (IOException e) {
                Log.e(PerguntaActivity.class.getName(), e.getMessage());
            }

            camera.startPreview();


        }



    }

}
