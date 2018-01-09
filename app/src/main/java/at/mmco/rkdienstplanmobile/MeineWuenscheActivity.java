package at.mmco.rkdienstplanmobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class MeineWuenscheActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    final Context thisApp = this;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ScrollView myScrollView;
    WebView mWebView;
    SharedPreferences sharedPref;
    boolean unreadBulletinPostsAvailable;
    String fullUserName;
    String userSurname;

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
        makeAllMenuItemsGray(navigationView);
        updateRegistrationInfo();
        if (unreadBulletinPostsAvailable){
            highlightBulletinMenuItem();
        }

    }   //creates the Toolbar
    private void setCheckedToolbarItem(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_wishes);
    } //sets the checked toolbar item. Item has to be manually changed, depending on the activity
    private void makeAllMenuItemsGray(NavigationView navigationView){
        navigationView.getMenu()
                .findItem(R.id.nav_start)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_info)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_wishes)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_functions)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_trainings)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_holidays)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_stats)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_bulletin)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_vcards)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_contribute)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_about)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_logoff)
                .getIcon()
                .setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_ATOP);
    } //TODO: ADD NEW MENU ITEMS!
    public void highlightBulletinMenuItem(){
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setItemIconTintList(null);

        navigationView.getMenu()
                .findItem(R.id.nav_wishes)  //change this to current menu id!
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_bulletin)
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
    }
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
    private void modifyPageDesign(){
        mWebView.loadUrl("javascript:(function(){ " +
                "var dutyList = document.getElementById('rosterGrid').getElementsByClassName('_selectDuty');" +
                "for (var i=0; i < dutyList.length; i++){ dutyList[i].innerHTML ='';}" +
                "})()");
    }
    private void scrollPopUpWindow() {
        Handler delayHandler = new Handler();
        for (int i = 0; i < 10; i++) {
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:(function(){ " +
                            "var dialog = document.getElementsByClassName('ui-dialog ui-widget ui-widget-content ui-corner-all ui-front ui-dialog-buttons ui-draggable');" +
                            "dialog[0].style.top = '" + ((myScrollView.getScrollY() * pixelDifferenceFactor()) + 200) + "px';" +
                            "dialog[0].style.left = '0px';" +
                            "})()");
                }
            }, 1000);
        }
    }
    public float pixelDifferenceFactor () {
        float scrollViewMaxScrollAmount = myScrollView.getMaxScrollAmount();
        float webViewContentLength = mWebView.getContentHeight();
        return scrollViewMaxScrollAmount / webViewContentLength;
    }//----Calculate the difference per pixel between scrollView & webView---

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final Boolean fabDisplayPref = sharedPref.getBoolean("pref_wishes_display_fab", true);
        final Boolean dutyWishesDisplayPref = sharedPref.getBoolean("pref_display_duty_wishes", false);
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);
        fullUserName = sharedPref.getString("user_full_name", "NULL");
        userSurname = sharedPref.getString("user_username","NULL");

        //Don't use activity_main layout
        setContentView(R.layout.activity_meine_wuensche);
        //setContentView(R.layout.activity_main); //why not?
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        myScrollView = findViewById(R.id.scrollview);




        //-----Start Toolbar-----
        createToolbar();
        setCheckedToolbarItem();
        //-----End Toolbar-----

        //-----Start Webview-----

        mWebView = findViewById(R.id.webview);

        //This prevents the webview being scrolled (vertikal und hoizontal, is SCHEIßE, aber geht...)
        //Durch den vertikalen scrollview ist vertikales scrollen aber möglich.
        //noinspection AndroidLintClickableViewAccessibility
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollPopUpWindow();
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

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url)
            {
                String currentUrl = mWebView.getUrl();

                //Hier ist das Javascript, das das Erscheinungsbild der Seite modifiziert.
                mWebView.loadUrl("javascript:(function() { " +
                        "document.getElementById('contentmain').style.width='90vw';" +
                        "document.getElementById('redline').style.display='none';" +
                        "document.getElementById('header').style.display='none';" +
                        "document.getElementById('mainnaviwrap').style.display='none';" +
                        "document.getElementById('vnavi').style.display='none';" +
                        "document.getElementById('footer').style.display='none'; " +
                        //"document.body.innerHTML = document.body.innerHTML.replace('Woche:', 'Woche ab:');" +
                        "document.getElementById('_week').style.width='185px';"+
                        "})()");

                //Wenn es in den Einstellungen so gewünscht wird, sollen die eingetragen Wünsche nicht angezeigt werden.
                if (!dutyWishesDisplayPref){
                    mWebView.loadUrl("javascript:(function() { " +
                            "document.getElementById('contentmain').getElementsByTagName('h2')[0].outerHTML = '';" +
                            "document.getElementById('preferenceGrid_wrapper').style.display='none';" +
                            "})()");
                }

                //Den Link zum Passwort ändern entfernen. hier .contains() und nicht .equals() weil es verschiedene
                //returnUrl paths gibt.
                if(currentUrl != null && currentUrl.contains("/Login")){
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<p><a[\\s\\S]+?<\\/p>)/, '');" +
                            "})()");
                }

                /* THIS SHIT IS NOT YET WORKING. FIXME
                mWebView.loadUrl("javascript:"+
                        "$(function () {"+
                            "$('#quickmenu select').change("+
                                "function () {"+
                                    "if ($(this).find('option:selected').attr('value') != '')"+
                                        "window.location = $(this).find('option:selected').attr('value');"+
                                "}"+
                            ");"+
                        "});"
                );
                */

                mWebView.setVisibility(View.VISIBLE);
                mySwipeRefreshLayout.setRefreshing(false);

            }
        });


        mWebView.setVisibility(View.GONE);
        mySwipeRefreshLayout.setRefreshing(true);
        //TODO: Versuche die Einblendung der Tastatur beim DatePicker zu unterbinden.
        //TODO: Dienst wünschen Dialog öffnet sich in der mitte der Seite, mach da was!!!
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Home/MeineWuensche"); //Lad die aktuelle Seite


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_fab_format_clear_black_24dp);
        if (!fabDisplayPref) {
            findViewById(R.id.fab).setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mWebView.scrollTo(0,0);
                modifyPageDesign();
            }
        });

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
            finish(); //beendet die aktuellen Activities um lange History zu vermeiden
        } else if (id == R.id.nav_info) {
            Intent daten = new Intent(this, MeineDatenActivity.class);
            startActivity(daten);
            finish();
        } else if (id == R.id.nav_wishes) {
            //Nichts machen, da diese Activity bereits ausgeführt wird.
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
            Intent bulletin = new Intent(this, BulletinActivity.class);
            startActivity(bulletin);
            finish();
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
