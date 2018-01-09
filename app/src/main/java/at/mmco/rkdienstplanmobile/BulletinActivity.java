package at.mmco.rkdienstplanmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
/*
Use those if you want some preference items to change stuff on the page:

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
 */


public class BulletinActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout mySwipeRefreshLayout;
    String userSurname;
    String fullUserName;
    boolean unreadBulletinPostsAvailable;
    SharedPreferences sharedPref;

    private void createToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateRegistrationInfo();
    }   //creates the Toolbar
    private void setCheckedToolbarItem(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_bulletin);
    } //sets the checked toolbar item. Item has to be manually changed, depending on the activity
    private void updateRegistrationInfo(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        TextView registrationInfo = header.findViewById(R.id.navHeader_description);
        if (!fullUserName.equals("NULL")) {
            registrationInfo.append(" " + fullUserName);
        } else if (!userSurname.equals("NULL")) {
            registrationInfo.append(" " + userSurname);
        }
    } //updates the nav header description with the name of the user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userSurname = sharedPref.getString("user_surname", "NULL");
        fullUserName = sharedPref.getString("user_full_name", "NULL");
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);

        setContentView(R.layout.activity_main);
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //-----Start Toolbar-----
        createToolbar();
        setCheckedToolbarItem();
        //-----End Toolbar-----

        //-----Start Webview-----

        final WebView mWebView = findViewById(R.id.webview);

        //This prevents the webview being scrolled (vertikal und hoizontal, is SCHEIßE, aber geht...)
        //Durch den vertikalen scrollview ist vertikales scrollen aber möglich.
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        //Set the action for pull down to refresh.
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Hier ist das Javascript, das das Erscheinungsbild der Seite modifiziert.
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                String currentUrl = mWebView.getUrl();

                mWebView.loadUrl("javascript:(function() { " +
                        "document.getElementById('contentmain').style.width='90vw';" +
                        "document.getElementById('redline').style.display='none';" +
                        "document.getElementById('header').style.display='none';" +
                        "document.getElementById('mainnaviwrap').style.display='none';" +
                        "document.getElementById('vnavi').style.display='none';" +
                        "document.getElementById('footer').style.display='none'; " +
                        "})()");

                //Den Link zum Passwort ändern entfernen. hier .contains() und nicht .equals() weil es verschiedene
                //returnUrl paths gibt.
                if(currentUrl != null && currentUrl.contains("/Login")){
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<p><a[\\s\\S]+?<\\/p>)/, '');" +
                            "})()");
                }

                mWebView.setVisibility(View.VISIBLE);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });

        mWebView.setVisibility(View.GONE);
        mySwipeRefreshLayout.setRefreshing(true);
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Information/SchwarzesBrett"); //Lad die aktuelle Seite
        findViewById(R.id.fab).setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent einstellungen = new Intent(this, SettingsActivity.class);
            startActivity(einstellungen);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start) {
            finish(); //beendet die aktuelle Activity und kehrt automatisch zum logical parent zurück.
        } else if (id == R.id.nav_info) {
            Intent daten = new Intent(this, MeineDatenActivity.class);
            startActivity(daten);
            finish();
        } else if (id == R.id.nav_wishes) {
            Intent wuensche = new Intent(this, MeineWuenscheActivity.class);
            startActivity(wuensche);
            finish();
        } else if (id == R.id.nav_functions) {
            Intent funktionen = new Intent(this, MeineFunktionenActivity.class);
            startActivity(funktionen);
            finish();
        } else if (id == R.id.nav_trainings) {
            Intent ausbildungen = new Intent(this, MeineAusbildungenActivity.class);
            startActivity(ausbildungen);
            finish();
        } else if (id==R.id.nav_holidays){
            Intent urlaube = new Intent(this, MeineUrlaubeActivity.class);
            startActivity(urlaube);
            finish();
        } else if (id == R.id.nav_stats) {
            Intent statistik = new Intent(this, MeineStatistikActivity.class);
            startActivity(statistik);
            finish();
        } else if (id == R.id.nav_bulletin) {
            //Nichts machen, da diese Activity bereits ausgeführt wird.
        } else if (id == R.id.nav_vcards) {
            Intent vcard = new Intent(this, vCardActivity.class);
            startActivity(vcard);
            finish();
        } else if (id == R.id.nav_contribute) {
            Intent contribute = new Intent(this, ContributeActivity.class);
            startActivity(contribute);
            finish();
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
            finish();
        } else if (id == R.id.nav_logoff) {
            WebView mWebView = findViewById(R.id.webview);
            mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Account/Logoff");
            Intent start = new Intent(this, MainActivity.class);
            startActivity(start);
            finishAffinity();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
