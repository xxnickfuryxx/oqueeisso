package br.com.oqueeisso.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.oqueeisso.R;
import br.com.oqueeisso.helper.PerfilDataBaseHelper;
import br.com.oqueeisso.model.Perfil;
import br.com.oqueeisso.util.Utils;

public class PerfilDialog extends Dialog implements View.OnClickListener{

    private Perfil perfil;
    private Activity activity;
    private Context context;
    private PerfilDataBaseHelper perfilDataBaseHelper;
    private SQLiteDatabase db;

    public PerfilDialog(Activity activity, Context context){
        super(context, R.style.AppThemeDialogFullScreen);
        setContentView(R.layout.perfil_layout);
        this.activity = activity;
        this.context = context;

        perfil = openDataBase();
        initialize();

    }

    private void initialize() {

        TextView txt_perfil = (TextView)findViewById(R.id.txt_perfil);
        txt_perfil.setText(perfil.getNome());

        ImageView img_perfil = (ImageView)findViewById(R.id.img_perfil);
        img_perfil.setOnClickListener(this);

        if(perfil.getImgPerfil() != null){
            byte[] b = perfil.getImgPerfil();
            img_perfil.setImageBitmap(Utils.createImageCircle(BitmapFactory.decodeByteArray(b, 0, b.length)));
        }

        TextView btn_ok = (TextView)this.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        TextView btn_cancelar = (TextView)this.findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(this);
    }

    private Perfil openDataBase(){
        perfilDataBaseHelper = new PerfilDataBaseHelper(getContext());
        db = perfilDataBaseHelper.getReadableDatabase();
        return perfilDataBaseHelper.buscaPerfil(db);
    }

    @Override
    public void show() {

        super.show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok){

            TextView txt_perfil = (TextView)findViewById(R.id.txt_perfil);
            perfil.setNome(txt_perfil.getText().toString());
            perfilDataBaseHelper.updatePerfil(db, perfil);

            this.dismiss();


        }else if(v.getId() == R.id.btn_cancelar) {
            this.dismiss();
        }else if(v.getId() == R.id.img_perfil){

            this.dismiss();

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.putExtra("CROP", true);
            photoPickerIntent.setType("image/*");

            activity.startActivityForResult(photoPickerIntent, 1);

        }
    }


}
