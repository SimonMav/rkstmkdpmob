package at.mmco.rkdienstplanmobile;

//import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.mmco.rkdienstplanmobile.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context thisApp = this;
    SwipeRefreshLayout mySwipeRefreshLayout;  //Set the Swipe Layout as global var
    User user = new User(); //create empty user object.

    private void createToolbar() {
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

    }   //creates the Toolbar

    private void updateRegistrationInfo() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView registrationInfo = header.findViewById(R.id.navHeader_description);
        if (!fullUserName.equals("NULL")) {
            registrationInfo.append(" " + fullUserName);
        } else if (!userUsername.equals("NULL")) {
            registrationInfo.append(" " + userUsername);
        }
    } //updates the nav header description with the name of the user

    private void setCheckedToolbarItem() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_start);
    } //sets the checked toolbar item. Item has to be manually changed, depending on the activity

    private void makeNotification(String title, String content) {
        Notification myNotification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_local_hospital_24dp)
                .build();
        // Sets an ID for the notification
        int mNotificationId = 2;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, myNotification);
        }
    }

    private void makeAllMenuItemsGray(NavigationView navigationView) {
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

    private void highlightBulletinMenuItem() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setItemIconTintList(null);

        navigationView.getMenu()
                .findItem(R.id.nav_start)  //change this to current menu id!
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
        navigationView.getMenu()
                .findItem(R.id.nav_bulletin)
                .getIcon()
                .setColorFilter(Color.parseColor("#B41218"), PorterDuff.Mode.SRC_ATOP);
    }

    private void setUpFirebaseRemoteConfiguration() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG) //TODO Change this to false for release
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(0) //TODO Change this to 3600 for release!
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setNavHeaderEventText() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView navHeaderText = header.findViewById(R.id.navHeader_title);
        navHeaderText.setText(mFirebaseRemoteConfig.getString("event_nav_header_title_text"));
    }

    private void showMessageUpdateAvailable() {
        new AlertDialog.Builder(thisApp)
                .setTitle(R.string.update_available_alert_title)
                .setMessage(R.string.update_available_alert_content)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=at.mmco.rkdienstplanmobile")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=at.mmco.rkdienstplanmobile")));
                                }
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    private void showIntroductionMessage() {
        View checkBoxView = View.inflate(this, R.layout.checkbox, null);
        CheckBox checkBox = checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showIntroductionMessagePref = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("show_introduction_message", showIntroductionMessagePref);
                editor.apply();
            }
        });
        checkBox.setText(R.string.dont_show_again);
        new AlertDialog.Builder(thisApp)
                .setTitle(R.string.introduction_alert_title)
                .setMessage(R.string.introduction_alert_content)
                .setView(checkBoxView)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .create()
                .show();
    }

    private void showRemoteMessage() {
        new AlertDialog.Builder(thisApp)
                .setTitle(mFirebaseRemoteConfig.getString("info_popup_title"))
                .setMessage(mFirebaseRemoteConfig.getString("info_popup_text"))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create()
                .show();
    } //shows a remote message

    /*private String getUsernameFromHTML(WebView mWebView) {
        // TODO: 14.12.17 check if this is fast enough
        mWebView.loadUrl("javascript:window.HTMLOUT.parseUsername('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"); // TODO: 14.12.17 Try getElementById('footer')
        return userUsername;
    } */

    private void writeUserToDatabase (User user){
        String username = user.getUsername();
        database.child("users").child(username).setValue(user);
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            Log.d("alert", message);
            Toast.makeText(thisApp, message, Toast.LENGTH_LONG).show();
            result.confirm();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(thisApp)
                    //.setTitle("WILLST DU WIRKLICH EINEN TITEL??")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                    makeNotification("Dienstwunsch erfolgreich eingetragen.", "Ruhigen Dienst!");
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.cancel();
                                }
                            })
                    .create()
                    .show();
            return true;
        }
    } //WebChrome class for using confirm() and alert()

    class MyJavaScriptInterface {  //put all functions here that should be call from the injected javascript
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void parseHTML(final String html) {

            if (html.contains("Neue Einträge am schwarzen Brett")) {
                unreadBulletinPostsAvailable = true;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("unread_bulletin_posts_available", unreadBulletinPostsAvailable);
                editor.apply();
                highlightBulletinMenuItem();
            } else {
                unreadBulletinPostsAvailable = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("unread_bulletin_posts_available", unreadBulletinPostsAvailable);
                editor.apply();
            }

            /*
            if (fullUserName.equals("NULL") && userSurname.equals("NULL")) { //nur wenn kein userName vorhanden ist.
                Matcher firstMatcher = Pattern.compile("(?<=Abmelden</a> )[\\s\\S]*?(?=</li>)").matcher(html); //matches "(n.surname)"
                if (firstMatcher.find()) {
                    String userNameInBrackets = (firstMatcher.group(0));
                    Matcher secondMatcher = Pattern.compile("(?<=\\.).*?(?=\\))").matcher(userNameInBrackets); //matches surname
                    if (secondMatcher.find()) {
                        userSurname = (secondMatcher.group(0));
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user_username", userUsername);
                        editor.apply();
                    }
                }
                sendFcmTokenToDatabase(userSurname); //update the database with the new name.
            } */
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void parseUsername(final String html) {


            Matcher firstMatcher = Pattern.compile("(?<=Abmelden</a> )[\\s\\S]*?(?=\n *</li>)").matcher(html); //matches "(n.surname)"
            if (firstMatcher.find()) {
                String userNameInBrackets = (firstMatcher.group(0));
                String finalUsername = userNameInBrackets.replace(".", "_").replace("(", "").replace(")", "");
                //change "(n.surname)" to "n_surname"
                finalUsername = finalUsername.toLowerCase(); //because u.sername is as valid as U.sErnAmE
                userUsername = finalUsername;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user_username", userUsername);
                editor.apply();

                user.setUsername(finalUsername); //set the username in the user object
                writeUserToDatabase(user);
            }
        }
    }


    String userUsername;
    String fullUserName;
    String personalId;
    boolean unreadBulletinPostsAvailable;
    boolean showIntroductionMessagePref;
    SharedPreferences sharedPref;
    int appOpeningCounter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call the super function


        //get ALL the preferences used on this page
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final Boolean maintenanceDisplayPref = sharedPref.getBoolean("pref_display_maintenance", true);
        final Boolean recertificationDisplayPref = sharedPref.getBoolean("pref_display_recertification", true);
        final Boolean upcomingDutiesDisplayPref = sharedPref.getBoolean("pref_display_upcoming_duties", true);
        final Boolean openDutiesDisplayPref = sharedPref.getBoolean("pref_display_open_duties", true);
        final Boolean eventsDisplayPref = sharedPref.getBoolean("pref_display_events", true);
        final Boolean birthdayDisplayPref = sharedPref.getBoolean("pref_display_birthdays", true);
        final Boolean enrolledEventsDisplayPref = sharedPref.getBoolean("pref_display_enrolled_events", true);
        final Boolean personalGreetingDisplayPref = sharedPref.getBoolean("pref_display_personal_greetings", false);
        final String personalGreeting = sharedPref.getString("pref_personal_greeting_string", "Willkommen!");
        showIntroductionMessagePref = sharedPref.getBoolean("show_introduction_message", true);
        userUsername = sharedPref.getString("user_username", "NULL");
        fullUserName = sharedPref.getString("user_full_name", "NULL");
        personalId = sharedPref.getString("user_personal_id", "NULL");
        unreadBulletinPostsAvailable = sharedPref.getBoolean("unread_bulletin_posts_available", false);
        appOpeningCounter = sharedPref.getInt("app_opening_counter", 0);

        //increase the counter and save it to  the sharedPrefs
        appOpeningCounter += 1; //increment the counter, save it for later
        if (appOpeningCounter > 10000000){
            appOpeningCounter = 0;
        } //make sure no overflow happens (weil eh wer die app mehr als 10Mio mal aufmachen wird!)
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("app_opening_counter", appOpeningCounter);
        editor.apply();

        /* //___START___
        int appOpeningCounter = sharedPref.getInt("app_opening_counter", 0);
        if (appOpeningCounter % 3 == 0) {
        CustomDialogClass cdd = new CustomDialogClass(MainActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
        } else {
        CustomDialogShortClass cdsd = new CustomDialogShortClass(MainActivity.this);
        cdsd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdsd.show();
        }
        //  TODO: Set frequency of PopUp to a good value (use 'else if (appOpeningCounter%int==0)') for second Dialog
        //___END___ */


        setContentView(R.layout.activity_main);  //initialize the content view

        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout); //initialize the (global) Swipe Layout

        createToolbar();

        setCheckedToolbarItem();

        user.setFcmToken(FirebaseInstanceId.getInstance().getToken()); //set the fcm token value in the user object
        if (!fullUserName.equals("NULL")){
            user.setFullName(fullUserName);
        }
        if (!userUsername.equals("NULL")){
            user.setUsername(userUsername);
        }
        if (!personalId.equals("NULL")){
            user.setPersonalId(personalId);
        }
        user.setAdRewardCounter(sharedPref.getInt("user_ad_score", 0));
        user.setAppOpeningCounter(appOpeningCounter);

        setUpFirebaseRemoteConfiguration();

        if (mFirebaseRemoteConfig.getBoolean("show_event_nav_header_title")) {
            setNavHeaderEventText();
            //Hier kann noch mehr event related stuff passieren!
        }
        if (showIntroductionMessagePref){
            showIntroductionMessage();
        }

        if (BuildConfig.VERSION_CODE < mFirebaseRemoteConfig.getDouble("min_apk_version_code")) {
            showMessageUpdateAvailable();
        }

        if (mFirebaseRemoteConfig.getBoolean("show_info_popup")) {
            showRemoteMessage();
        }


        final WebView mWebView = findViewById(R.id.webview);

        //This prevents the webview being scrolled
        //Durch den vertikalen scrollview ist vertikales scrollen aber möglich.
        //noinspection AndroidLintClickableViewAccessibility
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        //Set the action for pull down to refresh.
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });

        //set the fab
        final FloatingActionButton fab = findViewById(R.id.fab);


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){

                Toast.makeText(thisApp, "Seite lädt...",
                        Toast.LENGTH_SHORT).show();

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {

                String currentUrl = mWebView.getUrl();

                //modifyPageDesignMAIN(mWebView); //TODO: Outsource all the javaScript to an external function

                //parse the full page as HTML to a string in the Javascript interface
                mWebView.loadUrl("javascript:window.HTMLOUT.parseHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                mWebView.loadUrl("javascript:window.HTMLOUT.parseUsername('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"); // TODO: 14.12.17 Try getElementById('footer')


                mWebView.loadUrl("javascript:(function() { " +
                        "document.getElementById('contentmain').style.width='90vw';" +
                        "document.getElementById('redline').style.display='none';" +
                        "document.getElementById('header').style.display='none';" +
                        "document.getElementById('mainnaviwrap').style.display='none';" +
                        "document.getElementById('vnavi').style.display='none';" +
                        "document.getElementById('footer').style.display='none'; " +
                        "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Neue Einträge am schwarzen Brett') > -1) {"+
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<h2>Neue Einträge[\\s\\S]+?<\\/ul>)/, '');" +
                        "}" +
                        "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Termine') > -1) {" +
                            "document.getElementById('calendars').style.display='none';" +
                            "document.getElementById('appointmentRanges').style.display='none';" +
                            "document.getElementById('appointmentOverview').style.width='90vw';" +
                            //"var calenderDlIconList = document.getElementById('appointmentOverview').getElementsByClassName('_ical');"+
                            //"for (var i=0; i < calenderDlIconList.length; i++){ calenderDlIconList[i].style.display ='none';}" +
                            // FIXME: 05.01.18 iCal Element ausblenden
                        "}"+
                        "})()");

                if (personalGreetingDisplayPref) {
                    final String personalGreetingsLoadUrl = "javascript:(function(){document.getElementById('contentmain').getElementsByTagName('H1')[0].innerHTML = '" +
                            personalGreeting +
                            "';})()";
                    mWebView.loadUrl(personalGreetingsLoadUrl);
                }

                if (!maintenanceDisplayPref) {
                    //Zuerst abbrüfen ob es überhaupt Ankündigungen gibt, bevor random content gelöscht wird.
                    mWebView.loadUrl("javascript:(function(){" +
                        "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Ankündigung Wartungsarbeiten') > -1) {"+
                            "document.body.innerHTML = document.body.innerHTML.replace('<h2>Ankündigung Wartungsarbeiten</h2>', '');" +
                            "document.getElementById('contentmain').getElementsByTagName('UL')[0].innerHTML = '';}" +
                         "})()");
                }

                if (!recertificationDisplayPref) {
                    mWebView.loadUrl("javascript:(function(){" +
                        "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Rezertifizierungsstatus') > -1) {"+
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<h2>Rezertifizierungsstatus[\\s\\S]+?<\\/p>)/, '');" +
                            "}})()");
                }

                if (!upcomingDutiesDisplayPref) {
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.body.innerHTML = document.body.innerHTML.replace('<h2>Nächste eingeteilte Dienste</h2>', '');" +
                            "var upcomingDutyList = document.getElementById('contentmain').getElementsByClassName('_upcomingDuties');" +
                            "for (var i=0; i < upcomingDutyList.length; i++){ upcomingDutyList[i].outerHTML ='';}" +
                            "})()");
                } else { //kalendergrafiken und downloadlinks ausblenden, die braucht kein Mensch.
                    mWebView.loadUrl("javascript:(function(){" +
                            "if((document.documentElement.textContent || document.documentElement.innerText).indexOf('Nächste eingeteilte Dienste') > -1){"+
                            "var calenderIconList = document.getElementById('contentmain').getElementsByClassName('fa fa-calendar');"+
                            "for (var i=0; i < calenderIconList.length; i++){ calenderIconList[i].style.display ='none';}" +
                            "}})()");
                }
                if (!openDutiesDisplayPref) {
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.body.innerHTML = document.body.innerHTML.replace('<h2>Offene Dienste</h2>', '');" +
                            "var openDutyList = document.getElementById('contentmain').getElementsByClassName('_advertisedRoles');"+
                            "for (var i=0; i < openDutyList.length; i++){ openDutyList[i].outerHTML ='';}" +
                            "})()");
                }

                if (!eventsDisplayPref) {
                    /*zuerst überprüfen ob es momentan die Geburtstagsliste gibt.
                    * Wenn ja kommentiere alles bis zur Geburtstagsliste aus.
                    * Wenn nein, kommentier alles bis zum cleaner ("<!-- -->") aus.
                    * TODO: Schau ob da die "Aktuellen Informationen" nicht reinfallen!
                    * TODO: REGEX, sobald aktuelle Termine verfügbar sind.
                    */
                    mWebView.loadUrl("javascript:(function(){" +
                            //es gibt keine Termine:
                            "if((document.documentElement.textContent || document.documentElement.innerText).indexOf('Aktuelle Termine') <= -1){}" +
                            //es gibt Termine:
                            "else if(( document.documentElement.textContent || document.documentElement.innerText).indexOf('Aktuelle Termine') > -1){" +
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<h2>Aktuelle[\\s\\S]+?<\\/ul>)/, '');}" +
                            "})()");

                }

                if (!enrolledEventsDisplayPref) {
                    mWebView.loadUrl("javascript:(function(){" +
                            "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Anmeldungen zu Terminen') > -1) {"+
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<h2>Anmeldungen[\\s\\S]*?<\\/ul>)/, '');}" +
                            "})()");
                }

                if (!birthdayDisplayPref) {
                    //REGEX: /(<h2>Die[\s\S]*?<\/ul>)/ = VON "<h2>Die" BIS ZUM NÄCHSTEN "</ul>" ALLES AUSWÄHLEN
                   mWebView.loadUrl("javascript:(function(){" +
                           "if (( document.documentElement.textContent || document.documentElement.innerText).indexOf('Geburtstagskinder') > -1) {"+
                           "document.body.innerHTML = document.body.innerHTML.replace(/(<h2>Die[\\s\\S]+?<\\/ul>)/, '');}" +
                           "})()");
                }


                //Mega IF am Anfang um zu überprüfen ob wirklich kein Inhalt da ist!
                //Theoretisch sollte das auch den Case abdecken wenn in den Prefs nix ausgewählt wurde.
                mWebView.loadUrl("javascript:(function(){" +
                        "if ( " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Rezertifizierungsstatus') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Offene Dienste') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Geburtstagskinder') <= -1 && "+
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Nächste eingeteilte Dienste') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Wartungsarbeiten') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Aktuelle Termine') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Anmeldungen zu Terminen') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('als gelesen markieren') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Anzeigezeitraum') <= -1 && " +
                        "(document.documentElement.textContent || document.documentElement.innerText).indexOf('Passwort') <= -1 " +
                        ") {" +
                        "document.body.innerHTML = document.body.innerHTML.replace('</h1>', '</h1><br><h2>Keine anzuzeigenden Inhalte</h2>');" +
                        "}" +
                        "})()");

                //set up the fab to show an forward arrow, if the bulletin site is displayed.
                if (currentUrl != null && currentUrl.equals("https://dienstplan.st.roteskreuz.at/Information/SchwarzesBrett")) {
                    fab.setImageResource(R.drawable.ic_arrow_forward_24dp); //
                } else {
                    fab.setImageResource(R.drawable.ic_fab_autorenew_24dp);
                }
                //Den Link zum Passwort ändern entfernen. hier .contains() und nicht .equals() weil es verschiedene
                //returnUrl paths gibt.
                if (currentUrl != null && currentUrl.contains("/Login")) {
                    mWebView.loadUrl("javascript:(function(){" +
                            "document.body.innerHTML = document.body.innerHTML.replace(/(<p><a[\\s\\S]+?<\\/p>)/, '');" +
                            "})()");
                }

                if (unreadBulletinPostsAvailable || !maintenanceDisplayPref || !recertificationDisplayPref || !upcomingDutiesDisplayPref ||
                        !openDutiesDisplayPref || !eventsDisplayPref || !birthdayDisplayPref) {
                    //Aktiviert das Script zum Dienst wünschen, nachdems mit innerHTML oftmalig nutzlos gemacht wurde.
                    mWebView.loadUrl("javascript:" +
                            "$(function () {" +
                            "instance = new DiversysScript.HomePage();" +
                            "var instance2 = new Home();" +
                            "});"
                    );
                }

                findViewById(R.id.textLoading).setVisibility(View.GONE); //hide loading text
                mWebView.setVisibility(View.VISIBLE); //show the fckn content!
                mySwipeRefreshLayout.setRefreshing(false); //hide refreshing icon


                writeUserToDatabase(user);

            }

        });

         //set up the fab to use the renew graphic and the OnClickListener
        fab.setImageResource(R.drawable.ic_fab_autorenew_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("https://dienstplan.st.roteskreuz.at");
            }
        });

        //set the WebChromeClient for using javascript like confirm(); and alert();
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.setVisibility(View.GONE);
        mySwipeRefreshLayout.setRefreshing(true);
        mWebView.loadUrl("https://dienstplan.st.roteskreuz.at"); //Lad die aktuelle Seite

        updateRegistrationInfo();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        WebView mWebView = findViewById(R.id.webview);

        //Wenn das Menü draussen is, klapps ein.
        //Wenn der WebView zurückgehen kann, geh zrück.
        //Sonst: geh aus der app raus.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onResume(){
        setCheckedToolbarItem();
        super.onResume();
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
            //Nichts machen, da diese Activity bereits ausgeführt wird.
        } else if (id == R.id.nav_info) {
            Intent daten = new Intent(this, MeineDatenActivity.class);
            startActivity(daten);
        } else if (id == R.id.nav_wishes) {
            Intent wuensche = new Intent(this, MeineWuenscheActivity.class);
            startActivity(wuensche);
        } else if (id == R.id.nav_functions) {
            Intent funktionen = new Intent(this, MeineFunktionenActivity.class);
            startActivity(funktionen);
        } else if (id == R.id.nav_trainings) {
            Intent ausbildungen = new Intent(this, MeineAusbildungenActivity.class);
            startActivity(ausbildungen);
        } else if (id==R.id.nav_holidays){
            Intent urlaube = new Intent(this, MeineUrlaubeActivity.class);
            startActivity(urlaube);
        } else if (id == R.id.nav_stats) {
            Intent statistik = new Intent(this, MeineStatistikActivity.class);
            startActivity(statistik);
        } else if (id == R.id.nav_bulletin) {
            Intent bulletin = new Intent(this, BulletinActivity.class);
            startActivity(bulletin);
        } else if (id == R.id.nav_vcards) {
            Intent vcard = new Intent(this, vCardActivity.class);
            startActivity(vcard);
        } else if (id == R.id.nav_contribute) {
            Intent contribute = new Intent(this, ContributeActivity.class);
            startActivity(contribute);
        } else if (id == R.id.nav_downloads) {
            Intent downloads = new Intent(this, DownloadsActivity.class);
            startActivity(downloads);
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
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
