package at.mmco.rkdienstplanmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class AboutActivity extends AppCompatActivity {

    final Context thisApp = this;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button clearPrefsButton =findViewById(R.id.clearPreferenceButton);
        final Button button = findViewById(R.id.easterButton);
        final ImageView imgButton = findViewById(R.id.easterImage1);

        clearPrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(thisApp)
                        .setMessage(R.string.clear_all_preferences_titel)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.clear();
                                        editor.apply();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .create()
                        .show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (imgButton.getVisibility() == View.VISIBLE) {
                    imgButton.setVisibility(View.GONE);
                }
                else {
                    imgButton.setVisibility(View.VISIBLE);
                }
            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(thisApp)
                        .setMessage("BeeDoo BeeDoo BeeDoo")
                        .setNeutralButton("okay :D",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .create()
                        .show();
            }
        });

        findViewById(R.id.easterButton).setVisibility(View.GONE);
        findViewById(R.id.easterImage1).setVisibility(View.GONE);





        //get ALL the preferences used on this page
        final String personalGreeting = sharedPref.getString("pref_personal_greeting_string", "Willkommen!");


        if (personalGreeting.equals("egg")) {
            findViewById(R.id.easterButton).setVisibility(View.VISIBLE);
        }



    }

}
