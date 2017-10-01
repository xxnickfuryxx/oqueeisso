package br.com.oqueeisso.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.oqueeisso.R;

public class WaitDialog extends Dialog{

    public WaitDialog(Context context){
        super(context, R.style.WaitDialogFull);
        setContentView(R.layout.wait_layout);

        initialize();


    }

    private void initialize() {

    }

    @Override
    public void show() {
        super.show();

    }


}
