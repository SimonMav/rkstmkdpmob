package at.mmco.rkdienstplanmobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class CustomDialogShortClass extends Dialog implements
        View.OnClickListener {

    private Activity c;
    public Dialog d;
    private Button yes, no;

    CustomDialogShortClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_short);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Intent contribute = new Intent(v.getContext(), ContributeActivity.class);
                v.getContext().startActivity(contribute);
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}