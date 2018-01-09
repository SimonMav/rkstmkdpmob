package at.mmco.rkdienstplanmobile;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Locale;

public class ContributeActivity extends AppCompatActivity implements RewardedVideoAdListener{

    TextView rewardText;
    Button adButton;
    TextView scoreValue;
    RewardedVideoAd myVideoAd;
    SharedPreferences sharedPref; //set preferences as global var
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    int score;
    User user;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init all used views
        rewardText = findViewById(R.id.RewardText);
        scoreValue = findViewById(R.id.scoreValue);
        adButton = findViewById(R.id.adButton);
        adButton.setEnabled(false);


        //initialize the Preferences in onCreate
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //init the score variable with the stored value (0 if value does not exist)
        score = sharedPref.getInt("user_ad_score", 0);

        scoreValue.setText(String.format("%d", score));



        MobileAds.initialize(this,"ca-app-pub-4602622106790117~4457278747");
        myVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        myVideoAd.setRewardedVideoAdListener(this);

        AdView myBannerAd = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3FFB05594D6789E7C4BB8CD4C1D9F7A4") //THIS IS JUST MY ONW DEVICE
                .build();
        myBannerAd.loadAd(adRequest);


        loadVideoAd();

        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adButton.setEnabled(false);
                if (myVideoAd.isLoaded()){
                    myVideoAd.show();
                }
            }
        });
    }

    public void loadVideoAd(){
        //USE THIS ID for ADs:
        //RealAD-ID: ca-app-pub-4602622106790117/2031260109
        //TestAd-ID: ca-app-pub-3940256099942544/5224354917
        myVideoAd.loadAd("ca-app-pub-4602622106790117/2031260109", new AdRequest.Builder()
                .addTestDevice("3FFB05594D6789E7C4BB8CD4C1D9F7A4") //THIS IS JUST MY OWN DEVICE FOR TESTING
                .build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {
        rewardText.append("Video wurde geladen!\n");
        adButton.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        rewardText.append("An Ad has opened!\n");
    }

    @Override
    public void onRewardedVideoStarted() {
        rewardText.append("An Ad has started!\n");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //rewardText.append("An Ad has closed!\n");
        loadVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        rewardText.setText(String.format(Locale.getDefault(),"%d %s erhalten!\n", rewardItem.getAmount(),rewardItem.getType()));
        score += 1;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_ad_score", score);
        editor.apply();
        database.child("users").child(sharedPref.getString("user_username", "NULL")).child("adRewardCounter").setValue(score);
        scoreValue.setText(String.format("%d", score));

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //rewardText.append("An Ad has caused focus to leave the app!\n");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        rewardText.append("An Ad has failed to load! How sad!\n");
    }


    //TODO: Save Variables onDestroy
}
