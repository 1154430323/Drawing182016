package com.tom.mydrawing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Created by tom on 2016/4/23.
 */
public class Splash extends Activity{

    private final int SPLASH_DISPLAY_TIME = 3000;

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }
    private static final String LAST_APP_VERSION = "last_app_version";
    private static final String TAG = "Splash";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
//

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                switch (checkAppStart()) {
                    case NORMAL:{
                        // We don't want to get on the user's nerves
                        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                        break;
                    }
                    case FIRST_TIME_VERSION:{
                        // TODO show what's new
                        Intent mainIntent = new Intent(Splash.this, IntroductionViewPageActivity.class);

                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();

                        break;
                    }
                    case FIRST_TIME:{
                        // TODO show a tutorial
                        Intent mainIntent = new Intent(Splash.this, IntroductionViewPageActivity.class);
//               Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                        break;
                    }
                    default:
                        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                        break;
                }

            }

        }, SPLASH_DISPLAY_TIME);


    }

    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppStart appStart = AppStart.NORMAL;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG,
                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }

    protected AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w(TAG, "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defenisvely assuming normal app start.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }


}
