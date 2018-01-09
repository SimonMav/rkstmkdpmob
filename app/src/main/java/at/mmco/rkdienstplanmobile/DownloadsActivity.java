//TODO MAKE THIS SHIT WORK CORRECTLY!
package at.mmco.rkdienstplanmobile;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


public class DownloadsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout mySwipeRefreshLayout;

    SharedPreferences sharedPref;
    boolean unreadBulletinPostsAvailable;
    String userSurname;
    String fullUserName;


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
        navigationView.setCheckedItem(R.id.nav_downloads);
    } //sets the cheched toolbar item. Item has to be manually changed, depending on the activity
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
                .findItem(R.id.nav_downloads)  //change this to current menu id!
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

    //FIXME TEST
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //FIXME TEST


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final Boolean showTrainingDatesPref = sharedPref.getBoolean("pref_trainings_display_dates", false);
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);
        userSurname = sharedPref.getString("user_username", "NULL");
        fullUserName = sharedPref.getString("user_full_name", "NULL");

        setContentView(R.layout.activity_main);
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //-----Start Toolbar-----
        createToolbar();
        setCheckedToolbarItem();
        //-----End Toolbar-----

        //-----Start Webview-----

        final WebView mWebView = (WebView) findViewById(R.id.webview);

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

        //FIXME TEST

        final boolean hasWriteStoragePermission = isWriteStoragePermissionGranted();
        //boolean hasReadStoragePermission = isReadStoragePermissionGranted();

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (hasWriteStoragePermission) {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (dm != null) {
                        dm.enqueue(request);
                    }
                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Berechtigungen fehlen!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //FIXME TEST

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

                if (!showTrainingDatesPref) {
                    mWebView.loadUrl("javascript:(function() { " +
                            "var startDateList = document.getElementById('contentmain').getElementsByClassName('datum');" +
                            "for (var i=0; i < startDateList.length; i++){ startDateList[i].outerHTML ='';}" +
                            "var endDateList = document.getElementById('contentmain').getElementsByClassName('datum');" +
                            "endDateList[0].innerHTML='';" +
                            "for (var i=1; i <= endDateList.length; i++){ endDateList[i].innerHTML ='•';}" +
                            "})()");
                }

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
        //mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Home/MeineAusbildungen"); //Lad die aktuelle Seite

        //debug TEST
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Download/Dienstplaene");
        //---

        findViewById(R.id.fab).setVisibility(View.GONE);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
            //Nichts machen, da diese Activity bereits ausgeführt wird.
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
            WebView mWebView = (WebView) findViewById(R.id.webview);
            mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Account/Logoff");
            Intent start = new Intent(this, MainActivity.class);
            startActivity(start);
            finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
