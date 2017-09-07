package com.br.oqueeisso.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.oqueeisso.R;
import com.br.oqueeisso.activity.DashboardActivity;
import com.br.oqueeisso.dialog.WaitDialog;
import com.br.oqueeisso.helper.PerfilDataBaseHelper;
import com.br.oqueeisso.model.Perfil;
import com.br.oqueeisso.util.CountryCodes;


public class ThirdFragment extends Fragment implements View.OnClickListener,
                                                        DialogInterface.OnClickListener{

    private SQLiteDatabase db;
    private PerfilDataBaseHelper perfilDataBaseHelper;
    private TextView nome;
    private TextView country_code;
    private TextView area_code;
    private TextView phone;
    private TextView email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_third, container, false);

        openDataBase();

        TextView btn_ok = (TextView)view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        TelephonyManager tm = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);

        country_code = (TextView)view.findViewById(R.id.country_code);
        area_code = (TextView)view.findViewById(R.id.area_code);
        email = (TextView)view.findViewById(R.id.email);
        phone = (TextView)view.findViewById(R.id.phone);
        nome = (TextView)view.findViewById(R.id.nome);

        country_code.setText(CountryCodes.getCode(tm.getSimCountryIso().toUpperCase()));

        return view;
    }

    private void openDataBase(){
        perfilDataBaseHelper = new PerfilDataBaseHelper(getContext());
        db = perfilDataBaseHelper.getReadableDatabase();
        perfilDataBaseHelper.onCreate(db);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok){

            if(nome.getText().toString().equals("") ||
                    country_code.getText().toString().equals("") ||
                    area_code.getText().toString().equals("") ||
                    phone.getText().toString().equals("") ||
                    email.getText().toString().equals("")){

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Error de Validação");
                alertDialog.setMessage("Todos os campos são obrigatórios");
                alertDialog.setPositiveButton("Ok", ThirdFragment.this);
                alertDialog.show();

            }else{

                Perfil perfil = new Perfil();
                perfil.setNome(nome.getText().toString());
                perfil.setEmail(email.getText().toString());
                perfil.setCountryCode(country_code.getText().toString());
                perfil.setAreaCode(area_code.getText().toString());
                perfil.setPhone(phone.getText().toString());

                perfilDataBaseHelper.insertPerfil(db, perfil);

                new WaitDialog(getContext()).show();
                Intent i = new Intent(v.getContext(), DashboardActivity.class);
                i.setAction(Intent.ACTION_MAIN);
                getContext().startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }


        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
