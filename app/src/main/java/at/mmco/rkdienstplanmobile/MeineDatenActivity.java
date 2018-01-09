package at.mmco.rkdienstplanmobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import at.mmco.rkdienstplanmobile.User;


public class MeineDatenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context thisApp = this;

    SwipeRefreshLayout mySwipeRefreshLayout;
    SharedPreferences sharedPref;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    String fullUserName;
    String userUsername;
    String personalId;
    User user = new User();
    boolean unreadBulletinPostsAvailable;

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
        navigationView.setCheckedItem(R.id.nav_info);
    } //sets the checked toolbar item. Item has to be manually changed, depending on the activity
    private void writeUserFullNameToDatabase (User user){
        String username = user.getUsername();
        database.child("users").child(username).child("fullName").setValue(user.getFullName());
    }
    private void writePersonalIdToDatabase (User user){
        String username = user.getUsername();
        database.child("users").child(username).child("personalId").setValue(user.getPersonalId());
    }

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
                .findItem(R.id.nav_info)  //change this to current menu id!
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_bulletin)
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
    } //highlights the current icon & the bulletin icon
    private void updateRegistrationInfo(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        TextView registrationInfo = header.findViewById(R.id.navHeader_description);
        if (!fullUserName.equals("NULL")) {
            registrationInfo.append(" " + fullUserName);
        } else if (!userUsername.equals("NULL")) {
            registrationInfo.append(" " + userUsername);
        }
    } //updates the nav header description with the name of the user

    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void parseFullNameFromHTML(final String html) {
            Matcher nameMatcher = Pattern.compile("(?<=<br>\n\t\t)[\\s\\S]*?(?=</div>)").matcher(html);
            if (nameMatcher.find()) {
                fullUserName = (nameMatcher.group(0));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user_full_name", fullUserName);
                editor.apply();
                user.setFullName(fullUserName);
                database.child("users").child(userUsername).child("fullName").setValue(fullUserName);
                writeUserFullNameToDatabase(user);
            }
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void parsePersonalIdFromHTML (final String html) {
            Matcher personalIdMatcher = Pattern.compile("(?<!\\d)\\d{8}(?!\\d)").matcher(html);
            if (personalIdMatcher.find()){
                personalId = (personalIdMatcher.group(0));

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user_personal_id", personalId);
                editor.apply();
                user.setPersonalId(personalId);
                database.child("users").child(userUsername).child("personalId").setValue(personalId);
                //writePersonalIdToDatabase(user);
            }
        }
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        fullUserName = sharedPref.getString("user_full_name", "NULL");
        userUsername = sharedPref.getString("user_username","NULL");
        personalId = sharedPref.getString("user_personal_id", "NULL");
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);

        user.setUsername(userUsername);

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
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        //Set the action for pull down to refresh.
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.setVisibility(View.GONE);
                mWebView.reload();
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final Boolean allowInfoChangePref = sharedPref.getBoolean("pref_allow_info_change", true);

        //Hier ist das Javascript, das das Erscheinungsbild der Seite modifiziert.
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                String currentUrl = mWebView.getUrl();

                mWebView.loadUrl("javascript:(function() { " +
                        "document.getElementById('contentmain').style.width='90vw';" +
                        "document.getElementById('redline').style.display='none';" +
                        "document.getElementById('header').style.display='none';" +
                        "document.getElementById('mainnaviwrap').style.display='none';" +
                        "document.getElementById('vnavi').style.display='none';" +
                        "document.getElementById('footer').style.display='none'; " +
                        "})()");
                if (!allowInfoChangePref){
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.getElementById('editAddress').style.display='none';" +
                            "document.getElementById('cancelAddressEditing').style.display='none';" +
                            "document.getElementById('saveAddressEditing').style.display='none';" +
                            "document.getElementById('editContacts').style.display='none';" +
                            "document.getElementById('cancelContactEditing').style.display='none';" +
                            "document.getElementById('saveContactEditing').style.display='none';" +
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

                if (fullUserName.equals("NULL")) {
                    mWebView.loadUrl("javascript:window.HTMLOUT.parseFullNameFromHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
                if (personalId.equals("NULL")) {
                    mWebView.loadUrl("javascript:window.HTMLOUT.parsePersonalIdFromHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }

            }
        });


        mWebView.setVisibility(View.GONE);
        mySwipeRefreshLayout.setRefreshing(true);
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Home/MeineDaten"); //Lad die aktuelle Seite

        //FloatingActionButton fab = findViewById(R.id.fab);
        findViewById(R.id.fab).setVisibility(View.GONE);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
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
            finish(); //beendet die aktuelle Activity um lange history zu vermeiden
        } else if (id == R.id.nav_info) {
            //Nichts machen, da diese Activity bereits ausgeführt wird.
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

