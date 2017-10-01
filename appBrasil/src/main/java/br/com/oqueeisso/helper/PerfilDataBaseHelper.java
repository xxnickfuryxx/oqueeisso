package br.com.oqueeisso.helper;

/**
 * Created by xxnickfuryxx on 27/09/15.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import br.com.oqueeisso.model.Perfil;

public class PerfilDataBaseHelper extends SQLiteOpenHelper {

    //private static final String DATABASE_NAME = "/sdcard/oqueeisso/oqueeisso.db";
    private static final String DATABASE_NAME = "oqueeisso.db";
    public static final String TABLE_NAME = "tb_perfil";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE "
                                                + TABLE_NAME + "( "
                                                + "nome text, "
                                                + "email text,  "
                                                + "country_code text,  "
                                                + "area_code text,  "
                                                + "phone text,  "
                                                + "img_perfil integer); ";

    public PerfilDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(!isExist(db, TABLE_NAME)){
            db.execSQL(DATABASE_CREATE);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        onCreate(db);
    }


    private boolean isExist(SQLiteDatabase db, String table){

        boolean retorno = false;

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+table+"'", null);
        try{
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    retorno = true;
                }else {

                    retorno = false;

                }
            }
        }finally{
            cursor.close();
        }

        return retorno;
    }


    public void insertPerfil(
            SQLiteDatabase db, Perfil perfil){

        SQLiteStatement liteProgram =
                db.compileStatement(" INSERT INTO "+TABLE_NAME+" VALUES(?,?,?,?,?,?); ");

        liteProgram.bindString(1, perfil.getNome());
        liteProgram.bindString(2, perfil.getEmail());
        liteProgram.bindString(3, perfil.getCountryCode());
        liteProgram.bindString(4, perfil.getAreaCode());
        liteProgram.bindString(5, perfil.getPhone());
        if(perfil.getImgPerfil() == null){
            liteProgram.bindNull(6);
        }else{
            liteProgram.bindBlob(6, perfil.getImgPerfil());
        }

        try{
            liteProgram.execute();

        }catch(SQLException e){
            Log.e(PerfilDataBaseHelper.class.getName(), e.getMessage());
        }finally{
            liteProgram.close();
        }

    }

    public void updatePerfil(
            SQLiteDatabase db, Perfil perfil){

        SQLiteStatement liteProgram =
                db.compileStatement(" UPDATE "+TABLE_NAME+" " +
                                        "SET NOME = ?, " +
                                        "IMG_PERFIL = ? " +
                                        "WHERE PHONE = ? ");

        liteProgram.bindString(1, perfil.getNome());
        liteProgram.bindBlob(2, perfil.getImgPerfil());
        liteProgram.bindString(3, perfil.getPhone());

        try{
            liteProgram.execute();

        }catch(SQLException e){
            Log.e(PerfilDataBaseHelper.class.getName(), e.getMessage());
        }finally{
            liteProgram.close();
        }

    }

    public Perfil buscaPerfil(SQLiteDatabase db){

        Perfil perfil = new Perfil();

        StringBuffer  query = new StringBuffer();
        query.append(" SELECT * ");
        query.append(" FROM "+TABLE_NAME  );

        Cursor cursor = db.rawQuery(query.toString(),null);
        try{
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{

                        perfil.setNome(cursor.getString(0));
                        perfil.setEmail(cursor.getString(1));
                        perfil.setCountryCode(cursor.getString(2));
                        perfil.setAreaCode(cursor.getString(3));
                        perfil.setPhone(cursor.getString(4));
                        perfil.setImgPerfil(cursor.getBlob(5));

                    }while(cursor.moveToNext());
                }
            }
        }finally{
            cursor.close();
        }

        return perfil;
    }

}
