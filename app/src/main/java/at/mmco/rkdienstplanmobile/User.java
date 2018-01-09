package at.mmco.rkdienstplanmobile;

/*
 *  Class representing the user containing all important values that define a user.
 *  Contains: username, Full name, fcmToken, 8 digit PersonalID, and the Version of the app the user currently uses.
 *  Intended to be fully saved to the database as an object
 */


@SuppressWarnings("unused")
public class User {


    //username contains the unique username used by the dienstplan-login
    private String username;
    //fullName contains surname and all given Names of the user (only available if it is present in the html and can be parsed)
    private String fullName;
    //fcmToken is a unique token used by firebase cloud massaging service to send personal notifications
    private String fcmToken;
    //peronalId is an 8 digit number consisting of: 3 digits departmentID, 3 digits running number, 2 digits year of entry.
    private String personalId;
    //adRewardCounter represent the amount of ad videos the user has fully watched.
    private int adRewardCounter;
    //appOpeningCounter represents the amount of times the user has opened the app (created the MainActivity)
    private int appOpeningCounter;
    //currentVersion is always the current VERSION_NAME. Provides information about the app version, making bug-hunting easier.
    private String currentVersion;

    //public String departement; //??

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.username = "%username";
        this.fullName = "%fullName";
        this.fcmToken = "%fcmToken";
        this.personalId = "%personalID";
        this.adRewardCounter = 0;
        this.appOpeningCounter = 0;
        this.currentVersion = BuildConfig.VERSION_NAME;
        // add any further values here
    }

    public User(String username, String fullName, String fcmToken, String personalId, int adRewardCounter, int appOpeningCounter) {
        // Constructor used to generate a full user object
        this.username = username;
        this.fullName = fullName;
        this.fcmToken = fcmToken;
        this.personalId = personalId;
        this.adRewardCounter = adRewardCounter;
        this.appOpeningCounter = appOpeningCounter;
        this.currentVersion = BuildConfig.VERSION_NAME;
        // add any further values here
    }

    /* ---Getter Methods---  */
    public String getUsername() {
        return username;
    }
    public String getFullName() {
        return fullName;
    }
    public String getFcmToken() {
        return fcmToken;
    }
    public String getPersonalId() {
        return personalId;
    }
    public int getAdRewardCounter() {
        return adRewardCounter;
    }
    public int getAppOpeningCounter() {
        return appOpeningCounter;
    }
    public String getCurrentVersion() {
        return currentVersion;
    }

    /* ---Setter Methods---  */
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
    public void setAdRewardCounter(int adRewardCounter) {
        this.adRewardCounter = adRewardCounter;
    }
    public void setAppOpeningCounter(int appOpeningCounter) {
        this.appOpeningCounter = appOpeningCounter;
    }
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
}


