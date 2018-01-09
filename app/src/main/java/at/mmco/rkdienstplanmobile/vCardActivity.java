package at.mmco.rkdienstplanmobile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vCardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout mySwipeRefreshLayout;
    SharedPreferences sharedPref;
    boolean unreadBulletinPostsAvailable;
    FloatingActionButton phoneFAB;
    //WebView mWebView;
    String fullUserName;
    String userSurname;
    String currentPhoneNumber = "$number$";
    String currentVCardName = "$name$";
    final Context thisApp = this;


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
        navigationView.setCheckedItem(R.id.nav_vcards);
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
                .findItem(R.id.nav_vcards)  //change this to current menu id!
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
    private boolean callParsePhoneNumberHandler() {
        final WebView mWebView = findViewById(R.id.webview);
        Handler delayHandler = new Handler();
        for (int i = 0; i < 10; i++) {
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:window.HTMLOUT.parseHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            }, 100);
        }
        return true;
    }
    String getCurrentPhoneNumber(){
        while (true){
            if (callParsePhoneNumberHandler()) break;
        }
        return currentPhoneNumber;
    }
    String getCurrentVCardName(){
        while (true){
            if (callParsePhoneNumberHandler()) break;
        }
        return  currentVCardName;
    }
    private void addPageFunctionality(WebView mWebView){
        mWebView.loadUrl("javascript:" +
                "$(function () {"+
                "instance = new DiversysScript.Vcards();"+
                "});"
        );
        mWebView.loadUrl("javascript:" +
                "$(function () {" +
                "$(\"#quickmenu select\").change(" +
                "function () {" +
                "if ($(this).find(\"option:selected\").attr(\"value\") != \"\")" +
                "window.location = $(this).find(\"option:selected\").attr(\"value\");" +
                "}" +
                ");" +
                "});" +
                "});"
        );
    }

    public class vCardWebViewClient extends WebViewClient {
        private final WeakReference<Activity> mActivityRef;


        private vCardWebViewClient(Activity activity) {
            mActivityRef = new WeakReference<Activity>(activity);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public void onPageFinished(WebView view, String url) {
            WebView mWebView = findViewById(R.id.webview);
            String currentUrl = mWebView.getUrl();


            //while(!addPhoneHyperlink()){} //verlässlich warten bis die Funktion fertig ist.

            mWebView.loadUrl("javascript:(function() { " +

                    "document.body.innerHTML = document.body.innerHTML.replace('border:1px #444 solid;width:532px;', 'border:none;width:330px;');" +

                    "document.getElementById('contentmain').style.width='90vw';" +
                    "document.getElementById('redline').style.display='none';" +
                    "document.getElementById('header').style.display='none';" +
                    "document.getElementById('mainnaviwrap').style.display='none';" +
                    "document.getElementById('vnavi').style.display='none';" +
                    "document.getElementById('footer').style.display='none';" +

                    "document.getElementById('_photo').style.position='relative';"+
                    "document.getElementById('_photo').style.top='-40px';"+

                    "document.getElementById('_title').style.position='relative';"+
                    "document.getElementById('_title').style.right='105px';"+
                    "document.getElementById('_title').style.top='50px';"+

                    "document.getElementById('_name').style.position='relative';"+
                    "document.getElementById('_name').style.right='105px';"+
                    "document.getElementById('_name').style.top='50px';"+

                    "document.getElementById('_department').style.position='relative';"+
                    "document.getElementById('_department').style.right='105px';"+
                    "document.getElementById('_department').style.top='40px';"+

                    "document.getElementById('_contacts').style.position='relative';"+
                    "document.getElementById('_contacts').style.right='105px';"+
                    "document.getElementById('_contacts').style.top='40px';"+

                    "document.getElementById('_vcf').style.display='none';" +

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

            callParsePhoneNumberHandler();
            addPageFunctionality(mWebView); //call all page scripts after modifying the sourcecode

            currentPhoneNumber = getCurrentPhoneNumber();
            currentVCardName = getCurrentVCardName();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                final Activity mailActivity = mActivityRef.get();
                if (mailActivity != null) {
                    MailTo mt = MailTo.parse(url);
                    Intent i = newEmailIntent(mailActivity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                    mailActivity.startActivity(i);
                    view.reload();
                    return true;
                }
            }else if (url.startsWith("tel:")){
                final Activity telActivity = mActivityRef.get();
                if (telActivity != null){
                    Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(tel);
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_CC, cc);
            intent.setType("message/rfc822");
            return intent;
        }
    } //This handles page design, urlOverrides and mailTOs
    public class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void parseHTML(final String html) { //FIXME
//            Matcher nameMatcher = Pattern.compile("(vcard_name.>[^<])[\\s\\S]*?(?=</span>)").matcher(html); //FIXME
//           Matcher nameMatcher = Pattern.compile("(?<=Abmelden</a> )[\\s\\S]*?(?=</li>)").matcher(html); //matches "(n.surname)"

            //NOTE: Wenn im Design der Seite was verändert wird, muss das Pattern hier auch angepasst werden!!!
            Matcher nameMatcher = Pattern.compile("(?<=<span id=\"_name\" class=\"vcard_name\" style=\"position: relative; right: 105px; top: 50px;\">)(.*?)(?=</span>)").matcher(html);
            if (nameMatcher.find()) {
                currentVCardName = (nameMatcher.group(0));
                //Toast.makeText(thisApp,  currentVCardName, Toast.LENGTH_SHORT).show();   //#DEBUG

            }

            Matcher phoneNumberMatcher = Pattern.compile("(\\+\\d{7,})").matcher(html);
            if (phoneNumberMatcher.find()) {
                currentPhoneNumber = (phoneNumberMatcher.group(0));
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);
        fullUserName = sharedPref.getString("user_full_name", "NULL");
        userSurname = sharedPref.getString("user_username","NULL");
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
        //noinspection AndroidLintClickableViewAccessibility
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                callParsePhoneNumberHandler();
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        mWebView.addJavascriptInterface(new vCardActivity.MyJavaScriptInterface(), "HTMLOUT");

        mWebView.setWebViewClient(new vCardWebViewClient(this));

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });


        mWebView.setVisibility(View.GONE); //is set to VISIBLE in onPageFinished in vCardWebViewClient
        mySwipeRefreshLayout.setRefreshing(true);
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at/Department/Vcards"); //Lad die aktuelle Seite


        phoneFAB = findViewById(R.id.fab);
        phoneFAB.setVisibility(View.VISIBLE);
        phoneFAB.setImageResource(R.drawable.ic_fab_phone_24dp);
        phoneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPhoneNumber = getCurrentPhoneNumber();
                currentVCardName = getCurrentVCardName();
                while(true){
                    if (callParsePhoneNumberHandler()) break;
                }
                if (!currentVCardName.equals("$name$") && !currentVCardName.equals("")) {
                    new AlertDialog.Builder(thisApp)
                            .setTitle(getCurrentVCardName() + " anrufen?")
                            .setMessage("Telefon öffnen und " + getCurrentPhoneNumber() + " wählen?")
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                                            phoneIntent.setData(Uri.parse("tel:" + currentPhoneNumber));
                                            startActivity(phoneIntent);
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //result.cancel();
                                        }
                                    })
                            .create()
                            .show();
                }
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
            Intent bulletin = new Intent(this, BulletinActivity.class);
            startActivity(bulletin);
            finish();
        } else if (id == R.id.nav_vcards) {
            //do nothing
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